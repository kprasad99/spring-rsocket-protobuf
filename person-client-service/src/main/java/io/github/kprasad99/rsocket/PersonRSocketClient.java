package io.github.kprasad99.rsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import io.github.kprasad.person.proto.PersonProto.Person;
import reactor.core.publisher.Flux;

@Service
public class PersonRSocketClient {

    @Autowired
    private RSocketRequester personClient;

    public Flux<Person> list() {
        return personClient.route("io.github.kprasad99.proto.person").retrieveFlux(Person.class);
    }

}

