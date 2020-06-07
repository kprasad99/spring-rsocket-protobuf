package io.github.kprasad99.rsocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "person.backend")
@Data
public class RSocketClientProperties {

    private String host;
    private int port;

}
