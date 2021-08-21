package lt.liutikas.web.page.controller;

import lt.liutikas.web.page.api.ProductController;
import lt.liutikas.web.page.dto.CreatePageDto;
import lt.liutikas.web.page.dto.NoBodyPageDto;
import lt.liutikas.web.page.dto.UpdatePageDto;
import lt.liutikas.web.page.model.Page;
import lt.liutikas.web.page.service.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DefaultProductController implements ProductController {

    private final PageService pageService;

    public DefaultProductController(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public ResponseEntity<List<NoBodyPageDto>> getPages() {
        return ResponseEntity.ok(pageService.getPages());
    }

    @Override
    public ResponseEntity<Page> getPageById(String id) {
        return ResponseEntity.ok(pageService.getPageById(id));
    }

    @Override
    public ResponseEntity<NoBodyPageDto> createPage(CreatePageDto createPageDto) {
        return ResponseEntity.ok(pageService.createPage(createPageDto));
    }

    @Override
    public ResponseEntity<NoBodyPageDto> updatePage(String id, UpdatePageDto updatePageDto) {
        return ResponseEntity.ok(pageService.updatePage(id, updatePageDto));
    }

}
