package com.groovin101.gow.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for serving the web UI.
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}


