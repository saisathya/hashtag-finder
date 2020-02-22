package com.hashtag_finder.services;

import com.hashtag_finder.models.HashtagFinder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface Instagram_HashtagFinderService {

    List<HashtagFinder> findBySearchWord(String searchWord);

    List<HashtagFinder> findAll();

    void insert();

//    void saveOrUpdateHashtagFinder(HashtagFinder hashtagFinder);
//
//    void deleteHashtagFinder(String id);

}
