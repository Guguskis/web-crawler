package lt.liutikas.web.page.service;

import lt.liutikas.web.page.dto.CreateLinkDto;
import lt.liutikas.web.page.model.Link;
import lt.liutikas.web.page.repository.ConnectionRepository;
import lt.liutikas.web.page.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final ConnectionRepository connectionRepository;

    public LinkService(LinkRepository linkRepository, ConnectionRepository connectionRepository) {
        this.linkRepository = linkRepository;
        this.connectionRepository = connectionRepository;
    }

    public List<Link> saveUniqueLinks(List<CreateLinkDto> createLinkDtos) {

        // todo
        // save unique links
        // save connections
        // return saved links

        return null;
    }
}
