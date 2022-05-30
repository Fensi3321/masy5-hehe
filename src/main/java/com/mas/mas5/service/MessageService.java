package com.mas.mas5.service;

import com.mas.mas5.model.AdminChatUser;
import com.mas.mas5.model.ChatRoom;
import com.mas.mas5.model.ChatUser;
import com.mas.mas5.model.Message;
import com.mas.mas5.repository.AdminChatUserRepository;
import com.mas.mas5.repository.ChatRoomRepository;
import com.mas.mas5.repository.ChatUserRepository;
import com.mas.mas5.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AdminChatUserRepository adminChatUserRepository;
    private final ChatUserRepository userRepository;

    public Message sendMessage(ChatUser sender, ChatRoom room, String message) {
        Message msg = Message.builder()
                .chatRoom(room)
                .chatUser(sender)
                .message(message)
                .build();

        sender.getMessages().add(msg);
        room.getMessages().add(msg);

        chatRoomRepository.save(room);
        userRepository.save(sender);

        return messageRepository.save(msg);
    }

    public void deleteMessage(ChatUser adminUser, Message msg) {
        AdminChatUser admin =  adminChatUserRepository.findByChatUser(adminUser).orElseThrow(() -> new RuntimeException("This user is not an admin"));
        List<ChatRoom> moderatedChats = admin.getModeratedChats();
        ChatRoom msgChat = msg.getChatRoom();

        boolean moderates = (moderatedChats.stream()
                .map(ChatRoom::getTopic)
                .filter(x -> x.equals(msgChat.getTopic()))
                .toList().size()) == 1;

        if (!moderates) {
            throw new RuntimeException("This admin doesnt moderate this chat");
        }

        messageRepository.delete(msg);
    }

}
