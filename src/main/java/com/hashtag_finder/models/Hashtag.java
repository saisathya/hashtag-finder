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
    int hashtagPostNumber; //contains Hashtag post number

    public Hashtag(String hashtagName, String hashtagPostNumber)
    {
        this.hashtagName = hashtagName;
        this.hashtagPostNumber = filterOutCommaInStringAndConvertToInt(hashtagPostNumber);
    }

    public Hashtag()
    {
        this.hashtagPostNumber = -1;
        this.hashtagName = null;
    }

    public int filterOutCommaInStringAndConvertToInt(String number)
    {
        System.out.println("Before"+number);
        String s = number.replaceAll(",","");
        System.out.println("After"+s);
        return Integer.parseInt(s);
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

    public void setHashtagPostNumber(String hashtagPostNumber) {
        this.hashtagPostNumber = filterOutCommaInStringAndConvertToInt(hashtagPostNumber);
    }

    public String toString()
    {
        return hashtagName + ": " + hashtagPostNumber + " followers";
    }

    @Override
    public int compareTo(Hashtag o) {
        return o.hashtagPostNumber - this.hashtagPostNumber;
    }
}
