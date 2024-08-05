package com.SoT.JIN.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user/profile")
    public UserProfileResponse getUserProfile(Principal principal) {
        if (principal == null) {
            return new UserProfileResponse(false, null);
        }

        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            return new UserProfileResponse(false, null);
        }

        return new UserProfileResponse(true, user.getProfileImageUrl());
    }

    public static class UserProfileResponse {
        private boolean isLoggedIn;
        private String profileImageUrl;

        public UserProfileResponse(boolean isLoggedIn, String profileImageUrl) {
            this.isLoggedIn = isLoggedIn;
            this.profileImageUrl = profileImageUrl;
        }

        public boolean getIsLoggedIn() {
            return isLoggedIn;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }
    }
}
