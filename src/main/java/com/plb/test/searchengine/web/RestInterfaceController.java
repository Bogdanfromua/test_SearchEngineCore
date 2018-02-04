package com.plb.test.searchengine.web;

import com.plb.test.searchengine.exceptions.DocumentNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;


@RestController
public class RestInterfaceController {
    //TODO
    private Map<String, String> documentsTp = new HashMap<>();

    @RequestMapping(path="documents/{key}", method = RequestMethod.PUT, consumes = TEXT_PLAIN_VALUE)
    public void addDocument(@PathVariable String key, @RequestBody String value) {
        //TODO
        documentsTp.put(key, value);
    }

    @RequestMapping(value = "documents/{key}", method = RequestMethod.GET, produces = TEXT_PLAIN_VALUE)
    public String getDocument(@PathVariable String key) {
        //TODO
        if (!documentsTp.containsKey(key)) {
            throw new DocumentNotFoundException();
        }
        return documentsTp.get(key);
    }

    @RequestMapping(value = "findDocuments", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Collection<String> findDocuments(String query) {
        //TODO
        return documentsTp.keySet();
    }
}
