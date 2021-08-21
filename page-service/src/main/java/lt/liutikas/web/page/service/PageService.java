package lt.liutikas.web.page.service;

import lt.liutikas.web.page.assembler.PageAssembler;
import lt.liutikas.web.page.configuration.exception.NotFoundException;
import lt.liutikas.web.page.dto.CreatePageDto;
import lt.liutikas.web.page.dto.NoBodyPageDto;
import lt.liutikas.web.page.dto.UpdatePageDto;
import lt.liutikas.web.page.model.Page;
import lt.liutikas.web.page.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PageService {

    private static final Logger LOG = LoggerFactory.getLogger(PageService.class);

    private final PageAssembler pageAssembler;
    private final PageRepository pageRepository;

    public PageService(PageAssembler pageAssembler, PageRepository pageRepository) {
        this.pageAssembler = pageAssembler;
        this.pageRepository = pageRepository;
    }

    public List<NoBodyPageDto> getPages() {
        List<Page> pages = pageRepository.findAllExcludeBody();

        List<NoBodyPageDto> noBodyPages = pages.stream()
                .map(pageAssembler::assembleNoBodyPage)
                .collect(Collectors.toList());

        LOG.info("Returned pages");

        return noBodyPages;
    }

    public Page getPageById(String id) {

        Optional<Page> page = pageRepository.findById(id);

        if (page.isEmpty()) {
            throw new NotFoundException(String.format("Page not found { id: \"%s\"}", id));
        }

        LOG.info(String.format("Page returned { id: \"%s\"}", id));

        return page.get();
    }

    public NoBodyPageDto createPage(CreatePageDto createPageDto) {

        Page page = pageAssembler.assemblePage(createPageDto);

        Page createdPage = pageRepository.save(page); // todo throw BAD_REQUEST if URL not unique

        LOG.info(String.format("Page created { id: \"%s\"}", createdPage.getId()));
        return pageAssembler.assembleNoBodyPage(createdPage);
    }

    public NoBodyPageDto updatePage(String id, UpdatePageDto updatePageDto) {

        Page page = getPageById(id);

        if (updatePageDto.isParsed() != null) {
            page.setParsed(updatePageDto.isParsed());
        }

        pageRepository.save(page);

        return pageAssembler.assembleNoBodyPage(page);
    }
}
