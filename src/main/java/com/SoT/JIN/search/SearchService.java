package com.SoT.JIN.search;

import com.SoT.JIN.rising.RisingService;
import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final SearchRepository searchRepository;
    private final StoryRepository storyRepository;
    private final RisingService risingService; // RisingService 추가

    @Autowired
    public SearchService(SearchRepository searchRepository, StoryRepository storyRepository, RisingService risingService) {
        this.searchRepository = searchRepository;
        this.storyRepository = storyRepository;
        this.risingService = risingService; // RisingService 주입
    }

    @Transactional
    public void saveSearchKeyword(String keyword) {
        Optional<Search> existingSearch = searchRepository.findByKeyword(keyword);
        if (existingSearch.isPresent()) {
            Search search = existingSearch.get();
            search.setCount(search.getCount() + 1);
            searchRepository.save(search);
        } else {
            Search newSearch = new Search();
            newSearch.setKeyword(keyword);
            newSearch.setCount(1L);
            searchRepository.save(newSearch);
        }

        // 검색어 저장 후 Rising 업데이트
        risingService.updateRisingFromSearch(this);  // 현재 인스턴스를 전달
    }

    @Transactional
    public void resetSearchCounts() {
        // 상위 8개의 검색어를 가져오기
        List<Search> topSearches = searchRepository.findTop8ByOrderByCountDesc();

        // 상위 8개를 제외한 나머지 검색어들을 모두 삭제
        List<Search> allSearches = searchRepository.findAll();
        for (Search search : allSearches) {
            if (!topSearches.contains(search)) {
                searchRepository.delete(search);
            }
        }

        // 상위 8개의 검색어의 count를 1로 설정
        for (Search search : topSearches) {
            search.setCount(1L); // 여기서 삭제하는 대신 count 값을 1로 설정
            searchRepository.save(search);
        }
    }

    public List<Search> getTopSearchKeywords(int limit) {
        return searchRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Search::getCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Story getStoryWithSmallestId(String keyword) {
        Pageable pageable = PageRequest.of(0, 1); // 1개의 결과만 가져옴
        List<Long> largestStoryIds = storyRepository.findIdsByKeyword(keyword, pageable);

        if (largestStoryIds.isEmpty()) {
            return null;
        }

        Long largestId = largestStoryIds.get(0);
        return storyRepository.findById(largestId).orElse(null);
    }
}