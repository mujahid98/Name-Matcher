package com.bigid.task;

import com.bigid.matcher.NameMatcher;
import com.bigid.model.MatchLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MatchCommand implements Callable<Map<String, List<MatchLocation>>> {
    private final List<String> lines;
    private final int offset;
    private final NameMatcher matcher;

    public MatchCommand(NameMatcher matcher, List<String> lines, int offset) {
        this.lines = lines;
        this.offset = offset;
        this.matcher = matcher;
    }

    @Override
    public Map<String, List<MatchLocation>> call() {
        return matcher.match(lines, offset);
    }
}
