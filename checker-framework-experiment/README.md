# checker-framework-experiment

A [Checker Framework](https://checkerframework.org/) hello world.

* run `./gradlew clean formatting-demo:build` to see how it validates formatting:
  ```
  /home/loki2302/checker-framework-experiment/formatting-demo/src/main/java/io/agibalov/FormattingDemo.java:5: error: [argument.type.incompatible] incompatible types in argument.
          System.out.printf("%d", "hello");
                                  ^
    found   : java.lang.String
    required: INT conversion category (one of: byte, short, int, long, BigInteger)
  1 error
  ```

* run `./gradlew clean nullness-demo:build` to see how it validates nullness:
  ```
  /home/loki2302/checker-framework-experiment/nullness-demo/src/main/java/io/agibalov/NullnessDemo.java:8: error: [assignment.type.incompatible] incompatible types in assignment.
          @NonNull String s = getString();
                                       ^
    found   : @Initialized @Nullable String
    required: @UnknownInitialization @NonNull String
  1 error
  ```
