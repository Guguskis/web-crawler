package lt.liutikas.web.crawler.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lt.liutikas.web.crawler.dto.CrawlQueueMessage;
import lt.liutikas.web.crawler.repository.PageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CrawlingService {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlingService.class);


    private final RabbitTemplate rabbitTemplate;
    private final PageRepository pageRepository;
    private final RestTemplate redditEndpoint;
    private final LinkService linkService;

    public CrawlingService(RabbitTemplate rabbitTemplate, PageRepository pageRepository, @Qualifier("reddit") RestTemplate restTemplate, LinkService linkService) {
        this.rabbitTemplate = rabbitTemplate;
        this.pageRepository = pageRepository;
        this.redditEndpoint = restTemplate;
        this.linkService = linkService;
    }

    public void processPage(CrawlQueueMessage message) {
        URL url;
        try {
            url = new URL(message.getUrl());
        } catch (MalformedURLException e) {
            LOG.error("Failed to add message to queue due to malformed url { url: \"{}\"}", message.getUrl());
            return;
        }

        String pageBody = redditEndpoint.getForObject(message.getUrl(), String.class);

        Document document = Jsoup.parse(pageBody);

        Elements hrefElements = document.getElementsByAttribute("href");
        List<String> validUrls = hrefElements.stream()
                .map(parseLinks())
                .filter(this::isCrawlableUrl)
                .map(makeAbsolute(url))
                .filter(this::isRedditUrl)
                .collect(Collectors.toList());

        validUrls = removeDuplicates(validUrls);

        List<CrawlQueueMessage> crawlQueueMessages = validUrls.stream()
                .map(this::assembleCrawlQueueMessage)
                .collect(Collectors.toList());

        crawlQueueMessages.forEach(this::addToQueue);

        LOG.info("Processed page { url:\"{}\", urlCount: {}}", url, validUrls.size());
    }

    private Function<String, String> makeAbsolute(URL url) {
        return unprocessedUrl -> convertToAbsoluteUrl(url, unprocessedUrl);
    }

    private Function<Element, String> parseLinks() {
        return element -> element.attr("href");
    }

    private CrawlQueueMessage assembleCrawlQueueMessage(String url) {
        CrawlQueueMessage crawlQueueMessage = new CrawlQueueMessage();
        crawlQueueMessage.setUrl(url);
        return crawlQueueMessage;
    }

    private ArrayList<String> removeDuplicates(List<String> validUrls) {
        return Lists.newArrayList(Sets.newHashSet(validUrls));
    }

    private boolean isRedditUrl(String urlString) {
        URL url;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOG.warn("Malformed url { url: \"{}\"}", urlString);
            return false;
        }

        String host = url.getHost();
        return host.contains("reddit.com");
    }

    private String convertToAbsoluteUrl(URL baseUrl, String url) {

        String protocol = baseUrl.getProtocol() + "://";
        String baseUrlString = protocol + baseUrl.getHost();

        boolean isProtocolAgnostic = url.startsWith("//");
        boolean isRelative = url.startsWith("/");

        if (isProtocolAgnostic) {
            url = protocol + url.substring(2);
        } else if (isRelative) {
            url = baseUrlString + url;
        }

        return url;
    }

    private boolean isCrawlableUrl(String url) {
        boolean relativeUrl = url.startsWith("/");
        boolean absoluteUrl = url.startsWith("http");
        return relativeUrl || absoluteUrl;
    }

    private void addToQueue(CrawlQueueMessage newMessage) {
        rabbitTemplate.convertAndSend("crawl-queue", newMessage);
    }
}
