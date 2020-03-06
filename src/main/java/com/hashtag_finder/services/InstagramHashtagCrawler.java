package com.hashtag_finder.services;

import com.hashtag_finder.models.Hashtag;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InstagramHashtagCrawler {


    public List<Hashtag> runGetHashtagsCrawler(String hashtag) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //maximize window
        driver.manage().window().maximize();
        //open browser with desired URL
        driver.get("https://www.instagram.com/explore/tags/instagram");
        WebElement input = driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[2]/input"));
        input.sendKeys("#"+hashtag);
        Thread.sleep(2000);
        //Get the values of the result
        List<WebElement> results = driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/div[2]/div[2]/div/a"));
        List<Hashtag> hashtags = new ArrayList<>();
        for(WebElement element : results)
        {
            if(element.getAttribute("href").contains("https://www.instagram.com/explore/tags/"))
            {
                String showAutocomplete = element.findElement(By.xpath("div/div/div[1]/span")).getAttribute("innerHTML");
                String getTagFollowers = element.findElement(By.xpath("div/div/div[2]/span/span")).getAttribute("innerHTML");
                Hashtag pair = new Hashtag(showAutocomplete, getTagFollowers);
                hashtags.add(pair);
            }
        }
        driver.close();
        return getSortedHashtags(hashtags);
    }

    public List<Hashtag> getSortedHashtags(List<Hashtag> hashtags)
    {
        Collections.sort(hashtags);
        return hashtags;
    }

}
