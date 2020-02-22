package com.hashtag_finder.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/*
Note: We need to make sure each class in our model class has getter and setter
in order to make object serilizable and store in database

This class contains hashtag name and its properties such as post numbers.
 */
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
