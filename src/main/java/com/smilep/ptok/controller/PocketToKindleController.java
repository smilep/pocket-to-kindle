package com.smilep.ptok.controller;

import com.smilep.ptok.service.PocketToKindleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PocketToKindleController {

    private PocketToKindleService pocketToKindleService;

    public PocketToKindleController(PocketToKindleService pocketToKindleService) {
        this.pocketToKindleService = pocketToKindleService;
    }

    @GetMapping("/send/{count}")
    public String send(@PathVariable int count) {
        pocketToKindleService.trigger(count);
        return "You sent count " + count + " in request!";
    }
}
