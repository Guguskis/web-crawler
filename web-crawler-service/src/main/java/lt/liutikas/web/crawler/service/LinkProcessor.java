package lt.liutikas.web.crawler.service;

import lt.liutikas.web.crawler.configuration.MessageQueueConfiguration;
import lt.liutikas.web.crawler.dto.LinkQueueMessage;
import lt.liutikas.web.crawler.model.Link;
import lt.liutikas.web.crawler.model.LinkProcessStatus;
import lt.liutikas.web.crawler.repository.LinkClient;
import lt.liutikas.web.crawler.repository.PageClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinkProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(LinkProcessor.class);

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate redditEndpoint;
    private final PageClient pageClient;
    private final LinkClient linkClient;

    public LinkProcessor(RabbitTemplate rabbitTemplate, PageClient pageClient, @Qualifier("reddit") RestTemplate restTemplate, LinkClient linkClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.pageClient = pageClient;
        this.redditEndpoint = restTemplate;
        this.linkClient = linkClient;
    }

    public LinkProcessStatus process(LinkQueueMessage message) {
        URL url;
        try {
            url = new URL(message.getUrl());
        } catch (MalformedURLException e) {
            LOG.error("Failed to add message to queue due to malformed url { url: \"{}\" }", message.getUrl());
            return LinkProcessStatus.MALFORMED_URL;
        }

        Document pageBody = getPageBody(url);

        List<String> urls = pageBody
                .getElementsByAttribute("href").stream()
                .map(this::parseUrl)
                .filter(this::isCrawlableUrl)
                .filter(this::isRedditUrl)
                .collect(Collectors.toList());

        List<Link> savedLinks = linkClient.save(urls, message.getUrl());
        List<String> savedUrls = savedLinks.stream()
                .map(Link::getUrl)
                .map(URL::toString)
                .collect(Collectors.toList());

        LOG.info("Parsed links for page { url:\"{}\" }", url);

        savedUrls.stream()
                .map(this::assembleQueueMessage)
                .forEach(this::addToQueue);

        LOG.info("Added new messages to queue { queue: \"{}\", messageCount: {}}", MessageQueueConfiguration.LINK_PROCESSING_QUEUE, savedUrls.size());

        return LinkProcessStatus.SUCCESS;
    }

    private Document getPageBody(URL url) {
        String pageBody = redditEndpoint.getForObject(url.toString(), String.class);
        return Jsoup.parse(pageBody);
    }

    private String parseUrl(Element element) {
        return element.attr("abs:href");
    }

    private LinkQueueMessage assembleQueueMessage(String url) {
        LinkQueueMessage linkQueueMessage = new LinkQueueMessage();
        linkQueueMessage.setUrl(url);
        return linkQueueMessage;
    }

    private boolean isRedditUrl(String url) {
        return url.contains("reddit.com");
    }

    private boolean isCrawlableUrl(String url) {
        boolean relativeUrl = url.startsWith("/");
        boolean absoluteUrl = url.startsWith("http");
        return relativeUrl || absoluteUrl;
    }

    private void addToQueue(LinkQueueMessage newMessage) {
        rabbitTemplate.convertAndSend(MessageQueueConfiguration.LINK_PROCESSING_QUEUE, newMessage);
    }

}
