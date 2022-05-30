package com.mas.mas5.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "admin_chat_user")
@Entity
@Getter
@Setter
public class AdminChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "chat_user_id", nullable = false)
    private ChatUser chatUser;

    @OneToMany(mappedBy = "admin", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ChatRoom> moderatedChats = new ArrayList<>();
}
