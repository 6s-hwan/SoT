package com.SoT.JIN.rising;

import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchRepository;
import com.SoT.JIN.search.SearchService;
import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.story.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RisingService {
    private static final Logger logger = LoggerFactory.getLogger(RisingService.class);  // Logger 선언
    private final RisingRepository risingRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public RisingService(RisingRepository risingRepository, StoryRepository storyRepository) {
        this.risingRepository = risingRepository;
        this.storyRepository = storyRepository;
    }
    @Transactional
    public void updateRisingFromSearch(SearchService searchService) {
        List<Search> topSearches = searchService.getTopSearchKeywords(Integer.MAX_VALUE);

        int rank = 1;
        for (Search search : topSearches) {
            int storyCount = storyRepository.countByKeywordAcrossFields(search.getKeyword());

            if (storyCount >= 3) {
                Rising rising = risingRepository.findByKeyword(search.getKeyword()).orElse(null);

                if (rising != null) {
                    rising.setRankOrder(rank);
                } else {
                    rising = new Rising(
                            search.getKeyword(),
                            rank,
                            "Location Placeholder",
                            "https://soteulji.s3.ap-northeast-2.amazonaws.com/test/regionmain.png"
                    );
                }
                risingRepository.save(rising);
                rank++;
            }
        }
    }
    @Transactional(readOnly = true)
    public Optional<Rising> findByKeyword(String keyword) {
        return risingRepository.findByKeyword(keyword);
    }
    @Transactional
    public void resetRisingData() {
        risingRepository.deleteAll();
    }
    @Transactional(readOnly = true)
    public List<Rising> getOtherTopRisings(String excludeKeyword, int limit) {
        return risingRepository.findByKeywordNot(excludeKeyword, PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "rankOrder")));
    }

    @Transactional(readOnly = true)
    public List<Rising> getTopRisings(int limit) {
        List<Rising> risings = risingRepository.findAllByOrderByRankOrderAsc(PageRequest.of(0, limit));
        logger.info("Fetched Rising entities: " + risings.size());  // 로그 메시지 추가
        return risings;
    }
    public Story getStoryWithSmallestId(String keyword) {
        // 1개의 결과만 가져오는 Pageable 설정
        Pageable pageable = PageRequest.of(0, 1);  // 첫 번째 페이지에서 한 개의 ID만 가져옴
        List<Long> storyIds = storyRepository.findIdsByKeyword(keyword, pageable);

        if (storyIds.isEmpty()) {
            return null;
        }

        Long smallestId = storyIds.get(0);  // 가장 작은 ID 가져오기 (첫 번째 값)
        return storyRepository.findById(smallestId).orElse(null);  // 해당 스토리 조회
    }
    // 특정 키워드를 제외한 상위 6개의 Rising 키워드에 해당하는 Story 가져오기
    public Map<Rising, Story> getTopStoriesExcludingKeyword(String excludeKeyword, int limit) {
        List<Rising> topRisings = getTopRisings(limit + 1); // 제외할 키워드를 고려해 limit + 1로 가져옴

        // 제외할 키워드를 제외하고 상위 limit개의 Rising 리스트를 필터링한 후, LinkedHashMap에 저장
        return topRisings.stream()
                .filter(rising -> !rising.getKeyword().equals(excludeKeyword))
                .limit(limit)
                .collect(Collectors.toMap(
                        rising -> rising,
                        rising -> getStoryWithSmallestId(rising.getKeyword()),
                        (oldValue, newValue) -> oldValue,  // 충돌 방지 처리
                        LinkedHashMap::new  // LinkedHashMap을 사용하여 순서 보장
                ));
    }
}