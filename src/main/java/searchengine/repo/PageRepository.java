package searchengine.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    Page findByPath(String path);

    Optional<Page> findByIdAndSite(int id, Site site);
    @Query(value = "SELECT * from Page WHERE id IN :ids", nativeQuery = true)
    List<Page> findByIds (@Param("ids")int[] ids);

    @Query(value = "SELECT count(*) from Page where site_id = :id")
    long count(@Param("id") long id);
    @Query(value = "SELECT * from Page WHERE path = :path and site_id = :id", nativeQuery = true)
    Optional<Page> findByPathAndSiteId(@Param("path") String path, @Param("id") long id);
    @Query(value = "SELECT * from Page WHERE site_id = :id", nativeQuery = true)
    List<Page> getAllPagesBySiteId(@Param("id") long id);
}
