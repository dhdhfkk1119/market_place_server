package markit.item.item_report.entity;

import markit._core._utils.DateUtil;
import markit.item.core.Item;
import markit.item.item_report._enum.ItemReportStatus;
import markit.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "item_report_tb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ItemReportStatus status = ItemReportStatus.PENDING;

    @CreationTimestamp
    private Timestamp createdAt;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = ItemReportStatus.PENDING;
        }
    }

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
