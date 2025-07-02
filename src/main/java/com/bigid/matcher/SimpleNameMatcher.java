package com.bigid.matcher;

import com.bigid.model.MatchLocation;
import java.util.*;

public class SimpleNameMatcher implements NameMatcher {
    private final Set<String> names;

    public SimpleNameMatcher(List<String> names) {
        this.names = new HashSet<>(names);
    }

    @Override
    public Map<String, List<MatchLocation>> match(List<String> lines, int baseLineOffset) {
        Map<String, List<MatchLocation>> result = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (String name : names) {
                int index = line.indexOf(name);
                while (index >= 0) {
                    result.computeIfAbsent(name, k -> new ArrayList<>())
                            .add(new MatchLocation(baseLineOffset + i, index));
                    index = line.indexOf(name, index + 1);
                }
            }
        }
        return result;
    }
}
