package com.market.market_place.item.PraiseTopic;


import com.market.market_place.item.praise.Praise;
import com.market.market_place.item.praise_category.PraiseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "praise_topic_tb")
public class PraiseTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praise_id")
    private Praise praise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PraiseCategory praiseCategory;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
