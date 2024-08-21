package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RisingRepository extends JpaRepository<Rising, Long> {
    Optional<Rising> findByKeyword(String keyword);
}
