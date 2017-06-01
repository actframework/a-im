package chatroom.endpoint;

import act.controller.annotation.UrlContext;
import act.event.OnEvent;
import act.ws.WebSocketConnectEvent;
import act.ws.WebSocketContext;
import chatroom.model.Message;
import chatroom.model.Room;
import chatroom.model.User;
import org.osgl.$;
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

    @WsAction
    public void handleMessage(Message message, WebSocketContext context) {
        String roomName = message.room;
        if (hasAccessTo(userOf(context), roomName)) {
            context.sendJsonToTagged(message, message.room, true);
        } else {
            context.sendJsonToSelf(C.map("error", "Forbidden"));
        }
    }

    @OnEvent
    public static void handleConnection(WebSocketConnectEvent event) {
        final WebSocketContext context = event.source();
        context.tag(Room.MAIN);
        String username = context.username();
        if (S.notBlank(username)) {
            userOf(context).runWith((user) -> {user.rooms.forEach(context::tag); return null;});
        }
    }

    private static boolean hasAccessTo($.Option<User> user, final String roomName) {
        return Room.isMain(roomName) || user.isDefined() && user.get().rooms.contains(roomName);
    }

    private static $.Option<User> userOf(WebSocketContext context) {
        String username = context.username();
        if (S.blank(username)) {
            return $.none();
        }
        User.Dao userDao = User.dao();
        return $.some(userDao.findByIdOrEmail(username));
    }

}
