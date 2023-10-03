package searchengine.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.Page;
import searchengine.model.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {
    @Query(value = "SELECT * from Site WHERE url = :url", nativeQuery = true)
    Optional<Site> findByUrl (@Param("url") String url);
}
