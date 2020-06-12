package io.github.kprasad99.rsocket;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import io.github.kprasad.person.proto.PersonProto.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableConfigurationProperties(RSocketClientProperties.class)
public class PersonProtoClient {

    @Autowired
    private RSocketClientProperties props;
	private WebClient client;

	private String getUrl() {
	    return String.format("http://%s:%d", props.getHost(), props.getHttpPort());
	}

	@PostConstruct
	public void init() {
		client = WebClient.builder().codecs(ccc -> {
			ccc.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(
					Jackson2ObjectMapperBuilder.json().serializerByType(Message.class, new JsonSerializer<Message>() {
						@Override
						public void serialize(Message value, JsonGenerator gen, SerializerProvider serializers)
								throws IOException {
							String str = JsonFormat.printer().omittingInsignificantWhitespace().print(value);
							gen.writeRawValue(str);
						}
					}).build()));
		}).baseUrl(getUrl()).build();
	}

	public Flux<Person> personsJson() {
		return client.get().uri("/api/proto/person").accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToFlux(Person.class);
	}

	public Mono<Person> findByIdJson(int id) {
		return client.get().uri("/api/proto/person/" + id).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(Person.class);
	}

	public Flux<Person> personsProto() {
		return client.get().uri("/api/proto/person").accept(new MediaType("application", "x-protobuf")).retrieve()
				.bodyToFlux(Person.class);
	}

	public Mono<Person> findByIdProto(int id) {
		return client.get().uri("/api/proto/person/" + id).accept(new MediaType("application", "x-protobuf")).retrieve()
				.bodyToMono(Person.class);
	}
}
