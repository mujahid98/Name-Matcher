package com.bigid.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

public class FileReaderUtil {

    public static List<Future<Map<String, List<com.bigid.model.MatchLocation>>>> readFromUrlInChunks(
            String url,
            int chunkSize,
            BiFunction<List<String>, Integer, Future<Map<String, List<com.bigid.model.MatchLocation>>>> taskSubmitter
    ) throws Exception {

        List<Future<Map<String, List<com.bigid.model.MatchLocation>>>> futures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(url).openStream()))) {

            List<String> chunk = new ArrayList<>();
            int lineOffset = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == chunkSize) {
                    futures.add(taskSubmitter.apply(new ArrayList<>(chunk), lineOffset));
                    chunk.clear();
                    lineOffset += chunkSize;
                }
            }

            if (!chunk.isEmpty()) {
                futures.add(taskSubmitter.apply(chunk, lineOffset));
            }
        }

        return futures;
    }
}

