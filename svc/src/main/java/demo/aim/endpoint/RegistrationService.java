package demo.aim.endpoint;

import act.controller.annotation.UrlContext;
import act.crypto.AppCrypto;
import act.db.DbBind;
import act.event.EventBus;
import demo.aim.event.UserSignedUp;
import demo.aim.model.User;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.util.Token;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The user registration service manage user sign up process.
 *
 * The sign up process:
 * 1. user fills the sign up form and submit to the server
 * 2. the user validate the data and send out welcome letter if all good
 * 3. the user received the welcome letter and click on the activate link
 * 4. frontend takes the link and pass token to the server to get user data
 * 5. upon retrieving user data frontend display activate form to allow user type in password
 * 6. frontend send password to the server to activate user
 * 7. server respond 204 back to frontend once user activated
 */
@UrlContext("registration")
public class RegistrationService extends PublicServiceBase {

    @Inject
    private User.Dao userDao;

    @Inject
    private EventBus eventBus;

    @Inject
    private AppCrypto crypto;

    /**
     * Sign up a user
     *
     * @param user the user info
     */
    @PostAction
    public void signUp(@Valid User user) {
        badRequestIf(userDao.exists(user.email), "user already exists");
        eventBus.trigger(new UserSignedUp(user));
    }

    /**
     * Verify the token and return associated user.
     *
     * @param tk the activate token
     * @return the user associated with `tk`
     * @see demo.aim.mail.PostOffice#sendWelcomeLetter(UserSignedUp)
     */
    @GetAction({"user", "user/tk={tk}"})
    public User getUserByToken(@NotNull String tk) {
        Token token = crypto.parseToken(tk);
        notFoundIfNot(token.isValid());
        return userDao.findByEmail(token.id());
    }

    /**
     * Activate the user.
     *
     * This accept user's password and activate the user.
     * @param user the user serialized by id
     * @param password the password to be set on the user
     */
    @PostAction("activate/{user}")
    public void activate(@DbBind @NotNull User user, char[] password) {
        userDao.activate(user, password);
    }

}
