package markit.chat.chat_room;

import markit._core._utils.DateUtil;
import markit.chat.chat_message.ChatMessage;
import markit.item.core.Item;
import markit.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_room_tb")
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_user")
    private Member loginUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_user")
    private Member otherUser;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message")
    private ChatMessage lastMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_item_id")
    private Item item; // 상품 정보

    private Long lastReadMessageIdByLoginUser;
    private Long lastReadMessageIdByOtherUser;

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
