package lt.liutikas.web.page.assembler;

import lt.liutikas.web.page.dto.CreatePageDto;
import lt.liutikas.web.page.dto.NoBodyPageDto;
import lt.liutikas.web.page.model.Page;
import org.springframework.stereotype.Component;

@Component
public class PageAssembler {

    public NoBodyPageDto assembleNoBodyPage(Page page) {

        NoBodyPageDto noBodyPageDto = new NoBodyPageDto();
        noBodyPageDto.setId(page.getId());
        noBodyPageDto.setUrl(page.getUrl());
        noBodyPageDto.setChildrenUrls(page.getChildrenUrls());
        noBodyPageDto.setParsed(page.isParsed());

        return noBodyPageDto;
    }

    public Page assemblePage(CreatePageDto createPageDto) {

        Page page = new Page();
        page.setUrl(createPageDto.getUrl());
        page.setBody(createPageDto.getBody());
        page.setChildrenUrls(createPageDto.getChildrenUrls());
        page.setParsed(createPageDto.isParsed());

        return page;
    }
}
