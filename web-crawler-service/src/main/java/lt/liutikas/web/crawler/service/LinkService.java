package lt.liutikas.web.crawler.service;

import lt.liutikas.web.crawler.repository.LinkRepository;

public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }
}
