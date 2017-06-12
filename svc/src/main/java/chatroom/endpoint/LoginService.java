package chatroom.endpoint;

import act.app.conf.AutoConfig;
import act.event.OnEvent;
import act.social.SocialProfile;
import chatroom.model.User;
import org.osgl.$;
import org.osgl.aaa.NoAuthentication;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.Result;
import org.osgl.util.Const;

import javax.inject.Inject;

/**
 * Process login/logout requests:
 *
 * * Provides social login callback
 * * POST /api/v1/login - login user
 *      - param username
 *      - param password
 * * POST /api/v1/logout - logout current user
 *
 */
@NoAuthentication
@AutoConfig
@SuppressWarnings("unused")
public class LoginService extends ServiceBase {

    private static final Const<String> URL_LOGIN = $.constant();
    private static final Const<String> URL_HOME = $.constant();

    @Inject
    private User.Dao userDao;

    /**
     * Handle a social login
     * @param profileFetchedEvent a social profile
     */
    @OnEvent
    public Result handleSocialLogin(SocialProfile.Fetched profileFetchedEvent) {
        SocialProfile profile = profileFetchedEvent.source();
        String email = profile.getEmail();
        User user = userDao.findByIdOrEmail(email);
        if (null == user) {
            user = new User(profile);
            userDao.save(user);
        } else {
            userDao.linkSocialProfile(user, profile);
        }
        context.login(user.email);
        return redirect(URL_HOME.get());
    }

    /**
     * Log a user in and run any actions
     * @param email user's email address
     * @param password user's password
     */
    @PostAction("login")
    public final void login(String email, String password) {
        User user = userDao.authenticate(email, password);
        unauthorizedIf(null == user);
        context.login(user.email);
    }

    /**
     * logout the current user
     */
    @PostAction("logout")
    public final void logout() {
        context.logout();
    }

}
