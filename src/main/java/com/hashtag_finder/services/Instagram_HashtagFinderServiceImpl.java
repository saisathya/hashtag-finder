package com.hashtag_finder.services;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.repositories.HashtagFinderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Instagram_HashtagFinderServiceImpl implements HashtagFinderService {

    @Autowired
    HashtagFinderRepo hashtagFinderRepo;

    @Override
    public List<HashtagFinder> findHashtagsBySearchWord(GetHashtagInput input) {
        String searchWord = input.getSearchWord();
        List<HashtagFinder> result = hashtagFinderRepo.findBySearchWord(searchWord);
        if(result == null || result.size() == 0) {
            // Run Scrapper
        }else
        {
            return hashtagFinderRepo.findBySearchWord(searchWord);
        }
        return null;
    }

    public List<HashtagFinder> findAll() {
        return hashtagFinderRepo.findAll();
    }

    @Override
    public void insertIntoDB(HashtagFinder hashtagFinder) {
        hashtagFinderRepo.insert(hashtagFinder);
    }

}
