package com.rapid.engine.service;

import com.rapid.engine.engine.AutoCompleteEngine;
import com.rapid.engine.entity.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RapidSuggestService {

    @Autowired
    DataIngestionService ingestionService;


    public Set<Suggestion> getSuggestions(int n, String groups, String query) {
        Set<Suggestion> suggestions = new HashSet<>();
        Map<String, AutoCompleteEngine> engines = ingestionService.getEngines();
        String[] groupsArray = groups.split(",");
        for (String g : groupsArray) {
            AutoCompleteEngine autoCompleteEngine = engines.get(g);
            if (autoCompleteEngine != null) {
                Set<Suggestion> results = autoCompleteEngine.autocomplete(query, n / groupsArray.length);
                suggestions.addAll(results);
            }
        }
        return suggestions;
    }


}
