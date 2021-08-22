package lt.liutikas.web.page.api;

import lt.liutikas.web.page.dto.CreateLinkDto;
import lt.liutikas.web.page.model.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/link")
public interface LinkController {

    @PostMapping("/batch")
    ResponseEntity<List<Link>> saveLinks(@RequestBody List<CreateLinkDto> createLinkDtos);

    @GetMapping("/link/{id}/connection")
    ResponseEntity<List<Link>> getConnections(@PathVariable String id);

}
