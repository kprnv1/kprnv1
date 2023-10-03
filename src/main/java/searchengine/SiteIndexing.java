package searchengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import searchengine.model.*;
import searchengine.morphology.MorphologyAnalyzer;
import searchengine.services.*;
import searchengine.sitemap.SiteMapBuilder;

import java.util.*;

public class SiteIndexing extends Thread {
    private static final Logger log = LogManager.getLogger();
    private final Site site;
    private final SiteRepositoryService siteRepositoryService;
    private final IndexRepositoryService indexRepositoryService;
    private final PageRepositoryService pageRepositoryService;
    private final LemmaRepositoryService lemmaRepositoryService;
    private final LemmaAllRepositoryService lemmaAllRepositoryService;
    private final boolean allSite;
    private Boolean isStoppingByHuman = false;
    SiteMapBuilder builder;
    private final String url;

    public SiteIndexing(Site site,
                        SearchSettings searchSettings,
                        SiteRepositoryService siteRepositoryService,
                        IndexRepositoryService indexRepositoryService,
                        PageRepositoryService pageRepositoryService,
                        LemmaRepositoryService lemmaRepositoryService,
                        LemmaAllRepositoryService lemmaAllRepositoryService,
                        boolean allSite,
                        String url) {
        this.site = site;
        this.siteRepositoryService = siteRepositoryService;
        this.indexRepositoryService = indexRepositoryService;
        this.pageRepositoryService = pageRepositoryService;
        this.lemmaRepositoryService = lemmaRepositoryService;
        this.lemmaAllRepositoryService = lemmaAllRepositoryService;
        this.allSite = allSite;
        this.builder = new SiteMapBuilder(site.getUrl(), this.isInterrupted(), pageRepositoryService, site);
        this.url = url;
    }

    @Override
    public void run() {
        try {
            if (allSite) {
                runAllIndexing();
            } else {
                runOneSiteIndexing(this.url.equals("") ? site.getUrl() : this.url, null);
                if (!this.url.equals("")) {
                    log.info("Индексация одной страницы завершена.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void runAllIndexing() {
        isStoppingByHuman = false;
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        siteRepositoryService.save(site);
        builder.builtSiteMap();
        for (Page page : pageRepositoryService.getAllPagesBySiteId(site.getId())) {
            runOneSiteIndexing(site.getUrl() + page.getPath(), page);
        }
        lemmaAllRepositoryService.deleteLemmaAll();
        lemmaAllRepositoryService.saveLemma();
        lemmaAllRepositoryService.saveIndex();
        log.info(site.getName() + " Индексация завершена.");
    }

    public void stopBuildSiteMap() {
        isStoppingByHuman = true;
        builder.stopBuildSiteMap();
    }

    public void runOneSiteIndexing(String searchUrl, Page page) {
        if (isStoppingByHuman) {
            return;
        }
        log.info("runOneSiteIndexing: " + searchUrl);
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        siteRepositoryService.save(site);

        List<String> fieldList = getFieldList();
        try {
            String pagePath = searchUrl.replaceAll(site.getUrl(), "");
            if (pagePath.isBlank()) {
                pagePath = "/";
            }
            Optional<Page> getPage;
            if (page == null) {
                getPage = pageRepositoryService.findPageByPagePathAndSiteId(pagePath, site.getId());
            } else {
                getPage = Optional.of(page);
            }
            Page checkPage = new Page();
            if (getPage.isPresent()) {
                checkPage = getPage.get();
            }

            if (pagePath.equals("/")) {
                prepareDbToIndexing(checkPage);
            }

            TreeMap<String, Integer> map = new TreeMap<>();
            for (String name : fieldList) {
                float weight = 1.0f;
                String stringByTag = getStringByTag(name, Objects.requireNonNull(page).getContent());
                MorphologyAnalyzer analyzer = new MorphologyAnalyzer();
                TreeMap<String, Integer> tempMap = analyzer.textAnalyzer(stringByTag);
                map.putAll(tempMap);
            }

            List<LemmaAll> lemmaAllList = Collections.synchronizedList(new ArrayList<>());
            for (Map.Entry<String, Integer> lemma : map.entrySet()) {
                lemmaAllList.add(new LemmaAll(lemma.getKey(), lemma.getValue(), site.getId(), checkPage.getId()));
            }
            lemmaAllRepositoryService.saveAll(lemmaAllList);
            map.clear();
        } catch (Exception e) {
            site.setLastError(e.getMessage());
            log.info(e.getMessage());
            site.setStatus(Status.FAILED);
            e.printStackTrace();
        }
        finally {
            siteRepositoryService.save(site);
        }

        site.setStatus(Status.INDEXED);
        siteRepositoryService.save(site);
    }

    private List<String> getFieldList() {
        return Arrays.asList("title", "body");
    }

    private String getStringByTag(String tag, String html) {
        String string = "";
        Document document = Jsoup.parse(html);
        Elements elements = document.select(tag);
        StringBuilder builder = new StringBuilder();
        elements.forEach(element -> builder.append(element.text()).append(" "));
        if (!builder.isEmpty()) {
            string = builder.toString();
        }
        return string;
    }

    private void prepareDbToIndexing(Page page) {
        List<Index> indexingList = indexRepositoryService.getAllIndexingByPage(page);
        List<Lemma> allLemmasIdByIndexing = lemmaRepositoryService.findLemmasByIndexing(indexingList);
        lemmaRepositoryService.deleteAllLemmas(allLemmasIdByIndexing);
    }
}
