package chatroom.endpoint;

import act.aaa.LoginUser;
import act.controller.Controller;
import act.controller.annotation.UrlContext;
import chatroom.model.User;

/**
 * the RESTful service base.
 *
 * Here we setup the URL context for all sub services: `/api/v1`
 */
@UrlContext("/api/v1")
public class ServiceBase extends Controller.Base {

    /**
     * RESTful service base for handling authenticated requests
     */
    public static class AuthenticatedServiceBase extends ServiceBase {
        @LoginUser
        protected User me;
    }

}
