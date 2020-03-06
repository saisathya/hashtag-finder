package com.hashtag_finder.controllers;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.services.HashtagFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hashtagFinder/instagram")
public class InstagramHashtagFinderController {

    @Autowired
    HashtagFinderService instagram_hashtagFinderService ;

    @PostMapping(path = "/getHashtags", consumes="application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getHashtags(@Valid @RequestBody GetHashtagInput input) {
        List<HashtagFinder> result = instagram_hashtagFinderService.findHashtagsBySearchWords(input);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
