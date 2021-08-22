package lt.liutikas.web.crawler.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lt.liutikas.web.crawler.dto.CrawlQueueMessage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlingService {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlingService.class);


    private final RabbitTemplate rabbitTemplate;
    private final PageClient pageClient;
    private final RestTemplate redditEndpoint;
    private final LinkClient linkClient;

    public CrawlingService(RabbitTemplate rabbitTemplate, PageClient pageClient, @Qualifier("reddit") RestTemplate restTemplate, LinkClient linkClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.pageClient = pageClient;
        this.redditEndpoint = restTemplate;
        this.linkClient = linkClient;
    }

    public void processPage(CrawlQueueMessage message) {
        URL url;
        try {
            url = new URL(message.getUrl());
        } catch (MalformedURLException e) {
            LOG.error("Failed to add message to queue due to malformed url { url: \"{}\"}", message.getUrl());
            return;
        }

        Document pageBody = getPageBody(url);

        List<String> validUrls = pageBody
                .getElementsByAttribute("href").stream()
                .map(this::parseUrl)
                .filter(this::isCrawlableUrl)
                .filter(this::isRedditUrl)
                .collect(Collectors.toList());

        validUrls = validUrls.stream()
                .filter(validUrl -> savedUniqueUrl(validUrl, message.getUrl()))
                .collect(Collectors.toList());

        LOG.info("Parsed links for page { url:\"{}\"}", url);

        validUrls.stream()
                .map(this::assembleCrawlQueueMessage)
                .forEach(this::addToQueue);

        LOG.info("Added new urls to queue { urlCount: {}}", validUrls.size());
    }

    private Document getPageBody(URL url) {
        String pageBody = redditEndpoint.getForObject(url.toString(), String.class);

        return Jsoup.parse(pageBody);
    }

    private String parseUrl(Element element) {
        return element.attr("abs:href");
    }

    private CrawlQueueMessage assembleCrawlQueueMessage(String url) {
        CrawlQueueMessage crawlQueueMessage = new CrawlQueueMessage();
        crawlQueueMessage.setUrl(url);
        return crawlQueueMessage;
    }

    private ArrayList<String> removeDuplicates(List<String> validUrls) {
        return Lists.newArrayList(Sets.newHashSet(validUrls));
    }

    private boolean isRedditUrl(String url) {
        return url.contains("reddit.com");
    }

    private boolean isCrawlableUrl(String url) {
        boolean relativeUrl = url.startsWith("/");
        boolean absoluteUrl = url.startsWith("http");
        return relativeUrl || absoluteUrl;
    }

    private void addToQueue(CrawlQueueMessage newMessage) {
        rabbitTemplate.convertAndSend("crawl-queue", newMessage);
    }

    private boolean savedUniqueUrl(String url, String sourceUrl) {
        return linkClient.saveUnique(url, sourceUrl);
    }
}
