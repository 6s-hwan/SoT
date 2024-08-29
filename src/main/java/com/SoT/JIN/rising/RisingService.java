package com.SoT.JIN.rising;

import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchRepository;
import com.SoT.JIN.search.SearchService;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RisingService {

    private final RisingRepository risingRepository;
    private final StoryRepository storyRepository; // StoryRepository 필드 추가

    @Autowired
    public RisingService(RisingRepository risingRepository, StoryRepository storyRepository) {
        this.risingRepository = risingRepository;
        this.storyRepository = storyRepository; // 생성자에서 StoryRepository 주입
    }
    @Transactional
    public void updateRisingFromSearch(SearchService searchService) {
        List<Search> topSearches = searchService.getTopSearchKeywords(Integer.MAX_VALUE);

        int rank = 1;
        for (Search search : topSearches) {
            int storyCount = storyRepository.countByKeywordAcrossFields(search.getKeyword());

            // 스토리가 3개 이상인 경우에만 Rising 엔티티 생성
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
    public void resetRisingData() {
        // 모든 Rising 데이터를 삭제하여 초기화
        risingRepository.deleteAll();
    }
    @Transactional(readOnly = true)
    public List<Rising> getOtherTopRisings(String excludeKeyword, int limit) {
        // 키워드를 제외한 상위 limit개의 Rising 엔티티를 가져옴
        return risingRepository.findByKeywordNot(excludeKeyword, PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "rankOrder")));
    }
}
