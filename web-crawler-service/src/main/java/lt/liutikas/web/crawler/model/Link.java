package lt.liutikas.web.crawler.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;

@Document
public class Link {

    private URL url;
}
