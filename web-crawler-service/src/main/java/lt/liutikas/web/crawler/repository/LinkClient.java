package lt.liutikas.web.crawler.repository;

import lt.liutikas.web.crawler.dto.CreateLinkDto;
import lt.liutikas.web.crawler.model.Link;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LinkClient {

    private final RestTemplate pageServiceEndpoint;

    public LinkClient(@Qualifier("page-service") RestTemplate restTemplate) {
        this.pageServiceEndpoint = restTemplate;
    }


    public List<Link> save(List<String> urls, String sourceUrl) {

        List<CreateLinkDto> request = urls.stream()
                .map(assembleRequest(sourceUrl))
                .collect(Collectors.toList());

        Link[] savedLinks = pageServiceEndpoint.postForObject("/api/link/batch", request, Link[].class);

        return List.of(savedLinks);
    }

    private Function<String, CreateLinkDto> assembleRequest(String sourceUrl) {
        return url -> {
            CreateLinkDto createLinkDto = new CreateLinkDto();
            createLinkDto.setUrl(url);
            createLinkDto.setSourceUrl(sourceUrl);
            return createLinkDto;
        };
    }

}
