package com.mas.mas5.repository;

import com.mas.mas5.model.AdminChatUser;
import com.mas.mas5.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminChatUserRepository extends JpaRepository<AdminChatUser, Integer> {
    Optional<AdminChatUser> findByChatUser(ChatUser user);
}