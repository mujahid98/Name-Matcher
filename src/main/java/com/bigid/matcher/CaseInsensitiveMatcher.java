package com.bigid.matcher;

import com.bigid.model.MatchLocation;
import java.util.*;

public class CaseInsensitiveMatcher implements NameMatcher {
    private final Set<String> names;

    public CaseInsensitiveMatcher(List<String> names) {
        this.names = new HashSet<>();
        for (String name : names) this.names.add(name.toLowerCase());
    }

    @Override
    public Map<String, List<MatchLocation>> match(List<String> lines, int baseLineOffset) {
        Map<String, List<MatchLocation>> result = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String lowerLine = lines.get(i).toLowerCase();
            for (String name : names) {
                int index = lowerLine.indexOf(name);
                while (index >= 0) {
                    result.computeIfAbsent(name, k -> new ArrayList<>())
                            .add(new MatchLocation(baseLineOffset + i, index));
                    index = lowerLine.indexOf(name, index + 1);
                }
            }
        }
        return result;
    }
}
