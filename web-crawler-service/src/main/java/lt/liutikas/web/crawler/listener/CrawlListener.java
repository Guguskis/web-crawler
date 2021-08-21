package lt.liutikas.web.crawler.listener;

import lt.liutikas.web.crawler.dto.CrawlQueueMessage;
import lt.liutikas.web.crawler.service.CrawlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CrawlListener implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlListener.class);

    private final CrawlingService crawlingService;

    public CrawlListener(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @RabbitListener(queues = "crawl-queue")
    public void listen(CrawlQueueMessage message) {

        crawlingService.processPage(message);

        LOG.info("Page parsed successfully { url : \"{}\"}", message.getUrl());
    }

}
