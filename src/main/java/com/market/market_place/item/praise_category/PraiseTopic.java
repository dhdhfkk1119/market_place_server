package com.market.market_place.item.praise_category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "praise_topic_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PraiseTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}