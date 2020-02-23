package com.hashtag_finder.services;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.HashtagFinder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HashtagFinderService {

    List<HashtagFinder> findHashtagsBySearchWord(GetHashtagInput input);

    void insertIntoDB(HashtagFinder hashtagFinder);

}
