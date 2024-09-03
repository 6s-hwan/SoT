package com.SoT.JIN.bookmark;

import com.SoT.JIN.story.StoryService;
import com.SoT.JIN.user.UserRepository;
import com.SoT.JIN.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @Autowired
    private StoryService storyService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/story/{storyId}/bookmark")
    public ResponseEntity<Void> toggleBookmark(@PathVariable Long storyId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        logger.debug("Received bookmark request. StoryId: {}, UserId: {}", storyId, user.getUserId());
        storyService.toggleBookmark(storyId, user);
        return ResponseEntity.ok().build();
    }



    private User getUserFromPrincipal(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
