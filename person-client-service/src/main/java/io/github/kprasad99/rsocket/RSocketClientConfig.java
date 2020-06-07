package io.github.kprasad99.rsocket;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeType;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import io.rsocket.core.Resume;

@Configuration
public class RSocketClientConfig {

    private static final String PATHPATTERN_ROUTEMATCHER_CLASS = "org.springframework.web.util.pattern.PathPatternRouteMatcher";

    @Bean
    public RSocketStrategies rSocketStrategies(ObjectProvider<RSocketStrategiesCustomizer> customizers) {
        RSocketStrategies.Builder builder = RSocketStrategies.builder();
        if (ClassUtils.isPresent(PATHPATTERN_ROUTEMATCHER_CLASS, null)) {
            builder.routeMatcher(new PathPatternRouteMatcher());
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

    @Bean
    RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder().dataMimeType(new MimeType("application", "x-protobuf"))
                .rsocketStrategies(rSocketStrategies).connectTcp("localhost", 9080).retry().block();
    }

    @Bean
    public RSocketServerCustomizer rsocketCustomizer() {
        return (rsoc) -> {
            rsoc.resume(new Resume());
        };
    }

    @Bean
    public RSocketStrategiesCustomizer protobufRSocketStrategyCustomizer() {
        return (strategy) -> {
            strategy.decoder(new ProtobufDecoder());
            strategy.encoder(new ProtobufEncoder());
        };
    }

}
