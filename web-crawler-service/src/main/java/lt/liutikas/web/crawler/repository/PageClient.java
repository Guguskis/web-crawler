package lt.liutikas.web.crawler.repository;

import lt.liutikas.web.crawler.dto.CreatePageDto;
import lt.liutikas.web.crawler.dto.NoBodyPageDto;
import lt.liutikas.web.crawler.dto.UpdatePageDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class PageClient {

    private final RestTemplate restTemplate;

    public PageClient(@Qualifier("page-service") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NoBodyPageDto> getPages() {
        return restTemplate.getForObject("/api/page", List.class);
    }

    public List<NoBodyPageDto> getPagesByParsed(boolean parsed) {
        return restTemplate.getForObject("/api/page?parsed={parsed}", List.class, parsed);
    }


    public NoBodyPageDto createPage(CreatePageDto createPageDto) {
        return restTemplate.postForObject("/api/page", createPageDto, NoBodyPageDto.class);
    }


    public NoBodyPageDto updatePage(String id, UpdatePageDto updatePageDto) {
        return restTemplate.postForObject("/api/page/{id}", updatePageDto, NoBodyPageDto.class, id);
    }

}
