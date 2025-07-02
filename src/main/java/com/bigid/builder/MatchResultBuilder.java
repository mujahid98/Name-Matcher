package com.bigid.builder;

import com.bigid.model.MatchLocation;
import java.util.*;

public class MatchResultBuilder {
    private final Map<String, List<MatchLocation>> data = new HashMap<>();

    public MatchResultBuilder add(Map<String, List<MatchLocation>> result) {
        result.forEach((k, v) -> data.computeIfAbsent(k, key -> new ArrayList<>()).addAll(v));
        return this;
    }

    public String buildTextReport() {
        StringBuilder sb = new StringBuilder();
        data.forEach((name, locations) -> sb.append(name).append(" --> ").append(locations).append("\n"));
        return sb.toString();
    }
}
