package com.example.demo.service;

import com.example.demo.dto.request.EmailRequest;
import com.example.demo.dto.respose.ApiResponse;
import com.example.demo.dto.respose.UserIdentityAvailability;
import com.example.demo.dto.respose.UserProfile;
import com.example.demo.dto.respose.UserSummary;
import com.example.demo.entity.User;
import com.example.demo.security.UserPrincipal;

public interface UserService {

	UserSummary getCurrentUser(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String email);

	UserProfile getUserProfile(String username);

	User addUser(User user);

	User updateUser(User newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String username, UserPrincipal currentUser);

	ApiResponse giveAdmin(String username);

	ApiResponse removeAdmin(String username);

	Boolean changePassword(EmailRequest email);


}