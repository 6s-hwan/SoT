package com.SoT.JIN.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/writer")
public class FollowController {

    private final UserRepository userRepository;

    @Autowired
    public FollowController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<String> followUser(@PathVariable("username") String username) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("로그인 유저를 찾을 수 없습니다."));
            User userToFollow = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("팔로우할 유저를 찾을 수 없습니다."));

            if (currentUser.equals(userToFollow)) {
                return ResponseEntity.badRequest().body("자기 자신을 팔로우할 수 없습니다.");
            }

            if (currentUser.getFollowing().contains(userToFollow)) {
                currentUser.unfollow(userToFollow);
            } else {
                currentUser.follow(userToFollow);
            }

            userToFollow.setFollowCount(userToFollow.getFollowers().size());
            userRepository.save(userToFollow);
            userRepository.save(currentUser);

            return ResponseEntity.ok(currentUser.getFollowing().contains(userToFollow) ? "followed" : "unfollowed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    @PostMapping("/{username}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser != null) {
            User userToUnfollow = userRepository.findByUsername(username).orElse(null);
            if (userToUnfollow != null) {
                currentUser.getFollowing().remove(userToUnfollow);
                userRepository.save(currentUser);
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/follows")).build();
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
