package lt.liutikas.web.page.api;

import lt.liutikas.web.page.dto.CreateLinkDto;
import lt.liutikas.web.page.model.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/link")
public interface LinkController {

//    @PostMapping
//    ResponseEntity<List<Link>> saveUniqueLinks(@RequestBody List<CreateLinkDto> createLinkDtos);

    //    @GetMapping("/{url}")
//    ResponseEntity<Boolean> isUnique(@PathVariable URL url);
//
    @PostMapping
    ResponseEntity<Link> saveLink(@RequestBody CreateLinkDto createLinkDto);

}
