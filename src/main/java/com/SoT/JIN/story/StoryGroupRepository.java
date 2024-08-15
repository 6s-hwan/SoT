package com.SoT.JIN.story;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryGroupRepository extends JpaRepository<StoryGroup, Long> {
    boolean existsByGroupName(String groupName);
    Optional<StoryGroup> findByGroupName(String groupName);

}