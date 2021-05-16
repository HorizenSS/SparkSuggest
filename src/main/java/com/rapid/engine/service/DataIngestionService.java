package com.rapid.engine.service;

import com.rapid.engine.engine.AutoCompleteEngine;
import com.rapid.engine.entity.Suggestion;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class DataIngestionService {


    Map<String, AutoCompleteEngine> engines = new HashMap<>();


    public void submitSuggestions(List<Suggestion> suggestions) {
        for (Suggestion s : suggestions) {
            AutoCompleteEngine engine;
            String group = s.getGroup();
            if (engines.containsKey(group)) {
                engine = engines.get(group);
            } else {
                engine = new AutoCompleteEngine();
                engines.put(group, engine);
            }
            engine.add(s);
        }
    }


}
