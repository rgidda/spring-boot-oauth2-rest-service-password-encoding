package io.github.pivopil.rest.handlers;

import io.github.pivopil.rest.constants.WS_API;
import io.github.pivopil.share.entities.ActiveWebSocketUser;
import io.github.pivopil.share.persistence.ActiveWebSocketUserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;

public class WebSocketConnectHandler<S>
        implements ApplicationListener<SessionConnectEvent> {

    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate,
                                   ActiveWebSocketUserRepository repository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    public void onApplicationEvent(SessionConnectEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        Principal user = SimpMessageHeaderAccessor.getUser(headers);
        if (user == null) {
            return;
        }
        String id = SimpMessageHeaderAccessor.getSessionId(headers);
        this.repository.save(
                new ActiveWebSocketUser(id, user.getName(), Calendar.getInstance()));
        this.messagingTemplate.convertAndSend(WS_API.TOPIC_FRIENDS_SIGNIN,
                Arrays.asList(user.getName()));
    }
}
