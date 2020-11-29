package demo.aim.security;

import act.aaa.ActAAAService;
import demo.aim.model.User;

public class SecurityAdapter extends ActAAAService.Base<User> {
    @Override
    protected boolean verifyPassword(User user, char[] password) {
        return user.verifyPassword(password);
    }
}
