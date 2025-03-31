package com.chikacow.kohimana.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

public class TestController {
    @GetMapping("/v3/shibal")
    public String shibal() {
        return "shibal";
    }
}
