package com.hashtag_finder.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.Comparator;

/*
Note: We need to make sure each class in our model class has getter and setter
in order to make object serilizable and store in database

This class contains hashtag name and its properties.
 */
public class Hashtag implements Comparable<Hashtag> {

    String hashtagName;
    String hashtagPostNumber; //contains Hashtag post number

    public Hashtag(String hashtagName, String hashtagPostNumber)
    {
        this.hashtagName = hashtagName;
        this.hashtagPostNumber = hashtagPostNumber;
    }

    public String getHashtagPostNumber() {
        return hashtagPostNumber;
    }

    public String getHashtagName() {
        return hashtagName;
    }

    public void setHashtagName(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public void setHashtagPostNumber(String hashtagPostNumber) {
        this.hashtagPostNumber = hashtagPostNumber;
    }

    public String toString()
    {
        return hashtagName + ": " + hashtagPostNumber + " followers";
    }

    @Override
    public int compareTo(Hashtag o) {
        return this.hashtagPostNumber.compareTo(o.hashtagPostNumber);
    }
}
