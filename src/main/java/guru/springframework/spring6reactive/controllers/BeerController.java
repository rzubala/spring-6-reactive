package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @DeleteMapping(BEER_PATH_ID)
    ResponseEntity<Void> deleteById(@PathVariable("beerId") Integer beerId) {
        beerService.deleteBeerById(beerId).subscribe();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    ResponseEntity<Void> patchBeer(@PathVariable("beerId") Integer beerId,
                                   @Validated @RequestBody BeerDTO beerDTO) {
        beerService.patchBeer(beerId, beerDTO).subscribe();
        return ResponseEntity.ok().build();
    }

    @PutMapping(BEER_PATH_ID)
    ResponseEntity<Void> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                                  @Validated @RequestBody BeerDTO beerDTO) {
        beerService.updateBeer(beerId, beerDTO).subscribe();
        return ResponseEntity.ok().build();
    }

    @PostMapping(BEER_PATH)
    ResponseEntity<Void> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        AtomicInteger atomicInteger = new AtomicInteger();

        beerService.createNewBeer(beerDTO).subscribe(savedDto -> {
           atomicInteger.set(savedDto.getId());
        });

        return ResponseEntity.created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/" + BEER_PATH
                        + "/" + atomicInteger.get()).build().toUri()).build();
    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId);
    }

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

}
