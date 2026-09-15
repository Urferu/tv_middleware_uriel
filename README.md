# TV Middleware

API middleware en Spring Boot que se apoya en [TV Maze](https://www.tvmaze.com/api) para buscar shows, consultar su detalle y, más adelante, guardar comentarios.

## Punto 0 — Base del proyecto

Se armó el esqueleto de la aplicación para poder ir sumando los endpoints del examen sin rehacer la estructura en cada avance.

- Spring Boot 3.4, Java 21 y Maven.
- Capas previstas: controlador, servicio, cliente HTTP, modelo, DTO y repositorio.
- Manejo centralizado de errores con `@RestControllerAdvice`. Las respuestas de error usan el mismo cuerpo (`timestamp`, `status`, `error`, `message`, `path`).
- Hibernate Validator queda disponible desde `spring-boot-starter-validation`.
- Pruebas unitarias del advice con JUnit 5 y MockMvc.

## Punto A — Búsqueda de shows

Quedó el endpoint `GET /api/shows/search?search_query={texto}`.

Consulta `https://api.tvmaze.com/search/shows?q=` y arma un arreglo con `id`, `name`, `channel`, `summary` y `genres`. El canal sale del `network` cuando existe; si el show es de streaming se usa el `webChannel`.

Si falta `search_query` o TV Maze falla, responde el advice con 400 o 502.

Ejemplo:

```bash
curl "http://localhost:8080/api/shows/search?search_query=girls"
```

## Punto B — Detalle de show

Quedó `GET /api/shows/{showId}`. Pide el show a `https://api.tvmaze.com/shows/{id}` y lo devuelve completo (nombre, géneros, network, imágenes, rating, `_links`, etc.).

Si TV Maze responde 404, el advice regresa 404 con el formato de error del proyecto.

```bash
curl "http://localhost:8080/api/shows/1"
```

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
