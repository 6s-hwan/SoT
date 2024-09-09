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
            // 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증 정보가 없는 경우 처리
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 팔로우할 수 있습니다.");
            }

            String currentUserEmail = authentication.getName();

            // 로그인된 유저 정보 조회
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("로그인 후 팔로우할 수 있습니다."));

            // 팔로우하려는 유저 정보 조회
            User userToFollow = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("팔로우할 유저를 찾을 수 없습니다."));

            // 자기 자신을 팔로우하려는 경우 처리
            if (currentUser.equals(userToFollow)) {
                return ResponseEntity.badRequest().body("자기 자신을 팔로우할 수 없습니다.");
            }

            // 이미 팔로우한 유저인 경우 언팔로우 처리
            if (currentUser.getFollowing().contains(userToFollow)) {
                currentUser.unfollow(userToFollow);
                userToFollow.setFollowCount(userToFollow.getFollowers().size());
                userRepository.save(userToFollow);
                userRepository.save(currentUser);
                return ResponseEntity.ok("언팔로우 했습니다.");
            }

            // 팔로우 처리
            currentUser.follow(userToFollow);
            userToFollow.setFollowCount(userToFollow.getFollowers().size());
            userRepository.save(userToFollow);
            userRepository.save(currentUser);

            return ResponseEntity.ok("팔로우 했습니다.");
        } catch (RuntimeException e) {
            // 유저를 찾지 못한 경우 등 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 일반적인 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다. 다시 시도해 주세요.");
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
