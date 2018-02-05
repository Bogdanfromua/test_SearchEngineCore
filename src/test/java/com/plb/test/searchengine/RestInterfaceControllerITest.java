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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
public class RestInterfaceControllerITest {
    @Autowired
    private MockMvc mvc;

    private static MockHttpServletRequestBuilder getDocumentRequest(String key) {
        return get("/documents/" + key)
                .accept(MediaType.TEXT_PLAIN);
    }

    private static MockHttpServletRequestBuilder putDocumentRequest(String key, String value) {
        return put("/documents/"+key)
                .contentType(MediaType.TEXT_PLAIN)
                .content(value);
    }

    private static MockHttpServletRequestBuilder findDocumentRequest(String query) {
        return get("/findDocuments")
                .param("query", query)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    @DirtiesContext
    public void addDocument() throws Exception {
        String value = "testValue";
        String key = "testKey";

        mvc.perform(putDocumentRequest(key, value))
                .andExpect(status().isOk());
    }


    @Test
    public void getDocument_nonExist() throws Exception {
        mvc.perform(getDocumentRequest("test"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    public void addAndGetDocument() throws Exception {
        String value = "testValue";
        String key = "test";

        mvc.perform(putDocumentRequest(key, value))
                .andExpect(status().isOk());

        mvc.perform(getDocumentRequest(key))
                .andExpect(status().isOk())
                .andExpect(content().string(value));
    }

    @Test
    public void findDocuments_emptySet() throws Exception {
        String query = "search query";
        mvc.perform(findDocumentRequest(query))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    public void addAndFindDocuments() throws Exception {
        String query = "test";
        Map<String, String> documents = new HashMap<>();
        documents.put("k1", "test aaa");
        documents.put("k2", "test bbb");

        // populate docs
        for (Map.Entry<String, String> entry : documents.entrySet()) {
            mvc.perform(putDocumentRequest(entry.getKey(), entry.getValue()))
                    .andExpect(status().isOk());
        }

        // test
        String expectedKeysJson = new ObjectMapper().writeValueAsString(documents.keySet());
        mvc.perform(findDocumentRequest(query))
                .andExpect(status().isOk())
                .andDo(result -> assertEquals(expectedKeysJson, result.getResponse().getContentAsString(), false));
    }
}