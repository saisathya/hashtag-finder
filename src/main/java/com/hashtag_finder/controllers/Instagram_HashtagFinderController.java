package com.hashtag_finder.controllers;

import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.services.Instagram_HashtagFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Instagram_HashtagFinderController {

    @Autowired
    Instagram_HashtagFinderService instagram_hashtagFinderService;

    @GetMapping("/findall")
    public ResponseEntity<?> getAll() {
        List<HashtagFinder> result = instagram_hashtagFinderService.findAll();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/add")
    public String add() {
        instagram_hashtagFinderService.insert();
        return "Added new entity";
    }

}
