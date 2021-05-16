package com.rapid.engine.engine;

import com.rapid.engine.entity.Suggestion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutoCompleteFragment implements Comparable<AutoCompleteFragment>{

    Suggestion suggestion;

    String fragment;

    @Override
    public int compareTo(AutoCompleteFragment o) {
        return this.fragment.compareTo(o.fragment);
    }
}
