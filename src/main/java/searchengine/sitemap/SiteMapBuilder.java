package searchengine.sitemap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import searchengine.model.Site;
import searchengine.services.PageRepositoryService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class SiteMapBuilder {
    private static final Logger log = LogManager.getLogger();

    private final String url;
    private final Boolean isInterrupted;
    private List<String> siteMap;
    private final PageRepositoryService pageRepositoryService;
    private final Site site;

    public ForkJoinPool pool = new ForkJoinPool();

    public SiteMapBuilder(String url, Boolean isInterrupted, PageRepositoryService pageRepositoryService, Site site) {
        this.url = url;
        this.isInterrupted = isInterrupted;
        this.pageRepositoryService = pageRepositoryService;
        this.site = site;
    }

    public void builtSiteMap() {
        pool = new ForkJoinPool();
        ParseUrl.clearUrlList();
        String text = pool.invoke(new ParseUrl(url, isInterrupted, pageRepositoryService, url, site));
        siteMap = stringToList(text);
    }

    public void stopBuildSiteMap() {
        log.info("ForkJoinPool stopping initiated ...");
        pool.shutdownNow();
        try {
            pool.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("ForkJoinPool: Ошибка закрытия потоков: " + e);
        }
    }

    private List<String> stringToList(String text) {
        return Arrays.stream(text.split("\n")).collect(Collectors.toList());
    }

    public List<String> getSiteMap() {
        return siteMap;
    }
}
