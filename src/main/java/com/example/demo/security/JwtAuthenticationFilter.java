package com.example.demo.security;

import com.example.demo.common.exception.AccessDeniedException;
import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.respose.ApiResponse;
import com.example.demo.entity.JwtToken;
import com.example.demo.entity.User;
import com.example.demo.repository.JwtTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            JwtToken jwtToken = jwtTokenRepository.findByToken(jwt);
            if(jwtToken != null) {
                Boolean jwtActivate = jwtToken.getActive();
                ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Phiên đăng nhập đã hết hạn");
                if (jwtActivate == false) throw new AccessDeniedException(apiResponse);
            }

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }


        } catch (AccessDeniedException e) {
            e.getMessage();
        } catch (Exception ex) {
            LOGGER.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public User getUser(HttpServletRequest request) {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundIdException("User không tồn tại"));

                return user;

            }
        } catch (Exception ex) {
            LOGGER.error("Could not set user authentication in security context", ex);
        }
        return null;
    }
}
