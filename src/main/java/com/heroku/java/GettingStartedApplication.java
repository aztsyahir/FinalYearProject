package com.heroku.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class GettingStartedApplication {

    @GetMapping("/")
    public String index() {
        return "Home";
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }

}
