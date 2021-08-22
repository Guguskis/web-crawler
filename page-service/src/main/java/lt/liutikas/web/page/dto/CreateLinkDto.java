package lt.liutikas.web.page.dto;

import java.net.URL;

public class CreateLinkDto {

    public URL url;
    public URL sourceUrl;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(URL sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
