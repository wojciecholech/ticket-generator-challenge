# Bingo Ticket Generator

## Requirements

### Functional Requirements Checklist

- [x] Generate a strip of 6 tickets
- [x] A bingo ticket consists of 9 columns and 3 rows.
- [x] Each ticket row contains five numbers and four blank spaces
- [x] Each ticket column consists of one, two or three numbers and never three blanks.
  - [x] The first column contains numbers from 1 to 9 (only nine),
  - [x] The second column numbers from 10 to 19 (ten), the third, 20 to 29 and so on up until
  - [x] The last column, which contains numbers from 80 to 90 (eleven).
- [x] Numbers in the ticket columns are ordered from top to bottom (ASC).
- [x] There can be **no duplicate** numbers between 1 and 90 **in the strip** (since you generate 6 tickets with 15 numbers each)

### Non-Functional Requirements Checklist
- [x] Generate 10k strip in less than 1 second.

## Sample output
```
---------------------
STRIP
---------------------
[3, -1, 20, -1, 40, -1, 61, -1, 89]
[4, 14, 25, -1, -1, 50, 64, -1, -1]
[9, 15, -1, 34, 49, 57, -1, -1, -1]

[-1, -1, 21, -1, -1, 55, 69, 73, 83]
[-1, 11, 28, 33, -1, -1, -1, 75, 87]
[-1, 17, 29, 39, 41, 58, -1, -1, -1]

[-1, -1, -1, 32, 44, 51, -1, 70, 86]
[5, -1, -1, 36, -1, 54, 66, -1, 88]
[7, 16, -1, -1, 47, 56, 67, -1, -1]

[-1, -1, 22, -1, 43, 52, 60, -1, 84]
[-1, 13, 26, 30, 46, 53, -1, -1, -1]
[-1, -1, -1, 31, 48, 59, 62, 76, -1]

[2, -1, 23, -1, -1, -1, 63, 72, 82]
[-1, 19, 24, -1, 42, -1, 68, 74, -1]
[8, -1, 27, -1, 45, -1, -1, 77, 90]

[1, 10, -1, 35, -1, -1, -1, 71, 80]
[6, 12, -1, 37, -1, -1, -1, 78, 81]
[-1, 18, -1, 38, -1, -1, 65, 79, 85]
```

## Quick Start
The application is based on Java 17 and uses Gradle build system.

To build Bingo Ticket Generator from source, use the following command in the main directory:

    ./gradlew clean build

You can run it like this:

    ./gradlew run

To run performance tests:

    ./gradlew jmh


## Performance tests results
Java Microbenchmark Harness (JMH) was used to test the application's performance in generating Bingo strips. The test results indicate that the application can generate around 50,000 bingo strips in one second on the MacBook Pro with i7 CPU.

```
# Warmup Iteration   1: 39022.089 ops/s
# Warmup Iteration   2: 45783.657 ops/s
# Warmup Iteration   3: 54652.825 ops/s
# Warmup Iteration   4: 55256.991 ops/s
# Warmup Iteration   5: 55167.252 ops/s
Iteration   1: 55677.512 ops/s[1m 2s]
Iteration   2: 55969.362 ops/s[1m 12s]
Iteration   3: 56316.906 ops/s[1m 22s]
Iteration   4: 55690.469 ops/s[1m 32s]
Iteration   5: 54944.118 ops/s[1m 42s]

Benchmark                          Mode  Cnt      Score      Error  Units
StripGeneratorBenchmark.generate  thrpt   25  51759.588 ± 4155.183  ops/s
```