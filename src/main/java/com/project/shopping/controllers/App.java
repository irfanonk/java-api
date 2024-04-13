package com.project.shopping.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App {

    @RequestMapping(value = "/")
    public String index() {
        return "Hello. App is running";
    }

}
