# TV Middleware

API middleware en Spring Boot que se apoya en [TV Maze](https://www.tvmaze.com/api) para buscar shows, consultar su detalle y, más adelante, guardar comentarios.

## Punto 0 — Base del proyecto

Se armó el esqueleto de la aplicación para poder ir sumando los endpoints del examen sin rehacer la estructura en cada avance.

- Spring Boot 3.4, Java 21 y Maven.
- Capas previstas: controlador, servicio, cliente HTTP, modelo, DTO y repositorio.
- Manejo centralizado de errores con `@RestControllerAdvice`. Las respuestas de error usan el mismo cuerpo (`timestamp`, `status`, `error`, `message`, `path`).
- Hibernate Validator queda disponible desde `spring-boot-starter-validation`.
- Pruebas unitarias del advice con JUnit 5 y MockMvc.

Los endpoints de search, show y comments se agregan en los siguientes puntos.

## Cómo correrlo

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-21"
mvn spring-boot:run
```

La aplicación queda en `http://localhost:8080`.

## Cómo probar

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-21"
mvn test
```
