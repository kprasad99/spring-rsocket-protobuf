package io.github.kprasad99.rsocket.endpoint;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.google.protobuf.Empty;

import io.github.kprasad.person.proto.PersonProto.Person;
import io.github.kprasad.person.proto.PersonService;
import io.github.kprasad99.rsocket.orm.dao.PersonDao;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Controller
@Slf4j
public class PersonRSocketService implements PersonService {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ModelMapper mapper;

    @Override
   // @MessageMapping("io.github.kprasad99.proto.person")
    public Flux<Person> get(Empty message, ByteBuf metadata) {
        return Flux.fromIterable(personDao.findAll()).map(toProto).map(Person.Builder::build).subscribeOn(Schedulers.boundedElastic());
    }

    @MessageMapping("io.github.kprasad99.proto.person")
    public Flux<Person> getProto() {
        log.info("Listing all persons");
        return Flux.fromIterable(personDao.findAll()).map(toProto).map(Person.Builder::build).subscribeOn(Schedulers.boundedElastic());
    }

    @MessageMapping("io.github.kprasad99.json.person")
    public Flux<io.github.kprasad99.rsocket.orm.model.Person> getJson() {
        return Flux.fromIterable(personDao.findAll()).subscribeOn(Schedulers.boundedElastic());
    }

    public Function<io.github.kprasad99.rsocket.orm.model.Person, Person.Builder> toProto = p -> mapper.map(p,
            Person.Builder.class);
}
