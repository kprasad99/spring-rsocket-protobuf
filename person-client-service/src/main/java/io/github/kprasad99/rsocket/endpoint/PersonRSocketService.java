package io.github.kprasad99.rsocket.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.kprasad.person.proto.PersonProto.Person;
import io.github.kprasad99.rsocket.PersonRSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api")
public class PersonRSocketService {

	@Autowired
	private PersonRSocketClient client;

	@GetMapping(value="/rsoc/person", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Person> list(){
		return client.list().subscribeOn(Schedulers.boundedElastic());
	}
}
