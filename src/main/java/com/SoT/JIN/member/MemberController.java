package com.SoT.JIN.member;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @GetMapping({"/join"})
    String register() {
        return "Join";
    }

    @GetMapping({"/home"})
    String list() {
        return "home";
    }

    @PostMapping({"/user"})
    String addUser(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("username") String username, @RequestParam("gender") String gender, @RequestParam("birth_year") String birthYear, @RequestParam("birth_month") String birthMonth, @RequestParam("birth_day") String birthDay, @RequestParam("phone1") String phone1, @RequestParam("phone2") String phone2, @RequestParam("phone3") String phone3) {
        String phone = phone1 + phone2 + phone3;
        String birth = birthYear + "-" + birthMonth + "-" + birthDay;
        User user = new User();
        user.setEmail(email);
        user.setPassword((new BCryptPasswordEncoder()).encode(password));
        user.setUsername(username);
        user.setGender(gender);
        user.setBirth(birth);
        user.setPhonenumber(phone);
        this.userRepository.save(user);
        return "redirect:/test";
    }
    /*
    @GetMapping({"/login"})
    public String login() {
        return "login.html";
    }

     */

    public MemberController(final UserRepository userRepository, final S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }
}
