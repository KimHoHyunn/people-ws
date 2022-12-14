package com.people.ws.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.ws.service.ChatService;
import com.people.ws.vo.ChatMessage;
import com.people.ws.vo.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 테스트는 chrome 웹스토어에서 websocket test를 찾아서 확장프로그램으로 추가하여 
 * 테스트
 * @author mh042
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler{

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();
	
    /**
     * 사용자가(브라우저) 웹소켓 서버에 붙게되면 동작하는 메소드 입니다.
     * 이때 WebSocketSession 값이 생성이 되는데 해당 값을 static 변수인 CLIENTS 객체에 
     * put 메소드를 활용하여 고유 아이디값을 키로 하고 세션을 값으로 하여 넣어주었습니다.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
    }
    
    /**
     * 접속이 끊어진 사용자가 발생하면 호출되는 메소드 입니다.
     * 해당 메소드를 활용하여 CLIENTS 객체에서 접속이 끊어진 아이디 값을 제거하도록 하면 코드는 완성 입니다.
     * 간단한 확장 프로그램을 크롬, 파이어폭스를 통하여 테스트를 해 봅니다.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove(session.getId());
    }
    

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    
    /**
     * 
     * 사용자의 메시지를 받게되면 동작하는 메소드입니다.
     * 구글링을 하면 해당 메소드에서 session객체에 바로 sendMessage를 보내는 코드가 많습니다.
     * 이러한 경우에는 자기 자신한테만 응답하는 에코(echo) 기능만 동작하게 됩니다.
     * 
     * 하지만 afterConnectionEstablished 메소드에서 접속한 사용자를 CLIENTS 객체에 담아주었으므로 
     * 해당 변수에는 접속한 세션의 값들이 전부 보관되고 있습니다.
     * handleTextMessage 메소드에서 CLIENTS 객체에 담긴 세션값들을 가져와서 반복문을 통해 위 처럼 메시지를 발송 해 주면, 
     * 자기자신 이외의 사용자에게 메시지를 보낼 수 있는 코드가 완성이 되게 됩니다.
     * 
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	String payload = message.getPayload();
    	
    	/**
    	 * 웹소켓클라이언트로부터 채팅메세지를 전달 받아 채팅 메세지 객체로 변환
    	 * 전달바은 메세지에 담긴 채팅방 ID로 발송대상 채팅방 정보 조회
    	 * 해당 채팅방에 입장해 있는 모든 클라이언트(WebSocket Session)에게 타입에 따른 메세지 발송
    	 */
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    	
    	
    	
    	/**
    	
    	
        log.info("payload {}", payload);
        String id = session.getId();  //메시지를 보낸 아이디
        log.info("id {}", id);
        CLIENTS.entrySet().forEach( arg->{
            if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        */
    }

}