package com.market.market_place.item.praise;

import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "praise_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Praise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praiser_id")
    private Member praiser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praised_member_id")
    private Member praisedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade;

    @CreationTimestamp
    private LocalDateTime createdAt;

<<<<<<< HEAD
    private boolean isBuyer;



    public static Praise createPraise(Trade trade, Member praiser, Member praisedMember) {
        return Praise.builder()
                .trade(trade)
                .praiser(praiser)
                .praisedMember(praisedMember)
                .build();
    }
}
=======
    @Column(length = 500)
    private String content;
}
>>>>>>> f-board
