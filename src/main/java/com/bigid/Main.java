package com.bigid;

import com.bigid.builder.MatchResultBuilder;
import com.bigid.config.AppConfig;
import com.bigid.factory.MatcherFactory;
import com.bigid.matcher.NameMatcher;
import com.bigid.model.MatchLocation;
import com.bigid.task.MatchCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int CHUNK_SIZE = 1000;

    private static final List<String> NAMES = List.of(
            "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas",
            "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
            "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
            "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );

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
        List<Future<Map<String, List<MatchLocation>>>> futures = new ArrayList<>();

        System.out.println("\nDownloading and processing the file in chunks...\n");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL("http://norvig.com/big.txt").openStream()))) {

            List<String> chunk = new ArrayList<>();
            int lineOffset = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == CHUNK_SIZE) {
                    futures.add(executor.submit(new MatchCommand(matcher, new ArrayList<>(chunk), lineOffset)));
                    chunk.clear();
                    lineOffset += CHUNK_SIZE;
                }
            }

            if (!chunk.isEmpty()) {
                futures.add(executor.submit(new MatchCommand(matcher, chunk, lineOffset)));
            }

            for (Future<Map<String, List<MatchLocation>>> future : futures) {
                resultBuilder.add(future.get());  // Wait for all matcher tasks
            }
        }

        executor.shutdown();

        System.out.println("==== Match Results ====");
        System.out.println(resultBuilder.buildTextReport());
    }
}
