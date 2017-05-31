package chatroom.endpoint;

import act.aaa.LoginUser;
import chatroom.model.User;

/**
 * RESTful service base for handling authenticated requests
 */
public class AuthenticatedServiceBase extends ServiceBase {

    @LoginUser
    protected User me;

}
