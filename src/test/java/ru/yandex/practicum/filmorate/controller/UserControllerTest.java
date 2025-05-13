package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getUserById() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        ResponseEntity<User> getResponse = restTemplate.getForEntity(
                "/users/1",
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }

    @Test
    void findAll() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        ResponseEntity<User[]> getResponse = restTemplate.getForEntity(
                "/users",
                User[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }

    @Test
    void create() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));
    }

    @Test
    void update() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        HttpEntity<String> putRequest = new HttpEntity<>("{\"id\":1,\"login\":\"login_new\"," +
                "\"email\":\"email_new@mail.com\"," +
                "\"birthday\":\"2003-01-01\"," +
                "\"name\":\"name_new\"}",
                httpHeaders);

        ResponseEntity<User> putResponse = restTemplate.exchange("/users", HttpMethod.PUT, putRequest, User.class);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        ResponseEntity<User[]> getResponse = restTemplate.getForEntity(
                "/users",
                User[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));

        assertTrue(getResponse.getBody()[0].getId().equals(1L) &&
                getResponse.getBody()[0].getLogin().equals("login_new") &&
                getResponse.getBody()[0].getEmail().equals("email_new@mail.com") &&
                getResponse.getBody()[0].getBirthday().equals(LocalDate.of(2003, 1, 1)) &&
                getResponse.getBody()[0].getName().equals("name_new"));
    }

    @Test
    public void testValidations() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //электронная почта не может быть пустой и должна содержать символ @
        // email: null
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":null," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //email:" "
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\" \"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //email без @
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email.mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //валидный email
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //логин не может быть пустым и содержать пробелы
        // login:null
        postRequest = new HttpEntity<>("{\"login\":null,\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //login:" "
        postRequest = new HttpEntity<>("{\"login\":\" \",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //login с пробелами
        postRequest = new HttpEntity<>("{\"login\":\"log in\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //валидный login
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //имя для отображения может быть пустым — в таком случае будет использован логин
        // name:null
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":null}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals("login", postResponse.getBody().getName());

        //name: указано
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals("name", postResponse.getBody().getName());

        //дата рождения не может быть в будущем
        //birthday is today
        String bd = LocalDate.now().toString();
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"" + bd + "\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //birthday in past
        bd = LocalDate.now().minusDays(1).toString();
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"" + bd + "\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        //birthday in tomorrow
        bd = LocalDate.now().plusDays(1).toString();
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@email.com\"," +
                "\"birthday\":\"" + bd + "\",\"name\":\"name\"}", httpHeaders);
        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));
    }

    @Test
    public void addFriendTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> putResponse = restTemplate.exchange(
                "/users/1/friends/1", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));
    }

    @Test
    void deleteFriendTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> putResponse = restTemplate.exchange(
                "/users/1/friends/1", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> deleteRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> deleteResponse = restTemplate.exchange(
                "/users/1/friends/1", HttpMethod.DELETE, deleteRequest, User.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(deleteResponse.getBody()));
    }

    @Test
    void getFriendsByUserId() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> putResponse = restTemplate.exchange(
                "/users/1/friends/1", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        ResponseEntity<User[]> getResponse = restTemplate.getForEntity(
                "/users/1/friends",
                User[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }

    @Test
    void getMutualFriendsByUserId() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        ResponseEntity<User> postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        postRequest = new HttpEntity<>("{\"login\":\"login\",\"email\":\"email@mail.com\"," +
                "\"birthday\":\"1995-01-01\",\"name\":\"name\"}", httpHeaders);

        postResponse = restTemplate.postForEntity(
                "/users",
                postRequest,
                User.class
        );

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(postResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> putRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> putResponse = restTemplate.exchange(
                "/users/1/friends/1", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        putRequest = new HttpEntity<>(httpHeaders);

        putResponse = restTemplate.exchange(
                "/users/1/friends/3", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        putRequest = new HttpEntity<>(httpHeaders);

        putResponse = restTemplate.exchange(
                "/users/2/friends/3", HttpMethod.PUT, putRequest, User.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(putResponse.getBody()));

        ResponseEntity<User[]> getResponse = restTemplate.getForEntity(
                "/users/1/friends/common/2",
                User[].class
        );
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(getResponse.getBody()));
    }
}