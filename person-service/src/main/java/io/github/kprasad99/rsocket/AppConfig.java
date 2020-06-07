package io.github.kprasad99.rsocket;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import io.rsocket.core.Resume;

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

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RSocketServerCustomizer rsocketCustomizer() {
        return (rsoc) -> {
            rsoc.resume(new Resume());
        };
    }

    @Bean
    public RSocketStrategiesCustomizer protoRSocketStrategyCustomizer() {
        return (strategy) -> {
            strategy.decoder(new ProtobufDecoder());
            strategy.encoder(new ProtobufEncoder());
        };
    }

}
