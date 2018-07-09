# jooq-experiment

1. `./gradlew flywayMigrate` to generate a dummy H2 database at `./db`
2. `./gradlew generateDummyJooqSchemaSource` to generate jOOQ bindings based on previously generated dummy database.
3. `./gradlew test` to run tests.
