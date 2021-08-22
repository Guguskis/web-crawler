package lt.liutikas.web.page.controller;

import lt.liutikas.web.page.api.LinkController;
import lt.liutikas.web.page.dto.CreateLinkDto;
import lt.liutikas.web.page.model.Link;
import lt.liutikas.web.page.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DefaultLinkController implements LinkController {

    private final LinkService linkService;

    public DefaultLinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public ResponseEntity<List<Link>> saveLinks(List<CreateLinkDto> createLinkDtos) {
        return ResponseEntity.ok(linkService.saveLinks(createLinkDtos));
    }

}
