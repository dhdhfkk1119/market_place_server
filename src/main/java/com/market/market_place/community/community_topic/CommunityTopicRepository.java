package com.market.market_place.community.community_topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityTopicRepository extends JpaRepository<CommunityTopic, Long> {

    Optional<CommunityTopic> findByName(String name);

}
