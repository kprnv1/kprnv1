package searchengine.services;

import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

public interface SiteRepositoryService {
    Optional<Site> getSite (String url);
    Site getSite (int siteId);
    void save(Site site);
    long siteCount();
    List<Site> getAllSites();
}
