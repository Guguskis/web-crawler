package lt.liutikas.web.crawler.controller;

import lt.liutikas.web.crawler.dto.CrawlQueueMessage;
import lt.liutikas.web.crawler.dto.NoBodyPageDto;
import lt.liutikas.web.crawler.repository.PageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PageRepository pageRepository;
    private final RabbitTemplate rabbitTemplate;

    public AdminController(PageRepository pageRepository, RabbitTemplate rabbitTemplate) {
        this.pageRepository = pageRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public ResponseEntity<List<NoBodyPageDto>> getPages(@RequestParam(required = false) Optional<Boolean> parsed) {

        if (parsed.isPresent()) {
            return ResponseEntity.ok(pageRepository.getPagesByParsed(parsed.get()));
        } else {
            return ResponseEntity.ok(pageRepository.getPages());
        }
    }

    @PostMapping
    public void addMessageToQueue(@RequestBody CrawlQueueMessage message) {
        rabbitTemplate.convertAndSend("crawl-queue", message);
    }

}
