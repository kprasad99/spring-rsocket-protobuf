package io.github.kprasad99.rsocket;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

@Configuration
@EnableWebFlux
public class AppConfig implements WebFluxConfigurer {

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(
				Jackson2ObjectMapperBuilder.json().serializerByType(Message.class, new JsonSerializer<Message>() {
					@Override
					public void serialize(Message value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						String str = JsonFormat.printer().omittingInsignificantWhitespace().print(value);
						gen.writeRawValue(str);
					}
				}).build()));

	}

}
