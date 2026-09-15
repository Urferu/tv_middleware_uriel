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

## Punto B — Cache de shows

Antes de llamar a TV Maze, `GET /api/shows/{showId}` busca el id en la colección `shows` de Mongo. Si ya está, se devuelve ese documento. Si no, se consulta la API, se guarda el show y luego se responde.

Así se evita pegarle a TV Maze en cada detalle y se cumple el cache pedido en el examen.

## Punto C — Comentarios

Quedó `POST /api/shows/{showId}/comments` para guardar un comentario y una calificación de 0 a 5 en la colección `comments`, ligados al id del show. Responde el status de la petición.

Hibernate Validator rechaza rating fuera de rango o comentario vacío.

```bash
curl -X POST "http://localhost:8080/api/shows/1/comments" \
  -H "Content-Type: application/json" \
  -d "{\"comment\":\"Muy buena\",\"rating\":5}"
```

## MongoDB Atlas

El examen pide un cluster gratuito sin restricción de IP. Spring Data MongoDB ya está cableado y lee la URI desde `MONGODB_URI` (no se sube al repo).

Pasos para el cluster:

1. Crear cuenta y un cluster **M0** en [MongoDB Atlas](https://www.mongodb.com/atlas).
2. En **Database Access**, crear un usuario con contraseña.
3. En **Network Access**, permitir `0.0.0.0/0` (cualquier IP).
4. En **Connect**, copiar la URI `mongodb+srv://...` y poner la base `tv_middleware`.
5. Exportar la variable o copiar `.env.example` a un `.env` local (este último no se commitea):

```bash
export MONGODB_URI="mongodb+srv://USER:PASSWORD@CLUSTER.mongodb.net/tv_middleware?retryWrites=true&w=majority"
```

Las pruebas unitarias no levantan Mongo: el perfil de test excluye el auto-config para no depender del cluster.

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
