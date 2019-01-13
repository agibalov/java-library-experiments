# graphql-experiment

`./gradlew clean bootRun` and then go to `http://localhost:8080/graphiql`. Sample query: `{ hello(name: "Andrey") }` results in:

```
{
  "data": {
    "hello": "Hello, Andrey!"
  }
}
```

Also `http://localhost:8080/voyager` for API description.
