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
    private final SearchService searchService;
    private final SearchRepository searchRepository;

    @Autowired
    public RisingService(RisingRepository risingRepository, SearchService searchService, SearchRepository searchRepository) {
        this.risingRepository = risingRepository;
        this.searchService = searchService;
        this.searchRepository = searchRepository;
    }

    public List<Rising> getAllRising() {
        return risingRepository.findAll();
    }

    @Transactional
    public void resetRisingData() {
        // 모든 Rising 데이터를 삭제하여 초기화
        risingRepository.deleteAll();
    }


    @Transactional
    public void updateRisingFromSearch() {
        // 모든 검색어를 가져옴
        List<Search> topSearches = searchService.getTopSearchKeywords(Integer.MAX_VALUE);

        int rank = 1;
        for (Search search : topSearches) {
            Rising rising = risingRepository.findByKeyword(search.getKeyword()).orElse(null);

            if (rising != null) {
                // 기존 데이터가 있으면 업데이트 및 활성화
                rising.setRankOrder(rank);
            } else {
                // 기존 데이터가 없으면 새로 생성
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
}
