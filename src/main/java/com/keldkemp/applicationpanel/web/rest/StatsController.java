package com.keldkemp.applicationpanel.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @GetMapping("/check")
    public Boolean checkAuth() {
        return true;
    }
}
