package com.hashtag_finder.services;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.Hashtag;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.repositories.HashtagFinderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InstagramHashtagFinderServiceImpl implements HashtagFinderService {

    @Autowired
    HashtagFinderRepo hashtagFinderRepo;

    @Autowired
    InstagramHashtagCrawler instagramHashtagCrawler;

    @Override
    public List<HashtagFinder> findHashtagsBySearchWords(GetHashtagInput input) {
        List<String> searchWords = input.getSearchWords();
        List<HashtagFinder> hashtagFinders = new ArrayList<>();

        for(String searcWord: searchWords)
        {
            if(searcWord.length() >= 1) hashtagFinders.add(findHashtagsBySearchWord(searcWord));
        }

        return hashtagFinders;
    }

    public HashtagFinder findHashtagsBySearchWord(String searchWord)
    {
        List<HashtagFinder> results = hashtagFinderRepo.findBySearchWord(searchWord);
        if(results == null || results.size() == 0) {
            try {
                List<Hashtag> hashtags = instagramHashtagCrawler.runGetHashtagsCrawler(searchWord);
                HashtagFinder hf = new HashtagFinder();
                hf.setHashtags(hashtags);
                hf.setSearchWord(searchWord);
                results.add(hf);
                this.insertIntoDB(hf);
            }catch (Exception ex)
            {
            }
        }
        return new HashtagFinder();
        //return results.get(0);
    }


    public List<HashtagFinder> findAll() {
        return hashtagFinderRepo.findAll();
    }

    @Override
    public void insertIntoDB(HashtagFinder hashtagFinder) {
        hashtagFinderRepo.insert(hashtagFinder);
    }

}
