package com.hashtag_finder.services;

import com.hashtag_finder.models.Hashtag;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class InstagramHashtagCrawler {

    private static final long SEARCH_TIME = 25000;
    private static final long DELAY = 100;
    final double percentangeThreshold = 0.50;
    final int hashtagSizeList = 1;
    final int bfsThreadNumber = 5;
    private volatile GracefulExecutor executor;
    private final static ConcurrentMap<String, Boolean> seen = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, String> hashtagResults = new ConcurrentHashMap<>();
    private static final int AVERAGE_LIKES = 2000;
    private static final int MAX_RETURNED = 20;

    public List<Hashtag> runGetHashtagsCrawler(String hashtag) throws InterruptedException {
        //Set up
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
        options.addArguments("--disable-features=VizDisplayCompositor");
        //options.addArguments("headless");
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
        hashtags = updateHashTags(inputResultLinks, hashtag);
        driver.quit();
        return getSortedHashtags(hashtags);
    }

    private List<Hashtag> updateHashTags(List<WebElement> inputResults, String hashtag) {
        List<Hashtag> result = new ArrayList<Hashtag>();
        List<Hashtag> padding = new ArrayList<>();
        for(WebElement webElement: inputResults)
        {
            System.out.println(webElement.getTagName());
            if(webElement.findElement(By.xpath("div/div/div[1]/span")).getText().charAt(0) == '#') {
                webElement.click();
                String hashTagStr = webElement.findElement(By.xpath("div/div/div[1]/span")).getText();
                result = bfs(hashTagStr);
                break;
            }
        }
        int i = 0;
        for(Hashtag paddingHashTag : getPadding(hashtag))
        {
            if(i == 10)
            {
                break;
            }
            result.add(paddingHashTag);
            i++;
        }
        return result.isEmpty() ? null : result;
    }

    private List<Hashtag> getPadding(String hashtag)
    {
        WebDriverManager.chromedriver().setup();
        WebDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //maximize window
        driver.manage().window().maximize();
        //open browser with desired URL
        driver.get("https://www.instagram.com/explore/tags/instagram");
        WebElement input = driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[2]/input"));
        input.sendKeys("#"+hashtag);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        driver.quit();
        return hashtags;
    }

    private List<Hashtag> bfs(String start) {
        List<Hashtag> hashtags = new ArrayList<>();
        //start
        bfsStart(start);
        //run time
        try {
            Thread.sleep(SEARCH_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done Searching with: " + hashtagResults.size() + " hits");
        bfsStop();
        Iterator<Map.Entry<String, String>> itr = hashtagResults.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            String key = entry.getKey();
            String value = entry.getValue();
            hashtags.add(new Hashtag(key, value));
        }
        return hashtags;
    }

    private String getHashTagFromLink(String link) {
        return "#"+link.substring(39, link.length()-1);
    }

    private String getLinkFromHashTag(String hashtag) {
        return "https://www.instagram.com/explore/tags/" + hashtag.substring(1) + "/";
    }

    public List<Hashtag> getSortedHashtags(List<Hashtag> hashtags)
    {
        Collections.sort(hashtags);
        return hashtags;
    }

    public synchronized void bfsStop()
    {
        try {
            executor.shutdownNow();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void bfsStart(String start) {
        executor = new GracefulExecutor(Executors.newFixedThreadPool(bfsThreadNumber));
        try {
            Thread.sleep(100);
            executor.execute(new FindHashTagValueThread(start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class FindHashTagValueThread implements Runnable{

        private String hashtag;
        WebDriver hashTagPageDriver;
        private boolean inappropriate = false;

        public FindHashTagValueThread(String hashtag){
            this.hashtag = hashtag;
        }

        private boolean alreadySeen()
        {
            return seen.putIfAbsent(hashtag, true) != null;
        }

        private void submitCrawlTask(String link)
        {
            try {
                Thread.sleep(DELAY);
                System.out.println("Starting " + link);
                executor.execute(new FindHashTagValueThread(getHashTagFromLink(link)));
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }

        public int getTopNineAverage(String hashtag)
        {

            WebDriverWait wait = new WebDriverWait(hashTagPageDriver, 2000);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[1]/div[1]/h1"), hashtag));
            //Handle inappropriate pop up
            if(hashTagPageDriver.getPageSource().contains("Can we help?"))
            {
                inappropriate = true;
                return 0;
            }
            //Get top nine
            List<WebElement> topNine = hashTagPageDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[1]/div"));
            topNine.addAll(hashTagPageDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[2]/div")));
            topNine.addAll(hashTagPageDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[1]/div/div/div[3]/div")));
            //open browser with desired URL
            int sum = 0;
            int count = 0;
            List<Future<Integer>> futureLikes = new ArrayList<Future<Integer>>();
            GracefulExecutor topNineExecutor = new GracefulExecutor(Executors.newFixedThreadPool(bfsThreadNumber));
            for(WebElement picture: topNine)
            {
                Callable<Integer> callable = new PostCallable(picture);
                Future<Integer> future = topNineExecutor.submit(callable);
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
                    e.printStackTrace();
                }
            }
            return count != 0 ? sum/count : 0;
        }

        private boolean isInHashTagRange(String hashtag) {
            double average = getTopNineAverage(hashtag);
            //need to judge if new hashtag is in range of averageLikes
            double upperLimit = average + (average * percentangeThreshold);
            double lowerLimit = average - (average * percentangeThreshold);
            System.out.println("Your Average: " + AVERAGE_LIKES + "  - HashTagAverage: " + "  -  UpperLimit: " + upperLimit + "  -  LowerLimit:" + lowerLimit);
            if(AVERAGE_LIKES <= upperLimit && AVERAGE_LIKES >= lowerLimit)
            {
                return true;
            }
            return true;
        }

        private List<String> getNeighbors() {
            WebDriverWait wait = new WebDriverWait(hashTagPageDriver, 10);
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[2]/span/span[2]/div")));
            List<WebElement> tags = hashTagPageDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/main/header/div[2]/div[2]/span/span[2]/div"));
            List<String> neighborTagLinks = new ArrayList<>();
            for(WebElement tag : tags)
            {
                neighborTagLinks.add(tag.findElement(By.xpath("a")).getAttribute("href"));
            }
            return neighborTagLinks;
        }

        @Override
        public void run() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            hashTagPageDriver =new ChromeDriver(options);
            if(alreadySeen())
            {
                hashTagPageDriver.quit();
                return;
            }
            //Get the link and go there in this driver
            hashTagPageDriver.get(getLinkFromHashTag(hashtag));
            //should this hashtag be used? if not it will be null
            if(isInHashTagRange(hashtag)) {
                hashtagResults.putIfAbsent(hashtag, hashTagPageDriver.findElement(By.xpath("/html/body/div[1]/section/main/header/div[2]/div[1]/div[2]/span/span")).getText());
                System.out.println("SIZE is now growing: " + hashtagResults.size());
            }
            if(inappropriate)
            {
                hashTagPageDriver.quit();
                return;
            }
            //get a list of related hashtags that are not in
            for(String link : getNeighbors())
            {
                if(Thread.currentThread().isInterrupted())
                {
                    hashTagPageDriver.quit();
                    return;
                }
                submitCrawlTask(link);
            }
            hashTagPageDriver.quit();
        }
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
                if(postDriver.findElements(By.xpath("//*[@id=\"react-root\"]/section/mainnewFixedThreadPool/div/div/article/div[2]/section[2]/div/div/span/span")).isEmpty())
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
            postDriver.quit();
            return retInteger;
        }

        @Override
        public Integer call() throws Exception {
            return getLikes(picture);
        }
    }

    class GracefulExecutor extends AbstractExecutorService {
        private final ExecutorService executorService;
        private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());

        GracefulExecutor(ExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void shutdown() {
            this.executorService.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            return this.executorService.shutdownNow();
        }

        @Override
        public boolean isShutdown() {
            return this.executorService.isShutdown();
        }

        @Override
        public boolean isTerminated() {
            return this.executorService.isTerminated();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return this.executorService.awaitTermination(timeout, unit);
        }

        @Override
        public void execute(Runnable command) {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    }
                    finally {
                        if(isShutdown() && Thread.currentThread().isInterrupted())
                        {
                            tasksCancelledAtShutdown.add(command);
                        }
                    }
                }
            });
        }
    }
}
