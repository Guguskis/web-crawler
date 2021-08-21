package lt.liutikas.web.page.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.List;

@Document
public class Page {

    @Id
    private String id;
    @Indexed(unique = true)
    private URL url;
    private String body;
    private boolean parsed;
    private List<URL> childrenUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<URL> getChildrenUrls() {
        return childrenUrls;
    }

    public void setChildrenUrls(List<URL> childrenUrls) {
        this.childrenUrls = childrenUrls;
    }

    public boolean isParsed() {
        return parsed;
    }

    public void setParsed(boolean parsed) {
        this.parsed = parsed;
    }
}
