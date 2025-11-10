package com.wudke.abwicklungsservice;

import com.wudke.abwicklungsservice.persistence.AbwicklungsEntity;
import com.wudke.abwicklungsservice.persistence.AbwicklungsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@DirtiesContext
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class AbwicklungsResourceTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private AbwicklungsRepository abwicklungsRepository;

    @BeforeEach
    void setUp() {
        abwicklungsRepository.deleteAll();
    }

    @Test
    void getAll() {
        RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
            .when()
                .get("v1/abwicklungen")
            .then()
                .statusCode(OK.value());
    }

    @Test
    void getByUnknownId() {
        RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
            .when()
                .get("v1/abwicklungen/" + UUID.randomUUID())
            .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void getByInvalidId() {
        RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
            .when()
                .get("v1/abwicklungen/9999")
            .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void getById() {
        UUID uuid = UUID.randomUUID();
        abwicklungsRepository.save(new AbwicklungsEntity(uuid, null, null, null, null));

        RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
            .when()
                .get("v1/abwicklungen/" + uuid)
            .then()
                .statusCode(OK.value());
    }
}