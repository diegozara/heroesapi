package com.digitalinnovation.heroesapi.controller;

import com.digitalinnovation.heroesapi.document.Heroes;
import com.digitalinnovation.heroesapi.repository.HeroesRepository;
import com.digitalinnovation.heroesapi.service.HeroesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.digitalinnovation.heroesapi.constans.HerosConstant.HEROES_ENDPOINT_LOCAL;

@RestController
@Slf4j
public class HeroesController {

    HeroesService heroesService;
    HeroesRepository heroesRepository;

    private static final org.slf4j.Logger logController = org.slf4j.LoggerFactory.getLogger(HeroesController.class);

    public HeroesController (HeroesService heroesService, HeroesRepository heroesRepository){
        this.heroesRepository = heroesRepository;
        this.heroesService = heroesService;
    }

    @GetMapping(HEROES_ENDPOINT_LOCAL)
    public Flux<Heroes> getAllItems (){
        logController.info("requesting the list of all heroes");
         return heroesService.findAll();
    }

   @GetMapping(HEROES_ENDPOINT_LOCAL+"/id")
    public Mono<ResponseEntity<Heroes>> findByIdHero (@PathVariable String id){

       logController.info("requestinh the hero with id {}",id);

        return heroesService.findById(id)
                .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   @PostMapping(HEROES_ENDPOINT_LOCAL)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Heroes> createHero(@RequestBody Heroes heroes){

       logController.info("a new Hero was created");

        return heroesService.save(heroes);
   }

   @DeleteMapping(HEROES_ENDPOINT_LOCAL+"/id")
    @ResponseStatus(code = HttpStatus.CONTINUE)
    public Mono <HttpStatus> deleteByIDHero (@PathVariable String id){
        heroesService.deleteByIdHero(id);

       logController.info("deleting a hero with id {}",id);

        return Mono.just(HttpStatus.CONTINUE);
   }

}
