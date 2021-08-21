package lt.liutikas.web.page.repository;

import lt.liutikas.web.page.model.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends MongoRepository<Page, String> {

    @Query(value = "{}", fields = "{url : 1, parsed : 1, childrenUrls: 1}")
    List<Page> findAllExcludeBodyByParsed();

    @Query(value = "{ parsed: ?0 }", fields = "{url : 1, parsed : 1, childrenUrls: 1}")
    List<Page> findAllExcludeBodyByParsed(Boolean parsed);
}
