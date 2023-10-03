package searchengine.sitemap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.services.PageRepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.RecursiveTask;

public class ParseUrl extends RecursiveTask<String> {
    public final static List<String> urlList = new Vector<>();
    private static final Logger log = LogManager.getLogger();
    private final PageRepositoryService pageRepositoryService;
    private final String url;
    private final Boolean isInterrupted;
    private final String baseUrl;
    private final Site site;

    public ParseUrl(String url, boolean isInterrupted, PageRepositoryService pageRepositoryService, String baseUrl, Site site) {
        this.url = url;
        this.isInterrupted = isInterrupted;
        this.pageRepositoryService = pageRepositoryService;
        this.baseUrl = baseUrl;
        this.site = site;
    }

    public static void clearUrlList() {
        urlList.clear();
    }

    @Override
    protected String compute() {
        if (isInterrupted) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(url);
        try {
            Thread.sleep(200);
            Connection.Response response = getResponseByUrl(url);
            Page page = new Page();
            page.setCode(response.statusCode());
            page.setPath(url.replaceAll(baseUrl, ""));
            page.setContent(response.body());
            page.setSiteId(site.getId());
            pageRepositoryService.save(page);
            Document doc = response.parse();
            Elements rootElements = doc.select("a");

            List<ParseUrl> linkGrabers = new ArrayList<>();
            rootElements.forEach(element -> {
                String link = element.attr("abs:href");
                if (link.startsWith(element.baseUri())
                        && !link.equals(element.baseUri())
                        && !link.contains("#")
                        && !link.contains(".pdf")
                        && !urlList.contains(link)
                ) {
                    urlList.add(link);
                    ParseUrl linkGraber = new ParseUrl(link, false, pageRepositoryService, baseUrl, site);
                    linkGraber.fork();
                    linkGrabers.add(linkGraber);
                }
            });

            for (ParseUrl lg : linkGrabers) {
                String text = lg.join();
                if (!text.equals("")) {
                    result.append("\n");
                    result.append(text);
                }
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Ошибка парсинга сайта: " + url);
        }
        return result.toString();
    }

    protected Connection.Response getResponseByUrl(String url) throws InterruptedException, IOException {
        log.info(" getResponseByUrl[" + urlList.size() + "]: " + url);
        Thread.sleep(200);
        return Jsoup.connect(url)
                .maxBodySize(0)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .execute();
    }

}
