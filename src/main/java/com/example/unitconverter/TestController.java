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
        return "redirect:/length";
    }

    @GetMapping("/weight")
    public String weight(Model model) {
        List<String> weightUnits = List.of("kg", "lb", "oz", "g", "mg");
        model.addAttribute("units", weightUnits);
        model.addAttribute("activeTab", "weight");
        return "index";
    }

    @GetMapping("/length")
    public String length(Model model) {
        List<String> lengthUnits = List.of("m", "cm", "mm", "km", "ft", "in");
        model.addAttribute("units", lengthUnits);
        model.addAttribute("activeTab", "length");
        return "index";
    }

    @GetMapping("/temperature")
    public String temperature(Model model) {
        List<String> tempUnits = List.of("C", "F", "K");
        model.addAttribute("units", tempUnits);
        model.addAttribute("activeTab", "temperature");
        return "index";
    }

    @PostMapping("/convert")
    public String convert(@RequestParam double value,
                          @RequestParam String fromUnit,
                          @RequestParam String toUnit,
                          @RequestParam UnitCategory category, // ← use Enum here
                          Model model) {

        double result;

        switch (category) {
            case TEMPERATURE:
                result = convertTemperature(value, fromUnit, toUnit);
                break;
            case WEIGHT:
                double fromWeight = weightConversionRates.getOrDefault(fromUnit, 1.0);
                double toWeight = weightConversionRates.getOrDefault(toUnit, 1.0);
                result = value * (toWeight / fromWeight);
                break;
            case LENGTH:
                double fromLength = lengthConversionRates.getOrDefault(fromUnit, 1.0);
                double toLength = lengthConversionRates.getOrDefault(toUnit, 1.0);
                result = value * (toLength / fromLength);
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        model.addAttribute("value", value);
        model.addAttribute("result", result);
        model.addAttribute("fromUnit", fromUnit);
        model.addAttribute("toUnit", toUnit);
        model.addAttribute("activeTab", category.name().toLowerCase()); // for HTML

        switch (category) {
            case WEIGHT:
                model.addAttribute("units", weightConversionRates.keySet());
                break;
            case LENGTH:
                model.addAttribute("units", lengthConversionRates.keySet());
                break;
            case TEMPERATURE:
                model.addAttribute("units", List.of("C", "F", "K"));
                break;
        }

        return "index";
    }

    // For weight
    private static final Map<String, Double> weightConversionRates = Map.of(
            "kg", 1.0,
            "lb", 2.20462,
            "g", 1000.0,
            "mg", 1_000_000.0,
            "oz", 35.274
    );

    // For length
    private static final Map<String, Double> lengthConversionRates = Map.of(
            "m", 1.0,
            "cm", 100.0,
            "mm", 1000.0,
            "km", 0.001,
            "inch", 39.3701,
            "foot", 3.28084
    );

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals(toUnit)) {
            return value; // no conversion needed
        }

        switch (fromUnit + "_to_" + toUnit) {
            case "C_to_F":
                return (value * 9/5) + 32;
            case "F_to_C":
                return (value - 32) * 5/9;
            case "C_to_K":
                return value + 273.15;
            case "K_to_C":
                return value - 273.15;
            case "F_to_K":
                return (value - 32) * 5/9 + 273.15;
            case "K_to_F":
                return (value - 273.15) * 9/5 + 32;
            default:
                throw new IllegalArgumentException("Unsupported temperature conversion: " + fromUnit + " to " + toUnit);
        }
    }
}
