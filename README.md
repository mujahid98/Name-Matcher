
# 🔍 Name Matcher - Java 21

## 📘 Problem Statement

Design and implement a simple Java program to find specific strings in a large text file. The program must include:

1. **Main Module**
   - Reads the large text file in parts (e.g., 1000 lines per part).
   - Sends each part to a matcher running in a separate thread.
   - Once all matchers finish, it invokes the aggregator to combine and print results.

2. **Matcher**
   - Accepts a text string.
   - Searches for matches from a fixed set of 50 common English first names.
   - Returns a map from matched word → its location(s) in the text.

3. **Aggregator**
   - Collects results from all matchers.
   - Aggregates and prints the final result.

### ⚠️ Requirements

- The results should be printed **only after all text chunks are processed**.
- Each matcher must run **concurrently** using threads.

---

## 🧾 Names to Match

The following are the 50 most common English first names to match in the text:

```

James, John, Robert, Michael, William, David, Richard, Charles, Joseph, Thomas,
Christopher, Daniel, Paul, Mark, Donald, George, Kenneth, Steven, Edward, Brian,
Ronald, Anthony, Kevin, Jason, Matthew, Gary, Timothy, Jose, Larry, Jeffrey, Frank,
Scott, Eric, Stephen, Andrew, Raymond, Gregory, Joshua, Jerry, Dennis, Walter,
Patrick, Peter, Harold, Douglas, Henry, Carl, Arthur, Ryan, Roger

```

---

## 💡 Solution Approach

- Read the input file in **chunks of 1000 lines**.
- Spawn a **Platform Thread** per chunk to search for name matches.
- Matchers report each match as:  
```

Name → \[lineOffset, charOffset]

```
- Use an **Aggregator** to merge all partial maps into a final result.
- Finally, print each matched name and its positions.

---

## 🧱 Project Structure

```

Name-Matcher/
├── builder/
│   └── MatchResultBuilder.java         # Aggregates match results
├── factory/
│   └── MatcherFactory.java             # Creates matcher instances
├── matcher/
│   ├── NameMatcher.java                # Interface for matchers
│   ├── SimpleNameMatcher.java          # Case-sensitive matcher
│   └── CaseInsensitiveMatcher.java     # Case-insensitive matcher
├── model/
│   └── MatchLocation.java              # Holds line & character offset
├── task/
│   └── MatchCommand.java               # Runnable for each match task
├── util/
│   ├── Constants.java                  # Common constants including names to match, URL, chunk size
│   └── FileReaderUtil.java             # Utility to read file in chunks from URL
├── Main.java                          # Entry point for the application
└── README.md                          # This file

````

---

## 🚀 How to Run

### ✅ Prerequisites

- Java 21 installed and configured
- Maven 3.9+
- Internet access (for Maven dependencies and `big.txt` download)

---

### 🔨 Build & Test

```bash
mvn clean test
````

---

### ▶️ Run the Program

```bash
   mvn compile exec:java -D exec.mainClass="com.bigid.Main"
```

Follow the CLI prompt to choose the matcher strategy:

* Case Insensitive Matcher
* Simple Name Matcher

The app will then:

1. Download `big.txt` from [http://norvig.com/big.txt](http://norvig.com/big.txt)
2. Split into parts and process in threads
3. Print all name matches and their positions

---

## 🧠 Design Patterns Used

| Component    | Pattern           | Description                                   |
| ------------ | ----------------- | --------------------------------------------- |
| `Main`       | Orchestrator      | Coordinates flow between matcher & aggregator |
| `Matcher`    | Strategy Pattern  | Supports pluggable matcher strategies         |
| `Aggregator` | Utility Singleton | Aggregates results centrally                  |

---

## ⚙️ Concurrency Design

* Uses **Platform Threads** for efficient parallel processing.
* Each thread performs a **CPU-bound** string matching task.
* Uses `ExecutorService` and `CompletableFuture` for managing threads and collecting results.

### 🧵 Why Not Virtual Threads?

* This task is **CPU-bound**.
* Virtual threads are optimized for **I/O-bound** workloads.
* Platform threads offer better performance for our use case.

---

## 📄 Sample Output

```
Timothy --> [[lineOffset=13000, charOffset=19775], [lineOffset=13000, charOffset=42023]]
James --> [[lineOffset=1000, charOffset=1234]]
...
```

---

## ✅ Testing

* JUnit 5 tests for:

    * `CaseInsensitiveMatcher`
    * `SimpleNameMatcher`
    * Aggregation logic

To run:

```bash
mvn test
```

---

## 📚 References

* [http://norvig.com/big.txt](http://norvig.com/big.txt) – Sample input file
* [OpenJDK Java 21](https://openjdk.org/projects/jdk/21/)
* [Java Loom: Virtual Threads](https://openjdk.org/projects/loom/)

---

## 💡 Future Improvements

* Use Virtual Threads for I/O use cases
* Add REST interface to expose name matcher as a service
* Collect metrics for performance insights (execution time, thread count)

---
