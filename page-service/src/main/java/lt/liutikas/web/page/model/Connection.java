package lt.liutikas.web.page.model;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Connection {

    @Id
    private String id;
    @DBRef
    private Link linkA;
    @DBRef
    private Link linkB;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Link getLinkA() {
        return linkA;
    }

    public void setLinkA(Link linkA) {
        this.linkA = linkA;
    }

    public Link getLinkB() {
        return linkB;
    }

    public void setLinkB(Link linkB) {
        this.linkB = linkB;
    }
}
