package com.people.ws.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.people.ws.service.ChatService;
import com.people.ws.vo.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/people/chatroom/create")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
    
    /**
     * postman으로 채팅방을 생성하고
	 * {
	 *    "roomId": "a4a7d613-93ba-4128-a5d8-dda3223a7805",
	 *    "name": "people",
	 *    "sessions": []
	 * }
	 *
	 * 생성된 채팅방 ID로 대화는 json으로 send
	 * {
	 *  "type":"ENTER",
	 *  "roomId": "a4a7d613-93ba-4128-a5d8-dda3223a7805",
	 *  "sender":"peopleTester",
	 *  "message":"입장"
	 * }
     */
}