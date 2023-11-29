package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(2)
    void testListBeers() {
        webTestClient.get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3)
        ;
    }

    @Test
    @Order(1)
    void testGetById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(BeerDTO.class)
        ;
    }

    @Test
    void testCreateBeer() {
        String location = "http://localhost:8080/api/v2/beer/4";
        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location(location);
        ;
    }

    @Test
    @Order(3)
    void testUpdateBeer() {
        webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(99)
    void testDeleteBeer() {
        webTestClient.delete().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isNoContent()
                ;
    }
}