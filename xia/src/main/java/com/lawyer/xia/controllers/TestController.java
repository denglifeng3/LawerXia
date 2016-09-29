package com.lawyer.xia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lindeng on 9/23/2016.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "Hello xia!";
    }
}
