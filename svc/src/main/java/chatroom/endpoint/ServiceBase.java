package chatroom.endpoint;

import act.controller.Controller;
import act.controller.annotation.UrlContext;

/**
 * the RESTful service base.
 *
 * Here we setup the URL context for all sub services: `/api/v1`
 */
@UrlContext("/api/v1")
public class ServiceBase extends Controller.Base {
}
