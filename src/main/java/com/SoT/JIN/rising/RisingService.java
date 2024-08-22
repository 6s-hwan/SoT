package com.SoT.JIN.rising;

import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchRepository;
import com.SoT.JIN.search.SearchService;
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

    // SearchService 의존성을 제거하고 필요 시 메서드 인자로 전달받도록 변경
    @Autowired
    public RisingService(RisingRepository risingRepository) {
        this.risingRepository = risingRepository;
    }

    @Transactional
    public void updateRisingFromSearch(SearchService searchService) {
        List<Search> topSearches = searchService.getTopSearchKeywords(Integer.MAX_VALUE);

        int rank = 1;
        for (Search search : topSearches) {
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
