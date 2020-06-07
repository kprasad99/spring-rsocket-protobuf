package io.github.kprasad99.rsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.rsocket.RSocketStrategiesAutoConfiguration;

@SpringBootApplication(exclude = { RSocketStrategiesAutoConfiguration.class })
public class PersonClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonClientServiceApplication.class, args);
	}

}
