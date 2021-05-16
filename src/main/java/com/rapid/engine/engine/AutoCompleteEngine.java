package com.rapid.engine.engine;

import com.rapid.engine.entity.Suggestion;

import java.util.*;

public class AutoCompleteEngine {

    TreeSet<AutoCompleteFragment> tree = new TreeSet<>();

    private Comparator<Suggestion> SCORE_COMPARATOR = new Comparator<Suggestion>() {
        @Override
        public int compare(Suggestion o1, Suggestion o2) {
            double r = o1.getScore() - o2.getScore();
            if (r > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    };


    public void add(Suggestion s) {
        List<String> fragments = Fragmenter.getFragments(s);
        List<AutoCompleteFragment> afragments = new ArrayList<>();
        for (String f : fragments) {
            afragments.add(new AutoCompleteFragment(s, f));
        }
        tree.addAll(afragments);
    }

    public Set<Suggestion> autocomplete (String query, int limit) {
        if (query.isEmpty()) {
            return new HashSet();
        }

        String normalized = Fragmenter.normalize(query); // matri
        TreeSet<Suggestion> results = new TreeSet(SCORE_COMPARATOR);

        AutoCompleteFragment queryWrapper = new AutoCompleteFragment(null, normalized);
        String lastCharDropped = normalized.substring(0, normalized.length()-1); // matr
        String withSubsequentLastChar = lastCharDropped + (char) (normalized.charAt(normalized.length()-1) + 1); // matrj
        AutoCompleteFragment endWrapper = new AutoCompleteFragment(null, withSubsequentLastChar);

        SortedSet<AutoCompleteFragment> subset = tree.subSet(queryWrapper, endWrapper);
        for (AutoCompleteFragment fragment : subset) {
            if (results.size() < limit) {
                results.add(fragment.getSuggestion());
            } else if (SCORE_COMPARATOR.compare(fragment.getSuggestion(), results.last()) < 0) {
                results.remove(results.last());
                results.add(fragment.getSuggestion());
            }
        }
        return results;
    }


}
