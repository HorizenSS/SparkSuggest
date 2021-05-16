package com.rapid.engine.engine;

import com.rapid.engine.entity.Suggestion;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Fragmenter {


    public static List<String> getFragments(Suggestion s) {
        List<String> words = new ArrayList<>();
        // "the godfather part iii" ==> "the", "godfather", ...
        String normalized = normalize(s.getSuggestion());
        for (String w : normalized.split("\\s+")) {
            words.add(w);
        }

        List<String> fragments = new ArrayList<>();
        String suffix = "";
        for (int i= words.size() - 1; i >=0; i--) {
            if (!suffix.isEmpty()) {
                suffix = " " + suffix;
            }
            suffix = words.get(i) + suffix;
            fragments.add(suffix);
        }
        return fragments; // "jean-claude van damme" ==> [damme, van damme, jean claude van damme, claude van damme]
    }

    public static String normalize(String dirty) {
        return Normalizer.normalize(dirty, Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]]", "")
                            .toLowerCase()
                            .replace("-", " ");
    }


}
