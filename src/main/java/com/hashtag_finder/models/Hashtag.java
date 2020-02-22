package com.hashtag_finder.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;


public class Hashtag {

    String hashtagName;
    int hashtagPostNumber; //contains Hashtag post number

    public Hashtag(String hashtagName, int hashtagPostNumber)
    {
        this.hashtagName = hashtagName;
        this.hashtagPostNumber = hashtagPostNumber;
    }

    public int getHashtagPostNumber() {
        return hashtagPostNumber;
    }

    public String getHashtagName() {
        return hashtagName;
    }

    public void setHashtagName(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public void setHashtagPostNumber(int hashtagPostNumber) {
        this.hashtagPostNumber = hashtagPostNumber;
    }
}
