package com.rapid.engine.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
public class Suggestion {

    // "the matrix"
    @ToString.Include
    String suggestion;

    // "http://imdb.com/name/nm53535", "/person/keanu-reeves"
    String target;

    // "movie", "person", "tvshow"
    String group;

    double score;


}
