package io.github.kprasad99.rsocket;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.kprasad99.rsocket.endpoint.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableConfigurationProperties(RSocketClientProperties.class)
public class PersonClient {

    @Autowired
    private RSocketClientProperties props;
	private WebClient client;

    private String getUrl() {
        return String.format("http://%s:%d", props.getHost(), props.getHttpPort());
    }

	@PostConstruct
	public void init() {
		client = WebClient.create(getUrl());
	}

	public Flux<Person> persons() {
		return client.get().uri("/api/person").accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Person.class);
	}

	public Mono<Person> findById(int id) {
		return client.get().uri("/api/person/"+id).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Person.class);
	}
}
