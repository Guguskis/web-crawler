package lt.liutikas.web.crawler.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Component
public class WebConfig {

    @Bean("page-service")
    public RestTemplate getGoogleRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:8080")
                .build();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
