package com.plb.test.searchengine;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestInterfaceITest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    public void addDocument() throws Exception {
        mvc.perform(
                put("/documents/test")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("testValue"))
                .andExpect(status().isOk());
    }


    @Test
    public void getDocument_nonExist() throws Exception {
        mvc.perform(
                get("/documents/test")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }


    @Test
    @DirtiesContext
    public void addAndGetDocument() throws Exception {
        String value = "testValue";
        String key = "test";

        mvc.perform(
                put("/documents/" + key)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(value))
                .andExpect(status().isOk());

        mvc.perform(
                get("/documents/" + key)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(value));

    }

    @Test
    public void findDocuments_emptySet() throws Exception {
        mvc.perform(
                get("/findDocuments")
                        .param("query", "search query")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    public void addAndFindDocuments() throws Exception {
        String query = "test";

        Map<String, String> matchingDocuments = new HashMap<>();
        matchingDocuments.put("k1", "test aaa");
        matchingDocuments.put("k2", "test bbb");

        Map<String, String> notMatchingDocuments = new HashMap<>();
        notMatchingDocuments.put("k3", "tes aaa");
        notMatchingDocuments.put("k4", "tes bbb");

        // populate docs
        for (Map.Entry<String, String> entry : matchingDocuments.entrySet()) {
            mvc.perform(
                    put("/documents/" + entry.getKey())
                            .contentType(MediaType.TEXT_PLAIN)
                            .content(entry.getValue()))
                    .andExpect(status().isOk());
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        for (Map.Entry<String, String> entry : notMatchingDocuments.entrySet()) {
            mvc.perform(
                    put("/documents/" + entry.getKey())
                            .contentType(MediaType.TEXT_PLAIN)
                            .content(entry.getValue()))
                    .andExpect(status().isOk());
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        // test
        String expectedKeysJson = new ObjectMapper().writeValueAsString(matchingDocuments.keySet());
        mvc.perform(
                get("/findDocuments")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> assertEquals(expectedKeysJson, result.getResponse().getContentAsString(), false));
    }
}