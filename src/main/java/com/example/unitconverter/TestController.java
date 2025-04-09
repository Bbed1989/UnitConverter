package com.example.unitconverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class TestController {

    @GetMapping("/")
    public String home() {
        return "index"; // This will map to src/main/resources/templates/index.html
    }

    @PostMapping("/convert")
    public String convert(@RequestParam double value,
                          @RequestParam String unit,
                          Model model) {
        double result = unit.equals("kgToLb") ? value * 2.20462 : value / 2.20462;
        model.addAttribute("result", result);
        return "index";
    }
}
