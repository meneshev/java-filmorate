package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getFilmById() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        ResponseEntity<Film> getResponse = restTemplate.getForEntity(
                "/films/1",
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }

    @Test
    void findAll() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        ResponseEntity<Film[]> getResponse = restTemplate.getForEntity(
                "/films",
                Film[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }

    @Test
    void create() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));
    }

    @Test
    void update() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        HttpEntity<String> putRequest = new HttpEntity<>("{\"id\":1,\"name\":\"name_new\"," +
                "\"description\":\"description_new\"," +
                "\"releaseDate\":\"2003-01-01\"," +
                "\"duration\":180,\"mpa\":{\"id\":1}}",
                httpHeaders);

        ResponseEntity<Film> putResponse = restTemplate.exchange("/films", HttpMethod.PUT, putRequest, Film.class);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        ResponseEntity<Film[]> getResponse = restTemplate.getForEntity(
                "/films",
                Film[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));

        assertTrue(getResponse.getBody()[0].getId().equals(1L) &&
                getResponse.getBody()[0].getName().equals("name_new") &&
                getResponse.getBody()[0].getDescription().equals("description_new") &&
                getResponse.getBody()[0].getReleaseDate().equals(LocalDate.of(2003, 1, 1)) &&
                getResponse.getBody()[0].getDuration().toMinutes() == 180);
    }

    @Test
    void testValidations() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //название не может быть пустым
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":null,\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());

        //максимальная длина описания — 200 символов
        //201 symbol
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;1dK8S$RwrhUuS;jjDHGVuN}xU%kWpw=E=[{)EvzD$1y64D63P/#XUxY2)#m_%*fy-pS" +
                "TCFh0C%[ug}Bpzhu@)SW}}%v*K+V=E8LyLD40y1Jx+1b5zHjwz$}AY!/q=YvVffd-[:Kbkb.Y&6GM,Fu3mcq%n8.Y[HbK%f36gBT." +
                "QVM,#)YXzpFJRU4+Ae3\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());

        //200 symbols
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;1dK8S$RwrhUuS;jjDHGVuN}xU%kWpw=E=[{)EvzD$1y64D63P/#XUxY2)#m_%*fy-pS" +
                "TCFh0C%[ug}Bpzhu@)SW}}%v*K+V=E8LyLD40y1Jx+1b5zHjwz$}AY!/q=YvVffd-[:Kbkb.Y&6GM,Fu3mcq%n8.Y[HbK%f36gBT." +
                "QVM,#)YXzpFJRU4+Ae\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        //199 symbols
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;1dK8S$RwrhUuS;jjDHGVuN}xU%kWpw=E=[{)EvzD$1y64D63P/#XUxY2)#m_%*fy-pS" +
                "TCFh0C%[ug}Bpzhu@)SW}}%v*K+V=E8LyLD40y1Jx+1b5zHjwz$}AY!/q=YvVffd-[:Kbkb.Y&6GM,Fu3mcq%n8.Y[HbK%f36gBT." +
                "QVM,#)YXzpFJRU4+A\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        //дата релиза — не раньше 28 декабря 1895 года
        //раньше 28 декабря 1895 года
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-27\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());

        //28 декабря 1895 года
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());


        //после 28 декабря 1895 года
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        //продолжительность фильма должна быть положительным числом
        //отрицательное
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":-1,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());

        // ноль
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":0,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());

        // положительное
        postRequest = new HttpEntity<>("{\"name\":\"name\"," +
                "\"description\":\"V?W&,RL8an5]4;\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    void addLikeTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponseUser = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponseUser.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponseUser.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<Film> putResponse = restTemplate.exchange(
                "/films/1/like/1", HttpMethod.PUT, putRequest, Film.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));
    }

    @Test
    void deleteLikeTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponseUser = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponseUser.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponseUser.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<Film> putResponse = restTemplate.exchange(
                "/films/1/like/1", HttpMethod.PUT, putRequest, Film.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> deleteRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<Film> deleteResponse = restTemplate.exchange(
                "/films/1/like/1", HttpMethod.DELETE, deleteRequest, Film.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(deleteResponse.getBody()));
    }

    @Test
    void getPopularFilms() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"name\":\"name\",\"description\":\"description\"," +
                "\"releaseDate\":\"1899-01-01\",\"duration\":120,\"mpa\":{\"id\":1}}", httpHeaders);

        ResponseEntity<Film> postResponse = restTemplate.postForEntity(
                "/films",
                postRequest,
                Film.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        ResponseEntity<Film[]> getResponse = restTemplate.getForEntity(
                "/films/popular",
                Film[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }
}