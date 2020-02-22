package com.hashtag_finder.repositories;

import com.hashtag_finder.models.HashtagFinder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagFinderRepo extends MongoRepository<HashtagFinder, String> {
    List<HashtagFinder> findBySearchWord(String searchWord);

//    void delete(String id);
}
