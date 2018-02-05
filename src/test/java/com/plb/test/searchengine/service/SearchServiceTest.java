package com.plb.test.searchengine.service;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class SearchServiceTest {


    private SearchService searchService;

    @Before
    public void testInit() {
        searchService = new SearchService();
    }

    @Test
    public void getDocument_nonExist() throws Exception {
        assertNull(searchService.getDocument("ddd"));
    }

    @Test
    public void addAndGetDocument() throws Exception {
        String value = "testValue";
        String key = "test";

        searchService.addDocument(key, value);
        assertEquals(value, searchService.getDocument(key));
    }

    @Test
    public void findDocuments_emptySet() throws Exception {
        assertTrue(searchService.findDocument("some term").isEmpty());
    }
}