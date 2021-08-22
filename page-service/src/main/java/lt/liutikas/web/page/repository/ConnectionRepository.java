package lt.liutikas.web.page.repository;

import lt.liutikas.web.page.model.Connection;
import lt.liutikas.web.page.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String> {

    List<Connection> findByLinkAOrLinkB(Link linkA, Link linkB);

}
