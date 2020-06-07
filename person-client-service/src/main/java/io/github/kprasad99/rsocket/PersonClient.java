package io.github.kprasad99.rsocket;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.kprasad99.rsocket.endpoint.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonClient {

	@Value("${client.port:8080}")
	private int port;
	private WebClient client;

	@PostConstruct
	public void init() {
		client = WebClient.create("http://localhost:" + port);
	}

	public Flux<Person> persons() {
		return client.get().uri("/api/person").accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Person.class);
	}
	
	public Mono<Person> findById(int id) {
		return client.get().uri("/api/person/"+id).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Person.class);
	}
}
