package com.SoT.JIN.rising;

import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchRepository;
import com.SoT.JIN.search.SearchService;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.story.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
}