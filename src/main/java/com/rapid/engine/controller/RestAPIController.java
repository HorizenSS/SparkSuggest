package com.rapid.engine.controller;

import com.rapid.engine.controller.serializer.SuggestionSerializer;
import com.rapid.engine.entity.Suggestion;
import com.rapid.engine.service.DataIngestionService;
import com.rapid.engine.service.RapidSuggestService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RestAPIController {

    @Autowired
    DataIngestionService dataIngestionService;

    @Autowired
    RapidSuggestService rapidSuggestService;

    @Timed(value = "rapidsuggest.post.time", description = "Time spent posting new suggestions")
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postSuggestions(@RequestBody List<Suggestion> suggestions) {
        dataIngestionService.submitSuggestions(suggestions);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://www.MyWebsite.com")
    @Timed(value = "rapidsuggest.autocomplete.time", description = "Time spent autocompleting a query")
    @GetMapping(value = "/rapidsuggest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity autocomplete(@RequestParam("q") String query,
                                       @RequestParam(name = "n", defaultValue = "10") int n,
                                        @RequestParam(name = "g", defaultValue = "movies,people") String groups) {
        Set<Suggestion> results = rapidSuggestService.getSuggestions(n, groups, query);
        String json = SuggestionSerializer.serialize(results, query);
        return new ResponseEntity(json, HttpStatus.OK);
    }


}
