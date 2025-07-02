package com.bigid.matcher;

import com.bigid.model.MatchLocation;
import java.util.List;
import java.util.Map;

public interface NameMatcher {
    Map<String, List<MatchLocation>> match(List<String> lines, int lineOffset);
}
