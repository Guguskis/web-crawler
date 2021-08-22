package lt.liutikas.web.crawler.repository;

import lt.liutikas.web.crawler.dto.CreateLinkDto;
import lt.liutikas.web.crawler.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class LinkClient {

    private static final Logger LOG = LoggerFactory.getLogger(LinkClient.class);

    private final RestTemplate pageServiceEndpoint;

    public LinkClient(@Qualifier("page-service") RestTemplate restTemplate) {
        this.pageServiceEndpoint = restTemplate;
    }

    public boolean saveUnique(String url, String sourceUrl) {
        CreateLinkDto createLinkDto = new CreateLinkDto();
        createLinkDto.setUrl(url);
        createLinkDto.setSourceUrl(sourceUrl);
        try {
            pageServiceEndpoint.postForObject("/api/link", createLinkDto, Link.class);
            return true;
        } catch (RestClientException e) {
            boolean is4xxClientError = ((HttpClientErrorException.BadRequest) e).getStatusCode().is4xxClientError();
            if (!is4xxClientError) {
                LOG.warn("Failed to save link { message: \"{}\" }", e.getMessage());
            }
            return false;
        }
    }

}
