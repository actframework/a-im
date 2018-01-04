package demo.aim.endpoint;

import act.app.ActionContext;
import act.db.DbBind;
import demo.aim.model.User;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.Result;

import javax.validation.constraints.NotNull;

/**
 * Handle user login logout
 */
public class LoginService extends PublicServiceBase {

    /**
     * Login a user with password.
     *
     * If user cannot be found or the password is not valid then return
     * 404 Not Found response. Otherwise a JWT token will be returned in
     * JSON format which looks like:
     *
     * ```
     * {"token": "token-content"}
     * ```
     *
     * @param user the user bind to email
     * @param password the password
     * @return jwt token in JSON format looks like `{"token": "the-token-content"}`
     */
    @PostAction("login")
    public Result login(@DbBind(field = "email") @NotNull User user, char[] password, ActionContext context) {
        notFoundIfNot(user.verifyPassword(password));
        context.login(user.email);
        return jwt();
    }

    /**
     * Logout the user.
     *
     * This will remove the user from session. However frontend must make sure
     * the existing JWT token get removed from local storage.
     *
     * This method will return 204 No Content response which should be ignored.
     *
     * @param context
     */
    @PostAction("logout")
    public void logout(ActionContext context) {
        context.logout();
    }

}
