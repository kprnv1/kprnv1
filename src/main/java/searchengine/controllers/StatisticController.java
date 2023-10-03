package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import searchengine.services.StatisticsService;
import searchengine.services.responses.StatisticResponseService;

@Controller
public class StatisticController {

    private final StatisticsService statistic;

    public StatisticController(StatisticsService statistic) {
        this.statistic = statistic;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistics() {
        StatisticResponseService stat = statistic.getStatistics();
        return ResponseEntity.ok(stat);
    }
}
