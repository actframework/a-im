package demo.aim.mail;

import act.event.OnEvent;
import act.mail.Mailer;
import demo.aim.event.UserSignedUp;
import demo.aim.model.User;

@Mailer
public class PostOffice extends Mailer.Util {

    @OnEvent(async = true)
    public void sendWelcomeLetter(UserSignedUp signedUp) {
        User user = signedUp.source();
        to(user.email);
        String token = signedUp.activateToken();
        send(user, token);
    }

}
