package com.SoT.JIN.search;

import com.SoT.JIN.rising.RisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final RisingService risingService;
    private final SearchService searchService;

    @Autowired
    public ScheduledTask(RisingService risingService, SearchService searchService) {
        this.risingService = risingService;
        this.searchService = searchService;
    }
    // 매주 월요일 00:00시에 Search 및 Rising 데이터를 초기화하고 업데이트하는 스케줄러
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetAndUpdateWeekly() {
        // 1. 검색 횟수 초기화
        searchService.resetSearchCounts();  // 1등부터 8등까지 검색 횟수 초기화

        // 3. Rising 데이터를 SearchService 기반으로 업데이트
        risingService.updateRisingFromSearch(searchService); // SearchService 기반으로 Rising 데이터 업데이트
    }
}
