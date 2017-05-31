package chatroom.endpoint;

import act.Act;
import act.app.conf.AutoConfig;
import act.event.OnEvent;
import act.social.SocialProfile;
import act.util.Stateless;
import chatroom.model.User;
import chatroom.util.CacheCabin;
import org.osgl.$;
import org.osgl.aaa.NoAuthentication;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.Result;
import org.osgl.util.C;
import org.osgl.util.Const;
import org.osgl.util.Token;
import org.rythmengine.Rythm;

import javax.inject.Inject;

/**
 * Process login/logout requests
 */
@Stateless
@NoAuthentication
@AutoConfig
@SuppressWarnings("unused")
public class LoginService extends ServiceBase {

    private static final Const<String> URL_REGISTRATION = $.constant();
    private static final Const<String> URL_LOGIN = $.constant();
    private static final Const<String> URL_HOME = $.constant();

    @Inject
    private CacheCabin cabin;

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
            return redirect(buildRegistrationUrl(profile));
        } else {
            userDao.linkSocialProfile(user, profile);
            context.login(user.email);
            return redirect(URL_HOME.get());
        }
    }

    private String buildRegistrationUrl(SocialProfile profile) {
        String cacheKey = cabin.deposit(profile);
        String registrationPattern = URL_REGISTRATION.get();
        return Rythm.renderStr(registrationPattern, C.map(
                "email", profile.getEmail(),
                "firstName", profile.getFirstName(),
                "lastName", profile.getLastName(),
                "social", Act.crypto().generateToken(Token.Life.ONE_HOUR, cacheKey)));
    }

    /**
     * Log a user in and run any actions
     * @param email user's email address
     * @param password user's password
     */
    @PostAction("login")
    public final void login(
            String email,
            String password
    ) {
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
