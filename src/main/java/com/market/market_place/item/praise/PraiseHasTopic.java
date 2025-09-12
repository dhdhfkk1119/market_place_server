package com.market.market_place.item.praise;

import com.market.market_place.item.praise_category.PraiseTopic;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "praise_has_topic_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PraiseHasTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praise_id")
    private Praise praise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praise_topic_id")
    private PraiseTopic praiseTopic;
}