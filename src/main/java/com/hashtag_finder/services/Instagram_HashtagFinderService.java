package com.hashtag_finder.services;

import com.hashtag_finder.models.HashtagFinder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface Instagram_HashtagFinderService {

//    List<HashtagFinder> findBySearch_word(String search_word);

    List<HashtagFinder> findAll();
    void insert();

//    void saveOrUpdateHashtagFinder(HashtagFinder hashtagFinder);
//
//    void deleteHashtagFinder(String id);
}
