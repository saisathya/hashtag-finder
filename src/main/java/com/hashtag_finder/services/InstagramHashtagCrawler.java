package com.hashtag_finder.services;

import com.hashtag_finder.models.Hashtag;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@Service
public class InstagramHashtagCrawler {

    final double percentangeThreshold = 0.50;
    final int hashtagSizeList = 1;
    public List<Hashtag> runGetHashtagsCrawler(String hashtag) throws InterruptedException {
        //Set up
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("headless");
        WebDriver driver=new ChromeDriver(options);
        TimeUnit.SECONDS.sleep(1);
        driver.get("https://www.instagram.com/explore/tags/instagram");
        WebElement input = driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[2]/input"));
        input.sendKeys("#"+hashtag);

        //Get the values of the result
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/div[2]/div[2]/div/a")));
        List<WebElement> inputResultLinks = driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/div[2]/div[2]/div/a"));

        List<Hashtag> hashtags;
        HashSet<String> viewedTags = new HashSet<>();
        hashtags = updateHashTags(inputResultLinks, driver, viewedTags, 2000);
        System.out.println("FINISHED");
        driver.close();
        return getSortedHashtags(hashtags);
    }

    private List<Hashtag> updateHashTags(List<WebElement> inputResults, WebDriver driver, HashSet<String> viewedTags, int averageLikes) {
        List<Hashtag> result;
        for(WebElement webElement: inputResults)
        {
            if(webElement.findElement(By.xpath("div/div/div[1]/span")).getText().charAt(0) == '#') {
                webElement.click();
                //entry point
                result = bfs(driver, webElement.findElement(By.xpath("div/div/div[1]/span")).getText(), viewedTags, averageLikes);
                return result.isEmpty() ? null : result;
            }
        }
        return null;
    }

    private List<Hashtag> bfs(WebDriver driver, String start, HashSet<String> viewedTags, int averageLikes) {
        List<Hashtag> hashtags = new ArrayList<>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(start);
        String hashtag;
        while(queue.size() != 0 || hashtags.size() == hashtagSizeList /* This is shortened to search faster */)
        {
            hashtag = queue.poll();
            System.out.print(hashtag + ": ");
            driver.get(getLinkFromHashTag(hashtag));
            //should this hashtag be used?
            Hashtag newHashtag = getHashTag(averageLikes, hashtag, driver);
            if(newHashtag != null) hashtags.add(newHashtag);
            if(hashtags.size() == hashtagSizeList) {
                break;
            }
            viewedTags.add(hashtag);
            //get a list of related hashtags that are not in
            Iterator<String> i = getNeighbors(driver).iterator();
            while(i.hasNext())
            {
                String link = i.next();
                if(!viewedTags.contains(link))
                {
                    System.out.print(link);
                    queue.add(getHashTagFromLink(link));
                }
                System.out.println("");
            }
        }
        return hashtags;
    }

    private String getHashTagFromLink(String link) {
        return "#"+link.substring(39, link.length()-1);
    }

    private String getLinkFromHashTag(String hashtag) {
        return "https://www.instagram.com/explore/tags/" + hashtag.substring(1) + "/";
    }

    private Hashtag getHashTag(int averageLikes, String hashtag, WebDriver driver) {
        double average = getTopNineAverage(driver, hashtag);
        //need to judge if new hashtag is in range of averageLikes
        double upperLimit = average + (average * percentangeThreshold);
        double lowerLimit = average - (average * percentangeThreshold);
        System.out.println("Average: " + averageLikes + "  -  UpperLimit: " + upperLimit + "  -  LowerLimit:" + lowerLimit);
        if(averageLikes <= upperLimit && averageLikes >= lowerLimit)
        {
            System.out.println("ADDING: " + hashtag);
            return new Hashtag(hashtag, ""+ driver.findElement(By.xpath("/html/body/div[1]/section/main/header/div[2]/div[1]/div[2]/span/span")).getText());
        }
        return null;
    }

    private List<String> getNeighbors(WebDriver page) {
        WebDriverWait wait = new WebDriverWait(page, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[2]/span/span[2]/div")));
        List<WebElement> tags = page.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[2]/span/span[2]/div"));
        List<String> neighborTagLinks = new ArrayList<>();
        for(WebElement tag : tags)
        {
            neighborTagLinks.add(tag.findElement(By.xpath("a")).getAttribute("href"));
        }
        return neighborTagLinks;
    }

    public int getTopNineAverage(WebDriver driver, String hashtag)
    {
        WebDriverWait wait = new WebDriverWait(driver, 2000);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[1]/div[1]/h1"), hashtag));
        //Get top nine
        List<WebElement> topNine = driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[1]/div"));
        topNine.addAll(driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[2]/div")));
        topNine.addAll(driver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[3]/div")));
        //open browser with desired URL
        int sum = 0;
        int count = 0;
        List<Future<Integer>> futureLikes = new ArrayList<Future<Integer>>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(9);
        for(WebElement picture: topNine)
        {
            Callable<Integer> callable = new PostCallable(picture);
            Future<Integer> future = executor.submit(callable);
            futureLikes.add(future);
        }
        for(Future<Integer> fut : futureLikes)
        {
            try {
                int likes = fut.get();
                if(likes != -1)
                {
                    sum += likes;
                    count++;
                }
            } catch (InterruptedException | ExecutionException e)
            {

            }
        }
        return sum/count;
    }

    public List<Hashtag> getSortedHashtags(List<Hashtag> hashtags)
    {
        Collections.sort(hashtags);
        return hashtags;
    }

    class PostCallable implements Callable<Integer> {

        private WebElement picture;

        public PostCallable(WebElement picture)
        {
            this.picture = picture;
        }


        public int getLikes(WebElement picture)
        {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
            options.addArguments("--disable-features=VizDisplayCompositor");

            WebDriver postDriver=new ChromeDriver(options);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            postDriver.get(picture.findElement(By.xpath("a")).getAttribute("href"));
            WebDriverWait wait = new WebDriverWait(postDriver, 10);
            WebElement likesElement = null;
            int counter = 0;
            if(postDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[2]/div/div/button/span")).isEmpty()) {
                if(postDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[2]/div/div/span/span")).isEmpty())
                {
                    return -1;
                }
                else
                {
                    likesElement = postDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[2]/div/div/button/span")).get(0);
                }
            }
            else
            {
                likesElement = postDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[2]/div/div/button/span")).get(0);
            }
            int retInteger = Integer.parseInt(likesElement.getText().replaceAll(",", ""));
            postDriver.close();
            return retInteger;
        }

        @Override
        public Integer call() throws Exception {
            return getLikes(picture);
        }
    }
}
