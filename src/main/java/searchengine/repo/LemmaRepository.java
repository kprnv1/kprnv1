package searchengine.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;

import javax.transaction.Transactional;
import java.util.List;

@Repository

public interface LemmaRepository extends CrudRepository<Lemma, Integer> {
    List<Lemma> findByLemma (String lemma);

    @Query(value = "SELECT * from Lemma WHERE id IN(:id)", nativeQuery = true)
    List<Lemma> findById (int[] id);

    @Query(value = "SELECT * from Lemma WHERE id IN :ids", nativeQuery = true)
    List<Lemma> findByIds (@Param("ids")int[] ids);

    @Query(value = "SELECT count(*) from Lemma where site_id = :id")
    long count(@Param("id") long id);
    @Modifying
    @Transactional
    @Query(value = "insert into lemma(frequency, lemma, site_id) " +
            "select f, l, id from (select :frequency f, :lemma l, :siteId id)t " +
            "ON DUPLICATE KEY UPDATE frequency = frequency + f;", nativeQuery = true)
    void saveLemma (@Param("lemma") String lemma, @Param("frequency") int frequency, @Param("siteId") int siteId);
    @Query(value = "SELECT id from Lemma where lemma = :lemma and site_id = :site_id")
    int findLemmaIdByNameAndSiteId(@Param("lemma") String lemma, @Param("site_id") int siteId);
}
