package searchengine.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaAll;

import javax.transaction.Transactional;

@Repository

public interface LemmaAllRepository extends CrudRepository<LemmaAll, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into lemma (frequency, lemma, site_id)\n" +
            "select sum(frequency), lemma, site_id\n" +
            "from search_engine.lemma_all\n" +
            "group by lemma, site_id;\n"
            , nativeQuery = true)
    void saveLemma();

    @Modifying
    @Transactional
    @Query(value = "insert into `index` (`rank`, lemma_id, page_id)\n" +
            "select a.frequency, l.id, a.page_id\n" +
            "  from search_engine.lemma_all a \n" +
            "  join search_engine.lemma l\n" +
            "    on l.lemma = a.lemma\n" +
            "   and l.site_id = a.site_id;\n"
            , nativeQuery = true)
    void saveIndex();
}
