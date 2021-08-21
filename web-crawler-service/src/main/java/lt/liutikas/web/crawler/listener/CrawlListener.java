package lt.liutikas.web.crawler.listener;

import lt.liutikas.web.crawler.dto.CrawlQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CrawlListener implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlListener.class);

    @RabbitListener(queues = "crawl-queue")
    public void listen(CrawlQueueMessage message) {
        LOG.info("Message received { message : \"{}\"}", message.getUrl());
    }

}
