package com.hashtag_finder.services;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.repositories.HashtagFinderRepo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class Instagram_HashtagFinderServiceImpl implements HashtagFinderService {

    @Autowired
    HashtagFinderRepo hashtagFinderRepo;

    @Override
    public List<HashtagFinder> findHashtagsBySearchWord(GetHashtagInput input) {
        String searchWord = input.getSearchWord();
        List<HashtagFinder> result = hashtagFinderRepo.findBySearchWord(searchWord);
        if(result == null || result.size() == 0) {
            try {
                List<HashTagPair> hs = this.getHashTags(searchWord);
                System.out.println(hs);
            }catch (Exception ex)
            {

            }

        }else
        {
            return hashtagFinderRepo.findBySearchWord(searchWord);
        }
        return null;
    }


    public List<HashTagPair> getHashTags(String hashtag) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //maximize window
        driver.manage().window().maximize();
        //open browser with desired URL
        driver.get("https://www.instagram.com/explore/tags/instagram");
        WebElement input = driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[2]/input"));
        input.sendKeys(hashtag);
        Thread.sleep(2000);
        //Get the values of the result
        List<WebElement> results = driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/div[2]/div[2]/div/a"));
        List<HashTagPair> topTenHashTags = new ArrayList<>();
        for(WebElement element : results)
        {
            if(element.getAttribute("href").contains("https://www.instagram.com/explore/tags/"))
            {
                String showAutocomplete = element.findElement(By.xpath("div/div/div[1]/span")).getAttribute("innerHTML");
                String getTagFollowers = element.findElement(By.xpath("div/div/div[2]/span/span")).getAttribute("innerHTML");
                HashTagPair pair = new HashTagPair(showAutocomplete, getTagFollowers);
                topTenHashTags.add(pair);
            }
        }
        return getTopTenHashTags(topTenHashTags);
        //closing the browser
        //driver.close();

    }

    public List<HashTagPair> getTopTenHashTags(List<HashTagPair> hashTagPairs)
    {
        Collections.sort(hashTagPairs, new SortByCount());
        return hashTagPairs.subList(0, 10);
    }
    class HashTagPair
    {
        String tagName;
        String tagCount;

        public HashTagPair(String tagName, String tagCount)
        {
            this.tagName = tagName;
            this.tagCount = tagCount;
        }

        public String toString()
        {
            return tagName + ": " + tagCount + " followers";
        }
    }

    class SortByCount implements Comparator<HashTagPair>
    {
        public int compare(HashTagPair a, HashTagPair b)
        {
            return a.tagCount.compareTo(a.tagCount);
        }
    }

    public void getHashtag()
    {
        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new FirefoxDriver();
    }


    public List<HashtagFinder> findAll() {
        return hashtagFinderRepo.findAll();
    }

    @Override
    public void insertIntoDB(HashtagFinder hashtagFinder) {
        hashtagFinderRepo.insert(hashtagFinder);
    }

}
