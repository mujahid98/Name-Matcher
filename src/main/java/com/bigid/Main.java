package com.bigid;

import com.bigid.builder.MatchResultBuilder;
import com.bigid.factory.MatcherFactory;
import com.bigid.matcher.NameMatcher;
import com.bigid.model.MatchLocation;
import com.bigid.task.MatchCommand;
import com.bigid.util.Constants;
import com.bigid.util.FileReaderUtil;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int CHUNK_SIZE = Constants.CHUNK_SIZE;
    private static final List<String> NAMES = Constants.NAMES_TO_MATCH;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose matcher type:");
        System.out.println("1. Simple (case-sensitive)");
        System.out.println("2. Case-insensitive");
        System.out.print("Enter 1 or 2: ");
        String choice = scanner.nextLine();

        String matcherType = switch (choice.trim()) {
            case "1" -> "simple";
            case "2" -> "case-insensitive";
            default -> {
                System.out.println("Invalid input. Defaulting to simple matcher.");
                yield "simple";
            }
        };

        NameMatcher matcher = MatcherFactory.getMatcher(matcherType, NAMES);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        MatchResultBuilder resultBuilder = new MatchResultBuilder();

        System.out.println("\nDownloading and processing the file in chunks...\n");

        try {
            List<Future<Map<String, List<MatchLocation>>>> futures =
                    FileReaderUtil.readFromUrlInChunks(Constants.TEXT_FILE_URL, CHUNK_SIZE,
                            (chunk, lineOffset) -> executor.submit(new MatchCommand(matcher, chunk, lineOffset))
                    );

            // Collect results with exception handling
            for (Future<Map<String, List<MatchLocation>>> future : futures) {
                try {
                    resultBuilder.add(future.get());
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while waiting: " + e.getMessage());
                    Thread.currentThread().interrupt(); // Restore interrupt flag
                } catch (ExecutionException e) {
                    System.err.println("Error while processing a chunk: " + e.getCause());
                    // Optionally skip or log the chunk error
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file or submitting tasks: " + e.getMessage());
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("==== Match Results ====");
        System.out.println(resultBuilder.buildTextReport());
    }
}
