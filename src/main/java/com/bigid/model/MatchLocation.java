package com.bigid.model;

public record MatchLocation(int lineOffset, int charOffset) {
    @Override
    public String toString() {
        return "[lineOffset=%d, charOffset=%d]".formatted(lineOffset, charOffset);
    }
}
