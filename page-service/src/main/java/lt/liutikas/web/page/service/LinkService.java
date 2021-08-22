package lt.liutikas.web.page.service;

import lt.liutikas.web.page.configuration.exception.BadRequestException;
import lt.liutikas.web.page.dto.CreateLinkDto;
import lt.liutikas.web.page.model.Connection;
import lt.liutikas.web.page.model.Link;
import lt.liutikas.web.page.repository.ConnectionRepository;
import lt.liutikas.web.page.repository.LinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class LinkService {

    private static final Logger LOG = LoggerFactory.getLogger(LinkService.class);

    private final LinkRepository linkRepository;
    private final ConnectionRepository connectionRepository;

    public LinkService(LinkRepository linkRepository, ConnectionRepository connectionRepository) {
        this.linkRepository = linkRepository;
        this.connectionRepository = connectionRepository;
    }

    public List<Link> saveLinks(List<CreateLinkDto> createLinkDtos) {

        List<Link> savedLinks = new LinkedList<>();

        createLinkDtos
                .stream().filter(this::filterValidDtos)
                .forEach(tryAddSavedLink(savedLinks));

        LOG.info("Saved links batch { linkCount: {}, savedLinkCount: {} }", createLinkDtos.size(), savedLinks.size());

        return savedLinks;
    }

    private Consumer<CreateLinkDto> tryAddSavedLink(List<Link> savedLinks) {
        return createLinkDto -> {
            try {
                savedLinks.add(saveLink(createLinkDto));
            } catch (DuplicateKeyException e) {
                // ignore invalid link
            }
        };
    }

    private boolean filterValidDtos(CreateLinkDto createLinkDto) {
        boolean urlsDistinct = !createLinkDto.getUrl().equals(createLinkDto.getSourceUrl());

        return urlsDistinct;
    }

    public Link saveLink(CreateLinkDto createLinkDto) {
        assertValidRequest(createLinkDto);

        Link link = saveLink(createLinkDto.getUrl());
        Link sourceLink = upsertLink(createLinkDto.getSourceUrl());
        saveConnection(link, sourceLink);

        return link;
    }

    private void assertValidRequest(CreateLinkDto createLinkDto) {
        boolean duplicatedUrls = createLinkDto.getUrl().equals(createLinkDto.getSourceUrl());

        if (duplicatedUrls) {
            throw new BadRequestException(String.format(
                    "Urls must be distinct { url: \"%s\", sourceUrl: \"%s\" }",
                    createLinkDto.getUrl(), createLinkDto.getSourceUrl()));
        }
    }

    private void saveConnection(Link link, Link sourceLink) {
        Connection connection = new Connection();
        connection.setLinkA(link);
        connection.setLinkB(sourceLink);

        connectionRepository.save(connection);
    }

    private Link saveLink(URL url) {
        Link link = new Link();
        link.setUrl(url);
        link = linkRepository.save(link); // should throw on not unique
        return link;
    }

    private Link upsertLink(URL sourceUrl) {
        Optional<Link> link = linkRepository.findByUrl(sourceUrl);

        if (link.isEmpty()) {
            Link sourceLink = new Link();
            sourceLink.setUrl(sourceUrl);
            return linkRepository.save(sourceLink);
        }

        return link.get();
    }
}
