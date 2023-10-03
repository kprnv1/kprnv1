package searchengine.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import searchengine.morphology.MorphologyAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Request {

    private static final Logger log = LogManager.getLogger();

    private final String req;
    private final List<String> reqLemmas;

    public List<String> getReqLemmas() {
        return reqLemmas;
    }

    public String getReq() {
        return req;
    }

    public Request(String req) {
        this.req = req;
        reqLemmas = new ArrayList<>();
        try {
            MorphologyAnalyzer analyzer = new MorphologyAnalyzer();
            reqLemmas.addAll(analyzer.getLemmas(req));
        } catch (Exception e) {
            log.info("ошибка морфологочиского анализа");
        }
    }
}
