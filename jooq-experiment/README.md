# jooq-experiment

* `./tool.sh start-db` to start Mysql Docker container. This will wait for Mysql to become ready.
* `./tool.sh migrate-db` to apply migrations to the empty database.
* `./tool.sh generate-jooq` to generate jOOQ code.
* `./gradlew clean test` to run tests.
* `./gradlew clean bootRun` to run the app.
* `./tool.sh stop-db` to stop Mysql Docker container.
* `./tool.sh delete-db-data` to delete Mysql data under `.data`. This will stop the Mysql container if it's running.

## Development workflow

* All development activities require having Mysql Docker container running, so make sure to `./tool.sh start-db` when you start working. Optionally, `./tool.sh stop-db` when you stop working.
* Run `./tool.sh migrate-db` and `./tool.sh generate-jooq` every time you add a DB migration. This will update the DB and re-generate the jOOQ code. 
* Keep jOOQ code under version control, because this allows you to at least `git clone` and try to compile it. (*Questionable: because you can't run the tests and the app itself without Mysql, is there an actual value in being able to compile the code without Mysql?*)  

## Notes

* The default code generation strategy (`org.jooq.codegen.DefaultGeneratorStrategy`) generates the nice names, but the DB names should all be `like_this` (`SCHOOL_STUDENTS`, `school_id`). As an alternative, there's `org.jooq.codegen.KeepNamesGeneratorStrategy`. It keeps the name as they, but it doesn't add any suffixes/prefixes where you'd normally expect them (the table class for `Students` is `Students`, and the record class is `Students` as well).
