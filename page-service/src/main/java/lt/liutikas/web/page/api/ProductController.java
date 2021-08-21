package lt.liutikas.web.page.api;

import lt.liutikas.web.page.dto.CreatePageDto;
import lt.liutikas.web.page.dto.NoBodyPageDto;
import lt.liutikas.web.page.dto.UpdatePageDto;
import lt.liutikas.web.page.model.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/page")
public interface ProductController {

    @GetMapping
    ResponseEntity<List<NoBodyPageDto>> getPages(@RequestParam(required = false) Optional<Boolean> parsed);

    @GetMapping("/{id}")
    ResponseEntity<Page> getPageById(@PathVariable String id);

    @PostMapping
    ResponseEntity<NoBodyPageDto> createPage(@RequestBody CreatePageDto createPageDto);

    @PatchMapping("/{id}")
    ResponseEntity<NoBodyPageDto> updatePage(@PathVariable String id, @RequestBody UpdatePageDto updatePageDto);

}
