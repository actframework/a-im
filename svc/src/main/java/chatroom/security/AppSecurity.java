package chatroom.security;

import act.aaa.ActAAAService;
import act.util.Stateless;
import chatroom.model.User;


/**
 * Plugin the aaa security framework
 */
@Stateless
public final class AppSecurity extends ActAAAService.Base<User> implements AppPrivileges {

    @Override
    protected String username(User user) {
        return user.email;
    }

    @Override
    protected Integer privilegeOf(User user) {
        return user.privilege;
    }

    @Override
    protected boolean verifyPassword(User user, char[] chars) {
        return user.verifyPassword(new String(chars));
    }

}
