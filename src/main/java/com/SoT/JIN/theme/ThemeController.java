package com.SoT.JIN.theme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemeController {

    @GetMapping("/theme")
    public String rise() {
        return "ThemeDetailPage";
    }
}
