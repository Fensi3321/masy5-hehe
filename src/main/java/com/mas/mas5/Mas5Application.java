package com.mas.mas5;

import com.mas.mas5.model.AdminChatUser;
import com.mas.mas5.model.ChatRoom;
import com.mas.mas5.model.ChatUser;
import com.mas.mas5.model.Message;
import com.mas.mas5.repository.AdminChatUserRepository;
import com.mas.mas5.repository.ChatRoomRepository;
import com.mas.mas5.repository.ChatUserRepository;
import com.mas.mas5.repository.MessageRepository;
import com.mas.mas5.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootApplication
@Controller
@RequiredArgsConstructor
public class Mas5Application implements CommandLineRunner {
    private final ChatUserRepository userRepository;
    private final AdminChatUserRepository adminRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    private final MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(Mas5Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        seed();

        ChatUser user1 = userRepository.findById(1).orElseThrow(RuntimeException::new);
        ChatUser user2 = userRepository.findById(2).orElseThrow(RuntimeException::new);
        ChatRoom chatRoom = chatRoomRepository.findByTopic("news").orElseThrow(RuntimeException::new);
        AdminChatUser admin = adminRepository.findById(1).orElseThrow(RuntimeException::new);

        messageService.sendMessage(user1, chatRoom, "Hello world");
        messageService.sendMessage(user1, chatRoom, "test");
        messageService.sendMessage(user2, chatRoom, "h");

        var messages = chatRoomRepository.findByTopic("news").get().getMessages();

        for (Message message : messages) {
            System.out.println("Sender: " + message.getChatUser().getName() + ", message: " + message.getMessage());
        }

        System.out.println();
        var userMessages = user1.getMessages();

        for (Message message : userMessages) {
            System.out.println("Sender: " + message.getChatUser().getName() + ", message: " + message.getMessage());
        }

        System.out.println("Moderated by " + admin.getChatUser().getName());
        admin.getModeratedChats().forEach(x -> System.out.println(x.getTopic()));

        System.out.println("\nDeleted");

        messageService.deleteMessage(admin.getChatUser(), userMessages.get(0));

        var messages2 = chatRoomRepository.findByTopic("news").get().getMessages();

        for (Message message : messages2) {
            System.out.println("Sender: " + message.getChatUser().getName() + ", message: " + message.getMessage());
        }
    }

    @Transactional
    public void seed() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTopic("news");

        ChatUser user1 = new ChatUser();
        user1.setName("Alice");
        ChatUser user2 = new ChatUser();
        user2.setName("Bob");

        user1.getChatRooms().add(chatRoom);
        user2.getChatRooms().add(chatRoom);
        userRepository.save(user1);
        userRepository.save(user2);

        ChatUser adminUser = new ChatUser();
        adminUser.setName("Admin");
        adminUser.getChatRooms().add(chatRoom);
        adminUser = userRepository.save(adminUser);

        AdminChatUser admin = new AdminChatUser();
        admin.setChatUser(adminUser);
        admin.getModeratedChats().add(chatRoom);

        adminRepository.save(admin);

        chatRoom.getUsers().addAll(List.of(user1, user2));
        chatRoom.setAdmin(admin);

        chatRoomRepository.save(chatRoom);
    }
}
