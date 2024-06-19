package com.SoT.JIN.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUsername(String username);
}
