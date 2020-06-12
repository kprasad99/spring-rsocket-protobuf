package io.github.kprasad99.rsocket;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeType;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableConfigurationProperties(RSocketClientProperties.class)
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
    RSocketRequester rSocketRequester(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies,
            RSocketClientProperties clientProp) {

        // SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new
        // ClientHandler());

        RSocketRequester rsocketRequester = rsocketRequesterBuilder.rsocketStrategies(strategies)
                .dataMimeType(new MimeType("application", "x-protobuf"))
                // .rsocketConnector(connector -> connector.acceptor(responder))
                .connectTcp(clientProp.getHost(), clientProp.getRsocPort()).retry().block();

        rsocketRequester.rsocket().onClose().doOnError(error -> log.warn("Connection CLOSED"))
                .doFinally(consumer -> log.info("Client DISCONNECTED")).subscribe();
        return rsocketRequester;
    }

    @Bean
    public RSocketStrategiesCustomizer protobufRSocketStrategyCustomizer() {
        return (strategy) -> {
            strategy.decoder(new ProtobufDecoder());
            strategy.encoder(new ProtobufEncoder());
        };
    }

}
