package com.SoT.JIN.search;

import com.SoT.JIN.rising.RisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class ScheduledTask {

    private final RisingService risingService;
    private final SearchService searchService;

    @Autowired
    public ScheduledTask(RisingService risingService, SearchService searchService) {
        this.risingService = risingService;
        this.searchService = searchService;
    }

    // 매주 월요일 00:00시에 실행되는 스케줄러
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetRisingKeywordsWeekly() {
        searchService.resetSearchCounts();  // 1등부터 8등까지 검색 횟수 초기화
    }

    // 매주 월요일 00:00시에 실행되는 스케줄러
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetRisingWeekly() {
        risingService.resetRisingData(); // 모든 Rising 데이터를 초기화
    }
}
