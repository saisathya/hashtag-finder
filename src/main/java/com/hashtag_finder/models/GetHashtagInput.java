package com.hashtag_finder.models;

import javax.validation.constraints.NotNull;

public class GetHashtagInput {
    @NotNull(message = "searchWord cannot be null. Please make sure your requestBody contains searchWord")
    private String searchWord;

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

}
