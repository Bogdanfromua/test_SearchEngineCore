package com.plb.test.searchengine.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SearchService {
    private Map<String, String> documents = new HashMap<>();
    private SetMultimap<String, String> index = HashMultimap.create();


    private HashSet<String> splitToTerms(@NotNull String value) {
        HashSet<String> terms = new HashSet<>(Arrays.asList(value.split("\\s+")));
        terms.remove("");
        return terms;
    }

    private void indexDocument(@NotNull String key, Set<String> terms) {
        terms.forEach(term -> index.put(term, key));
    }

    private Collection<String> searchByIndex(Set<String> terms) {
        List<Set<String>> candidates = new ArrayList<>();
        for (String term : terms) {
            candidates.add(index.get(term));
        }

        if (candidates.isEmpty()) {
            return new HashSet<>();
        }

        candidates.sort(Comparator.comparing(Set::size));

        HashSet<String> intersection = new HashSet<>(candidates.get(0));
        for (int i = 1; i < candidates.size(); i++) {
            Set<String> candidate = candidates.get(i);
            intersection.retainAll(candidate);
            if (intersection.isEmpty()) {
                break;
            }
        }

        return intersection;
    }

    /**
     * @param key - not empty string
     */
    public void addDocument(@NotNull String key, @NotNull String value) {
        if (key.isEmpty()) {
            throw new RuntimeException("invalid key (empty)");
        }

        if (documents.containsKey(key)) {
            throw new RuntimeException("invalid key (already exist)");
        }

        documents.put(key, value);
        Set<String> terms = splitToTerms(value);
        indexDocument(key, terms);
    }

    @Nullable
    public String getDocument(@NotNull String key) {
        return documents.get(key);
    }

    /**
     * @return - document keys
     */
    public Collection<String> findDocument(@NotNull String query) {
        Set<String> terms = splitToTerms(query);

        if (terms.isEmpty()) {
            return documents.keySet();
        }

        return searchByIndex(terms);
    }
}
