package searchengine.services;

import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

public interface PageRepositoryService {
    void save(Page page);

    Optional<Page> findPageByPageIdAndSite(int pageId, Site site);

    long pageCount();

    long pageCount(long siteId);

    Optional<Page> findPageByPagePathAndSiteId(String pagePath, int siteId);

    List<Page> getAllPagesBySiteId(int siteId);

    void deletePagesBySiteId(int siteId);
}
