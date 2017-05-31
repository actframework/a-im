package chatroom.endpoint;

import act.controller.annotation.UrlContext;
import act.event.OnEvent;
import act.ws.WebSocketConnectEvent;
import act.ws.WebSocketContext;
import chatroom.model.Message;
import chatroom.model.Room;
import chatroom.model.User;
import org.osgl.aaa.NoAuthentication;
import org.osgl.mvc.annotation.WsAction;
import org.osgl.util.C;
import org.osgl.util.S;

/**
 * The main function of this sample project: enable chat happening
 *
 * * Guests can view/send message in the main room
 * * Logged in user can view/send message in any room
 *
 */
@UrlContext("chat")
@NoAuthentication
@SuppressWarnings("unused")
public class ChatService {

    @OnEvent
    public void handleConnection(WebSocketConnectEvent event) {
        WebSocketContext context = event.source();
        context.tag(Room.MAIN);
        String username = context.username();
        if (null != username) {
            User user = userOf(context);
            if (null != user) {
                for (String roomName : user.rooms) {
                    context.tag(roomName);
                }
            }
        }
    }

    @WsAction
    public void handleMessage(Message message, WebSocketContext context) {
        String roomName = message.room;
        if (hasAccessTo(userOf(context), roomName)) {
            context.sendJsonToTagged(message, message.room, true);
        } else {
            context.sendJsonToSelf(C.map("error", "Forbidden"));
        }
    }

    private boolean hasAccessTo(User user, String roomName) {
        if (S.eq(Room.MAIN, roomName)) {
            // everyone has access to the main chat room
            return true;
        }
        if (null == user) {
            return false;
        }
        return user.rooms.contains(roomName);
    }

    private User userOf(WebSocketContext context) {
        String username = context.username();
        if (S.blank(username)) {
            return null;
        }
        User.Dao userDao = User.dao();
        return userDao.findByIdOrEmail(username);
    }

}
