package com.hashtag_finder.services;

import com.hashtag_finder.models.Hashtag;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.repositories.HashtagFinderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Instagram_HashtagFinderServiceImpl implements Instagram_HashtagFinderService {

    @Autowired
    HashtagFinderRepo hashtagFinderRepo;

//    @Override
//    public List<HashtagFinder> findBySearch_word(String search_word) {
//        return hashtagFinderRepo.findBySearch_word(search_word);
//    }

    @Override
    public List<HashtagFinder> findAll() {
        return hashtagFinderRepo.findAll();
    }

    @Override
    public void insert() {
        List<Hashtag> hashtags = new ArrayList<>();
        hashtags.add(new Hashtag("food", 2020));
        hashtags.add(new Hashtag("foo", 2020323));
        HashtagFinder hashtagFinder1 = new HashtagFinder();
        hashtagFinder1.setSearch_word("foo");
        hashtagFinder1.setHashtags(hashtags);
        hashtagFinderRepo.insert(hashtagFinder1);
    }

//    @Override
//    public void saveOrUpdateHashtagFinder(HashtagFinder hashtagFinder) {
//        hashtagFinderRepo.save(hashtagFinder);
//    }
//
//    @Override
//    public void deleteHashtagFinder(String id) {
//        hashtagFinderRepo.delete(id);
//    }
}
