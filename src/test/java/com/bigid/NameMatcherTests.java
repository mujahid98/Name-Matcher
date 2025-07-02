package com.bigid;

import com.bigid.matcher.CaseInsensitiveMatcher;
import com.bigid.matcher.NameMatcher;
import com.bigid.matcher.SimpleNameMatcher;
import com.bigid.matcher.CaseInsensitiveMatcher;
import com.bigid.model.MatchLocation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class NameMatcherTests {

    private List<String> sampleNames;

    @BeforeEach
    void setUp() {
        sampleNames = List.of("Michael", "James", "John", "Alice", "Bob");
    }

    @Test
    void testSimpleNameMatcher_singleMatch() {
        NameMatcher matcher = new SimpleNameMatcher(sampleNames);
        String line = "Michael is on the list.";
        Map<String, List<MatchLocation>> result = matcher.match(List.of(line), 0);

        assertNotNull(result);
        assertTrue(result.containsKey("Michael"));
        assertEquals(1, result.get("Michael").size());
        assertEquals(0, result.get("Michael").get(0).lineOffset());
    }

    @Test
    void testSimpleNameMatcher_noMatch() {
        NameMatcher matcher = new SimpleNameMatcher(sampleNames);
        String line = "No known name is present.";
        Map<String, List<MatchLocation>> result = matcher.match(List.of(line), 1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCaseInsensitiveMatcher_multipleMatches() {
        NameMatcher matcher = new CaseInsensitiveMatcher(sampleNames);
        String line = "michael, JAMES and john are here.";
        Map<String, List<MatchLocation>> result = matcher.match(List.of(line), 2);

        assertNotNull(result);
        assertTrue(result.containsKey("michael"));
        assertTrue(result.containsKey("james"));
        assertTrue(result.containsKey("john"));

        assertEquals(1, result.get("michael").size());
        assertEquals(1, result.get("james").size());
        assertEquals(1, result.get("john").size());

        assertEquals(2, result.get("michael").get(0).lineOffset());
    }

    @Test
    void testCaseInsensitiveMatcher_mixedCasing() {
        NameMatcher matcher = new CaseInsensitiveMatcher(sampleNames);
        String line = "BoB went with aLiCe to meet john.";
        Map<String, List<MatchLocation>> result = matcher.match(List.of(line), 3);

        assertNotNull(result);
        assertTrue(result.containsKey("bob"));
        assertTrue(result.containsKey("alice"));
        assertTrue(result.containsKey("john"));

        assertEquals(1, result.get("bob").size());
        assertEquals(1, result.get("alice").size());
        assertEquals(1, result.get("john").size());

        assertEquals(3, result.get("alice").get(0).lineOffset());
    }

    @Test
    void testEmptyInputLineList() {
        NameMatcher matcher = new SimpleNameMatcher(sampleNames);
        Map<String, List<MatchLocation>> result = matcher.match(List.of(), 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyNameList() {
        NameMatcher matcher = new SimpleNameMatcher(List.of());
        String line = "Michael is present.";
        Map<String, List<MatchLocation>> result = matcher.match(List.of(line), 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

