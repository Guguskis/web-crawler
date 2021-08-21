package lt.liutikas.web.crawler.dto;

import java.io.Serializable;

public class CrawlQueueMessage implements Serializable {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
