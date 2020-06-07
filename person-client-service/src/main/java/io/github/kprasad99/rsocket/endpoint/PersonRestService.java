package io.github.kprasad99.rsocket.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.kprasad99.rsocket.PersonClient;
import io.github.kprasad99.rsocket.endpoint.model.Person;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/person")
@Slf4j
public class PersonRestService {

	@Autowired
	private PersonClient client;
	
	@GetMapping
	public Flux<Person> list(){
		return client.persons().subscribeOn(Schedulers.boundedElastic());
	}
	
	@GetMapping("/{id}")
	public Mono<Person> findById(@PathVariable("id")int id){
		log.info("Requesting data for person with id {}", id);
		return client.findById(id).subscribeOn(Schedulers.boundedElastic());
	}
	
}
