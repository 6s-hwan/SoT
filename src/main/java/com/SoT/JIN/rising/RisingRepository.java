package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RisingRepository extends JpaRepository<Rising, Long> {
    Optional<Rising> findByKeyword(String keyword);
    // 특정 키워드를 제외하고 상위 N개의 데이터를 가져오는 메서드 정의
    List<Rising> findByKeywordNot(String keyword, Pageable pageable);
}
