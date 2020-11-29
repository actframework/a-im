package demo.aim.endpoint;

import org.osgl.aaa.NoAuthentication;

/**
 * Base class for all services that does not require a
 * login credential
 */
@NoAuthentication
public class PublicServiceBase extends ServiceBase {
}
