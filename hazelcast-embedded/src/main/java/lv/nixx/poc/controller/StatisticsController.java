package lv.nixx.poc.controller;

import lv.nixx.poc.model.Statistics;
import lv.nixx.poc.model.TxnStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final TxnStatistic stat;

    @Autowired
    public StatisticsController(TxnStatistic stat) {
        this.stat = stat;
    }

    @GetMapping("/statistics/word/{word}")
    public Statistics calculate(@PathVariable String word) {
        return stat.calculate(word);
    }

}