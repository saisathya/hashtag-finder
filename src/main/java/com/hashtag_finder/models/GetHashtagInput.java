package com.hashtag_finder.models;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetHashtagInput {
    @NotNull(message = "searchWords cannot be null. Please make sure your requestBody contains searchWord")
    private List<String> searchWords;

    public String getAverageLikes() {
        return averageLikes;
    }

    public void setAverageLikes(String averageLikes) {
        this.averageLikes = averageLikes;
    }

    private String averageLikes;

    public List<String> getSearchWords() {
        return searchWords;
    }

    public void setSearchWord(List<String> searchWords) {
        this.searchWords = searchWords;
    }

}
