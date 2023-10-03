package searchengine.services.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import searchengine.model.LemmaAll;
import searchengine.repo.LemmaAllRepository;
import searchengine.services.LemmaAllRepositoryService;

@Service
public class LemmaAllRepoServiceImpl implements LemmaAllRepositoryService {

    private static final Logger log = LogManager.getLogger();

    private final LemmaAllRepository lemmaAllRepository;

    public LemmaAllRepoServiceImpl(LemmaAllRepository lemmaAllRepository) {
        this.lemmaAllRepository = lemmaAllRepository;
    }

    @Override
    public synchronized void saveAll(Iterable<LemmaAll> entities) {
        lemmaAllRepository.saveAll(entities);
    }

    @Override
    public void saveLemma() {
        lemmaAllRepository.saveLemma();
    }
    @Override
    public void saveIndex() {
        lemmaAllRepository.saveIndex();
    }
    @Override
    public void deleteLemmaAll() {
        lemmaAllRepository.deleteAll();
    }
}
