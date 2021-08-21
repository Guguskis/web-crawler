package lt.liutikas.web.page.dto;

import java.net.URL;
import java.util.List;

public class NoBodyPageDto {

    private String id;
    private URL url;
    private List<URL> childrenUrls;
    private boolean parsed;

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
