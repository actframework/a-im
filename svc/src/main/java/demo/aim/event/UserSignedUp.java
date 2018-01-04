package demo.aim.event;

import act.Act;
import act.event.ActEvent;
import demo.aim.model.User;

public class UserSignedUp extends ActEvent<User> {

    public UserSignedUp(User source) {
        super(source);
    }

    public String activateToken() {
        User user = source();
        return Act.crypto().generateToken(user.email);
    }
}
