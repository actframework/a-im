package chatroom.endpoint;


import act.controller.annotation.UrlContext;
import act.validation.NotBlank;
import act.ws.WebSocketConnectionManager;
import act.ws.WebSocketConnectionRegistry;
import act.xio.WebSocketConnection;
import chatroom.model.User;
import org.osgl.mvc.annotation.PostAction;

import java.util.List;

@UrlContext("rooms")
@SuppressWarnings("unused")
public class ChatRoomService extends AuthenticatedServiceBase {

    @PostAction("{roomName}")
    public void join(@NotBlank String roomName, WebSocketConnectionManager manager, User.Dao userDao) {
        userDao.join(me, roomName);
        List<WebSocketConnection> connections = manager.usernameRegistry().get(me.email);
        WebSocketConnectionRegistry registry = manager.tagRegistry();
        for (WebSocketConnection connection : connections) {
            registry.register(roomName, connection);
        }
    }

}
