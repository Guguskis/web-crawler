package lt.liutikas.web.page.repository;

import lt.liutikas.web.page.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.Optional;

@Repository
public interface LinkRepository extends MongoRepository<Link, String> {

    Optional<Link> findByUrl(URL url);

}
