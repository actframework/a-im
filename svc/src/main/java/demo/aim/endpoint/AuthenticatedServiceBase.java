package demo.aim.endpoint;

import act.aaa.LoginUser;
import demo.aim.model.User;

/**
 * The base class for service that require a login credential
 */
public class AuthenticatedServiceBase extends ServiceBase {

    /**
     * The logged in user that can be accessed by sub classes.
     *
     * This field is injected by framework
     */
    @LoginUser
    protected User me;

}
