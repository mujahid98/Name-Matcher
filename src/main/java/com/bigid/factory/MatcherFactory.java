package com.bigid.factory;

import com.bigid.matcher.CaseInsensitiveMatcher;
import com.bigid.matcher.NameMatcher;
import com.bigid.matcher.SimpleNameMatcher;

import java.util.List;

public class MatcherFactory {
    public static NameMatcher getMatcher(String type, List<String> names) {
        return switch (type) {
            case "case-insensitive" -> new CaseInsensitiveMatcher(names);
            case "simple" -> new SimpleNameMatcher(names);
            default -> throw new IllegalArgumentException("Unknown matcher type: " + type);
        };
    }
}
