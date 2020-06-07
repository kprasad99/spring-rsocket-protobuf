package io.github.kprasad99.rsocket.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.kprasad.person.proto.PersonProto.Person;
import io.github.kprasad99.rsocket.PersonProtoClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api")
@Slf4j
public class PersonProtoService {

	@Autowired
	private PersonProtoClient client;

	@GetMapping(value="/proto-json/person", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Person> list(){
		return client.personsJson().subscribeOn(Schedulers.boundedElastic());
	}
	
	@GetMapping(value="/proto-json/person/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Person> findById(@PathVariable("id")int id){
		log.info("Requesting data for person with id {}", id);
		return client.findByIdJson(id).subscribeOn(Schedulers.boundedElastic());
	}

	@GetMapping(value="/proto/person", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Person> listProto(){
		return client.personsProto().subscribeOn(Schedulers.boundedElastic());
	}
	
	@GetMapping(value="/proto/person/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Person> findByIdProto(@PathVariable("id")int id){
		log.info("Requesting data for person with id {}", id);
		return client.findByIdProto(id).subscribeOn(Schedulers.boundedElastic());
	}

}
