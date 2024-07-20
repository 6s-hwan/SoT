package com.SoT.JIN.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final SearchService searchService;

    @Autowired
    public ScheduledTask(SearchService searchService) {
        this.searchService = searchService;
    }

    // 매일 자정에 실행되는 스케줄러 (CRON 표현식: 초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 0 * * *")
    public void resetSearchCountsDaily() {
        searchService.resetSearchCounts();
    }
}
