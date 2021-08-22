package lt.liutikas.web.page.repository;

import lt.liutikas.web.page.model.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String> {
}
