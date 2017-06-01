package chatroom.endpoint;

import act.cli.Command;
import act.controller.annotation.UrlContext;
import act.util.PropertySpec;
import act.validation.NotBlank;
import act.ws.WebSocketConnectionManager;
import act.xio.WebSocketConnection;
import chatroom.model.Room;
import chatroom.model.User;
import org.osgl.mvc.annotation.DeleteAction;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Provides services for the front-end to
 * * GET /api/v1/rooms - list all rooms
 * * GET /api/v1/rooms/{roomName}/users - list all users joined in the room specified by roomName
 * * POST /api/v1/rooms/{roomName} - join the current user to the room specified by roomName
 * * DELETE /api/v1/rooms/{roomName} - quit the current user from the room specified by roomName
 *
 * Note only authenticated user can use the room services
 */
@UrlContext("rooms")
@SuppressWarnings("unused")
public class RoomService extends ServiceBase.AuthenticatedServiceBase {

    private WebSocketConnectionManager connectionManager;
    private User.Dao userDao;

    @Inject
    public RoomService(@NotNull WebSocketConnectionManager connectionManager, @NotNull User.Dao userDao) {
        this.connectionManager = connectionManager;
        this.userDao = userDao;
    }

    @GetAction
    @Command(name = "room.list", help = "list all rooms")
    public Iterable<Room> list(Room.Dao roomDao) {
        return roomDao.findAll();
    }

    @GetAction("{roomName}/users")
    @PropertySpec("email,screenname")
    public Iterable<User> listUsers(String roomName, User.Dao userDao) {
        return userDao.findBy("rooms", roomName);
    }

    @PostAction("{roomName}")
    public void join(@NotBlank String roomName) {
        userDao.joinRoom(me, roomName);
        List<WebSocketConnection> myConnections = connectionManager.usernameRegistry().get(me.email);
        connectionManager.tagRegistry().register(roomName, myConnections);
    }

    @DeleteAction("{roomName}")
    public void quit(@NotBlank String roomName) {
        userDao.quitRoom(me, roomName);
        List<WebSocketConnection> myConnections = connectionManager.usernameRegistry().get(me.email);
        connectionManager.tagRegistry().deRegister(roomName, myConnections);
    }

}
