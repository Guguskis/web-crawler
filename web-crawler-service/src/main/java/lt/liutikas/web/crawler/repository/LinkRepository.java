package lt.liutikas.web.crawler.repository;

import lt.liutikas.web.crawler.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, String> {
}
