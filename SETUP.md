## Running the app
The repository contains  standard sprint boot application that can be launched using mvn or mvnw:
```
./mvnw clean spring-boot:run
```

Tests available by:

```
./mvnw test
```


## Documentation:
openapi.json spec can be generated using <b>springdoc-openapi</b> maven plugin.
<u>Note:</u> - it requires active application, target location of running service can be configured in pom.xml, plugin configuration section:
```
...
<configuration>
    <apiDocsUrl>http://localhost:8080/api-docs</apiDocsUrl>
    <outputFileName>openapi.json</outputFileName>
    <outputDir>${project.build.directory}</outputDir>
</configuration>
...
```

## API DOC UI
With current configuration swagger-ui is available without authorization (can be disabled in SecurityConfiguration.class)

UI is available via:
```dtd
http://"app host and port"/swagger-ui/index.html#/
```