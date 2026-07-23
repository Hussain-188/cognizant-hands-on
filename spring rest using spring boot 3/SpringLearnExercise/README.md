# Spring Learn Exercise

This is a minimal Spring Boot 3 project generated as an exercise.

Quick steps:

- Build (with proxy flags as requested):

```powershell
cd "spring rest using spring boot 3\SpringLearnExercise"
mvn clean package -Dhttp.proxyHost=proxy.cognizant.com -Dhttp.proxyPort=6050 -Dhttps.proxyHost=proxy.cognizant.com -Dhttps.proxyPort=6050 -Dhttp.proxyUser=123456
```

- Import in Eclipse: File > Import > Maven > Existing Maven Projects > select the folder above > Finish

- Run application: Run `SpringLearnApplication` (main method). Logs printed via SLF4J will verify main() execution.

Files to review:

- `src/main/java` — application code including `SpringLearnApplication` and a sample `HelloController`.
- `src/main/resources` — `application.properties` for configuration.
- `src/test/java` — sample test `SpringLearnApplicationTests`.
- `pom.xml` — Maven build and dependencies.
