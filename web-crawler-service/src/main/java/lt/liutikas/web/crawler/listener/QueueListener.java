package lt.liutikas.web.crawler.listener;

import lt.liutikas.web.crawler.configuration.MessageQueueConfiguration;
import lt.liutikas.web.crawler.dto.LinkQueueMessage;
import lt.liutikas.web.crawler.model.LinkProcessStatus;
import lt.liutikas.web.crawler.service.LinkProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class QueueListener implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(QueueListener.class);

    private final LinkProcessor linkProcessor;

    public QueueListener(LinkProcessor linkProcessor) {
        this.linkProcessor = linkProcessor;
    }

    @RabbitListener(queues = MessageQueueConfiguration.LINK_PROCESSING_QUEUE)
    public void listen(LinkQueueMessage message) {

        LOG.info("Found new message { queue: \"{}\", url: \"{}\" }",
                MessageQueueConfiguration.LINK_PROCESSING_QUEUE, message.getUrl());

        LinkProcessStatus status;

        try {
            status = linkProcessor.process(message);
        } catch (Exception e) {
            LOG.error("Failed to process message due to unknown reason", e);
            status = LinkProcessStatus.FAILED_UNKNOWN;
        }

        LOG.info("Finished processing message { queue: \"{}\", status: {}, url: \"{}\" }",
                MessageQueueConfiguration.LINK_PROCESSING_QUEUE, status, message.getUrl());
    }

}
