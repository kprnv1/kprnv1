package searchengine.services;

import searchengine.model.LemmaAll;

public interface LemmaAllRepositoryService {
    void saveAll(Iterable<LemmaAll> entities);

    void saveLemma();

    void saveIndex();

    void deleteLemmaAll();
}
