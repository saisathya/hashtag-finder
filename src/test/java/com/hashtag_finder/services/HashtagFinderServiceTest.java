package com.hashtag_finder.services;

import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.Hashtag;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.repositories.HashtagFinderRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class HashtagFinderServiceTest {

    @Mock
    private HashtagFinderRepo hashtagFinderRepo;

    @Mock
    private InstagramHashtagCrawler instagramHashtagCrawler;

    @InjectMocks
    private InstagramHashtagFinderServiceImpl hashtagFinderService;

    private MockMvc mockMvc;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hashtagFinderService).build();
    }

    @Test
    public void findHashtagsBySearchWordWithoutRunningCrawlerTest() throws InterruptedException {
        List<HashtagFinder> hashtagFindersTest = new ArrayList<>();
        hashtagFindersTest.add(new HashtagFinder());
        hashtagFindersTest.add(new HashtagFinder());

        GetHashtagInput anObject = new GetHashtagInput();
        anObject.setSearchWord("test");

        when(hashtagFinderRepo.findBySearchWord(anyString())).thenReturn(hashtagFindersTest);

        List<HashtagFinder> result = hashtagFinderService.findHashtagsBySearchWord(anObject);

        Assert.assertEquals(2, result.size());

        verify(instagramHashtagCrawler, never()).runGetHashtagsCrawler(anyString());
    }

    @Test
    public void findHashtagsBySearchWordRunningCrawlerTest() throws InterruptedException {
        List<HashtagFinder> hashtagFindersTest = new ArrayList<>();
        GetHashtagInput anObject = new GetHashtagInput();
        anObject.setSearchWord("test");

        List<Hashtag> resultFromCallingRunScrawler = new ArrayList<>();
        resultFromCallingRunScrawler.add(new Hashtag());


        when(hashtagFinderRepo.findBySearchWord(anyString())).thenReturn(hashtagFindersTest);
        when(instagramHashtagCrawler.runGetHashtagsCrawler(anyString())).thenReturn(resultFromCallingRunScrawler);

        List<HashtagFinder> result = hashtagFinderService.findHashtagsBySearchWord(anObject);

        Assert.assertEquals(1, result.size());
        verify(instagramHashtagCrawler, times(1)).runGetHashtagsCrawler(anObject.getSearchWord());

    }

}
