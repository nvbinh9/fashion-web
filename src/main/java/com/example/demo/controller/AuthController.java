package com.example.demo.controller;


import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.common.exception.error.AppException;
import com.example.demo.common.exception.BlogapiException;
import com.example.demo.dto.request.EmailRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignUpRequest;
import com.example.demo.dto.respose.ApiResponse;
import com.example.demo.dto.respose.JwtAuthenticationResponse;
import com.example.demo.dto.respose.UserProfile;
import com.example.demo.entity.JwtToken;
import com.example.demo.entity.User;
import com.example.demo.entity.role.Role;
import com.example.demo.entity.role.RoleName;
import com.example.demo.repository.JwtTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	private static final String USER_ROLE_NOT_SET = "User role not set";
	private static final String KEY = "USER";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtTokenRepository jwtTokenRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private ModelMapper mapper;


	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtTokenProvider.generateToken(authentication);
		Long userid = jwtTokenProvider.getUserIdFromJWT(jwt);

		JwtToken token = new JwtToken();
		token.setToken(jwt);
		token.setUserid(userid);
		token.activate();
		jwtTokenRepository.save(token);

		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			throw new BlogapiException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			throw new BlogapiException(HttpStatus.BAD_REQUEST, "Email đã tồn tại.");
		}

		String firstName = signUpRequest.getFirstName().toLowerCase();

		String lastName = signUpRequest.getLastName().toLowerCase();

		String username = signUpRequest.getUsername().toLowerCase();

		String email = signUpRequest.getEmail().toLowerCase();

		String password = passwordEncoder.encode(signUpRequest.getPassword());

		User user = new User(firstName, lastName, username, email, password);

		List<Role> roles = new ArrayList<>();

		if (userRepository.count() == 0) {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
			roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		} else {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		}

		user.setRoles(roles);

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
				.buildAndExpand(result.getId()).toUri();

		// save cache server(redis):
		try {
			UserProfile userProfile = mapper.map(user, UserProfile.class);
			redisTemplate.opsForHash().put(KEY, userProfile.getId(), userProfile);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Đăng ký thành công."));
	}

	@PostMapping("/changePassword")
	public ResponseEntity<Boolean> changePassword(@RequestBody EmailRequest email) {
		return ResponseEntity.ok(userService.changePassword(email));
	}


}
