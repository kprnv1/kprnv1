package searchengine.services.implementation;

import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repo.PageRepository;
import searchengine.services.PageRepositoryService;

import java.util.List;
import java.util.Optional;

@Service
public class PageRepoServiceImpl implements PageRepositoryService {

    private final PageRepository pageRepository;

    public PageRepoServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    public synchronized void save(Page page) {
        pageRepository.save(page);
    }

    @Override
    public Optional<Page> findPageByPageIdAndSite(int pageId, Site site) {
        return pageRepository.findByIdAndSite(pageId, site);
    }

    @Override
    public long pageCount() {
        return pageRepository.count();
    }

    @Override
    public long pageCount(long siteId) {
        return pageRepository.count(siteId);
    }

    @Override
    public Optional<Page> findPageByPagePathAndSiteId(String pagePath, int siteId) {
        return pageRepository.findByPathAndSiteId(pagePath, siteId);
    }
    @Override
    public List<Page> getAllPagesBySiteId(int siteId) {
        return pageRepository.getAllPagesBySiteId(siteId);
    }
    @Override
    public synchronized void deletePagesBySiteId(int siteId) {
        pageRepository.deleteAll(getAllPagesBySiteId(siteId));
    }
}
