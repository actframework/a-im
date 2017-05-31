package chatroom.endpoint;

import act.controller.annotation.UrlContext;
import chatroom.model.User;
import org.osgl.mvc.annotation.GetAction;

@UrlContext("users")
@SuppressWarnings("unused")
public class UserService extends AuthenticatedServiceBase {

    private User.Dao userDao;

    @GetAction
    public Iterable<User> list(String roomName) {
        return userDao.findBy("rooms", roomName);
    }

}
