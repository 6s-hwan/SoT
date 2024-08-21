package com.SoT.JIN.search;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public SearchService(SearchRepository searchRepository, StoryRepository storyRepository) {
        this.searchRepository = searchRepository;
        this.storyRepository = storyRepository;
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
        List<Long> storyIds = storyRepository.findIdsByKeyword(keyword);
        if (storyIds.isEmpty()) {
            return null;
        }
        Long smallestId = storyIds.stream().min(Long::compare).orElse(null);
        return storyRepository.findById(smallestId).orElse(null);
    }
}
