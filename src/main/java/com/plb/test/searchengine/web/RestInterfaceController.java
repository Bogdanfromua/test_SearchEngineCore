package com.plb.test.searchengine.web;

import com.plb.test.searchengine.exceptions.DocumentNotFoundException;
import com.plb.test.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;


@RestController
public class RestInterfaceController {

    private final SearchService searchService;

    @Autowired
    public RestInterfaceController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(path="documents/{key}", method = RequestMethod.PUT, consumes = TEXT_PLAIN_VALUE)
    public void addDocument(@PathVariable String key, @RequestBody String value) {
        searchService.addDocument(key, value);
    }

    @RequestMapping(value = "documents/{key}", method = RequestMethod.GET, produces = TEXT_PLAIN_VALUE)
    public String getDocument(@PathVariable String key) {
        String value = searchService.getDocument(key);
        if (null == value) {
            throw new DocumentNotFoundException();
        }
        return value;
    }

    @RequestMapping(value = "findDocuments", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Collection<String> findDocuments(String query) {
        return searchService.findDocument(query);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDocumentNotFoundException(DocumentNotFoundException e) {
        return new ResponseEntity<>("DocumentNotFoundException", NOT_FOUND);
    }
}
