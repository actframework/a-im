package chatroom.controller;

import act.controller.annotation.TemplateContext;
import org.osgl.aaa.NoAuthentication;
import org.osgl.mvc.annotation.GetAction;

@NoAuthentication
@TemplateContext("/login")
public class LoginController {

    @GetAction("/login")
    public void loginForm() {}

}
