package com.plb.test.searchengine.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SearchServiceTest_find {
    private SearchService searchService;

    @Parameters
    public static Iterable<Object> data() {
        List<Object> testCases = new ArrayList<>();

        {
            TestCase testCase;
            Map<String, String> matchingDocuments = new HashMap<>();
            matchingDocuments.put("k1", "test aaa");
            matchingDocuments.put("k2", "test bbb");
            Map<String, String> notMatchingDocuments = new HashMap<>();
            notMatchingDocuments.put("k3", "tes aaa");
            notMatchingDocuments.put("k4", "tes bbb");
            testCase = new TestCase("test", matchingDocuments, notMatchingDocuments);
            testCases.add(testCase);
        }
        {
            TestCase testCase;
            Map<String, String> matchingDocuments = new HashMap<>();
            matchingDocuments.put("k1", "test1 test2 test4 test3");
            matchingDocuments.put("k2", " test1 test1 test1 ds test2 test4 test3");
            Map<String, String> notMatchingDocuments = new HashMap<>();
            notMatchingDocuments.put("k3", "tes aaa");
            notMatchingDocuments.put("k4", "test1d test2 test4 test3");
            testCase = new TestCase("test1 test2 test3", matchingDocuments, notMatchingDocuments);
            testCases.add(testCase);
        }
        {
            TestCase testCase;
            Map<String, String> matchingDocuments = new HashMap<>();
            matchingDocuments.put("k1", "test aaa");
            matchingDocuments.put("k2", "test bbb");
            matchingDocuments.put("k3", "tes aaa");
            matchingDocuments.put("k4", "tes bbb");
            Map<String, String> notMatchingDocuments = new HashMap<>();
            testCase = new TestCase("", matchingDocuments, notMatchingDocuments);
            testCases.add(testCase);
        }

        return testCases;
    }


    @Parameter
    public TestCase testCase;

    public static class TestCase {
        String query = "test";
        Map<String, String> matchingDocuments = new HashMap<>();
        Map<String, String> notMatchingDocuments = new HashMap<>();
        TestCase(String query, Map<String, String> matchingDocuments, Map<String, String> notMatchingDocuments) {
            this.query = query;
            this.matchingDocuments = matchingDocuments;
            this.notMatchingDocuments = notMatchingDocuments;
        }
    }

    @Before
    public void testInit() {
        searchService = new SearchService();
    }

    @Test
    public void addAndFindDocuments() throws Exception {
        // populate docs
        testCase.matchingDocuments.forEach((key, value) ->
                searchService.addDocument(key, value));
        testCase.notMatchingDocuments.forEach((key, value) ->
                searchService.addDocument(key, value));

        // test
        assertEquals(testCase.matchingDocuments.keySet(), searchService.findDocument(testCase.query));
    }
}