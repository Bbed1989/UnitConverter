package com.example.unitconverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/")
    public String home(Model model) {
        List<String> units = List.of("kg", "lb", "oz", "g", "mg");
        model.addAttribute("units", units);
        return "index"; // This will map to src/main/resources/templates/index.html
    }

    @PostMapping("/convert")
    public String convert(@RequestParam double value,
                          @RequestParam String fromUnit,
                          @RequestParam String toUnit,
                          Model model) {

        double fromRate = conversionRates.getOrDefault(fromUnit, 1.0);
        double toRate = conversionRates.getOrDefault(toUnit, 1.0);

        double result = value * (toRate / fromRate);

        model.addAttribute("result", result);
        model.addAttribute("units", conversionRates.keySet());
        model.addAttribute("fromUnit", fromUnit);
        model.addAttribute("toUnit", toUnit);

        return "index";
    }

    private static final Map<String, Double> conversionRates = Map.of(
            "kg", 1.0,
            "lb", 2.20462,
            "g", 1000.0,
            "oz", 35.274
    );
}
