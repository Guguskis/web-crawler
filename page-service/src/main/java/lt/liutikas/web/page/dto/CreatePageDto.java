package lt.liutikas.web.page.dto;

import java.net.URL;
import java.util.List;

public class CreatePageDto {

    private URL url;
    private String body;
    private List<URL> childrenUrls;
    private boolean parsed;

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
