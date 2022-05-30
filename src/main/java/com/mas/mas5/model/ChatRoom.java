package com.mas.mas5.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@Transactional
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String topic;

    @ManyToMany
    @JoinTable(name = "chat_room_chat_users",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_users_id"))
    private List<ChatUser> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "admin_chat_user_id")
    private AdminChatUser admin;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Message> messages;

}
