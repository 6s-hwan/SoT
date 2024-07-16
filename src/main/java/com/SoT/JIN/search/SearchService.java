package com.SoT.JIN.search;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        searchRepository.deleteAll();
    }

}
