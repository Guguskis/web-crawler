package lt.liutikas.web.crawler.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
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
    @Qualifier("reddit")
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .build();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.USER_AGENT, "retardedStockBot-web-crawler 0.1");
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue crawlingQueue() {
        return new Queue("crawl-queue", false);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
