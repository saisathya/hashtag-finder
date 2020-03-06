package com.hashtag_finder.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;


import com.hashtag_finder.models.GetHashtagInput;
import com.hashtag_finder.models.HashtagFinder;
import com.hashtag_finder.services.HashtagFinderService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class InstagramHashtagFinderControllerTest {
    @Mock
    private HashtagFinderService hashtagFinderService;

    @InjectMocks
    private InstagramHashtagFinderController instagram_hashtagFinderController;

    private MockMvc mockMvc;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(instagram_hashtagFinderController).build();
    }

    @Test
    public void getHashtagsTest() throws Exception{

        //Create Json input to attach with post request
        GetHashtagInput anObject = new GetHashtagInput();
        anObject.setSearchWord(new ArrayList<String>(Arrays.asList("Test")));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(anObject);

        List<HashtagFinder> hashtagFindersTest = new ArrayList<>();
        hashtagFindersTest.add(new HashtagFinder());
        hashtagFindersTest.add(new HashtagFinder());

        when(hashtagFinderService.findHashtagsBySearchWords(any(GetHashtagInput.class))).thenReturn(hashtagFindersTest);

        mockMvc.perform(post("/hashtagFinder/instagram/getHashtags").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(hashtagFinderService, times(1)).findHashtagsBySearchWords(any(GetHashtagInput.class));
    }

    @Test
    public void getHashtagsBadRequestTest() throws Exception {
        String badRequestBody = "{\n" +
                "  \"badRequest\" : \"test\"\n" +
                "}";
        mockMvc.perform(post("/hashtagFinder/instagram/getHashtags").contentType(MediaType.APPLICATION_JSON)
                .content(badRequestBody))
                .andExpect(status().isBadRequest());
        verify(hashtagFinderService, never()).findHashtagsBySearchWords(any());
    }

}
