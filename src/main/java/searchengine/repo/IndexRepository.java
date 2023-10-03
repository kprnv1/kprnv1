package searchengine.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.*;

import java.util.List;

@Repository
public interface IndexRepository extends CrudRepository<Index, Integer> {
    List<Index> findByLemma(Lemma lemma);

    List<Index> findByPage(Page page);

    Index findByLemmaAndPage(Lemma lemma, Page page);
}
