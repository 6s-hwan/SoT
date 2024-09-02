package com.SoT.JIN.bookmark;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/story/{storyId}/bookmark")
    public String toggleBookmark(@PathVariable Long storyId, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principalObj = authentication.getPrincipal();
        String username;
        if (principalObj instanceof UserDetails) {
            username = ((UserDetails) principalObj).getUsername();
        } else {
            username = principalObj.toString();
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        bookmarkService.toggleBookmark(storyId, user);

        return "redirect:/story/" + storyId;
    }

    @GetMapping("/bookmarked-stories")
    public String getBookmarkedStories(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principalObj = authentication.getPrincipal();
        String username;
        if (principalObj instanceof UserDetails) {
            username = ((UserDetails) principalObj).getUsername();
        } else {
            username = principalObj.toString();
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Story> bookmarkedStories = bookmarkService.getBookmarkedStories(user);
        model.addAttribute("bookmarkedStories", bookmarkedStories);

        return "bookmarkedStories";
    }
}
