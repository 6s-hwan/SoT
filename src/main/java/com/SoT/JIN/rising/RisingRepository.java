package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RisingRepository extends JpaRepository<Rising, Long> {
    Optional<Rising> findByKeyword(String keyword);

    List<Rising> findByKeywordNot(String keyword, Pageable pageable);

    // Top limit 개수의 Rising 엔티티를 rankOrder 순으로 가져옴
    List<Rising> findAllByOrderByRankOrderAsc(Pageable pageable);
}
