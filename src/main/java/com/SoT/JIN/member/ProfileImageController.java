package com.SoT.JIN.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ProfileImageController {

    @GetMapping("/profile")
    public String showProfileImageForm() {
        return "ProfileImage";
    }

    private final UserRepository userRepository;

    @Autowired
    public ProfileImageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/profile/upload")
    public String uploadProfileImage(@RequestParam("image_url") String imageUrl,
                                     Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return "redirect:/my-page";
    }
}