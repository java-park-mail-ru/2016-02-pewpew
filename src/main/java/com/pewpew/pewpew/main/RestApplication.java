package com.pewpew.pewpew.main;

import com.pewpew.pewpew.rest.ScoreboardService;
import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.lang.annotation.Annotation;

@ApplicationPath("api")
public class RestApplication extends ResourceConfig {


    public RestApplication() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        register(EntityFilteringFeature.class);
        register(new MoxyJsonConfig().setFormattedOutput(true).resolver());

        final AccountService accountService = new AccountService();
        final SessionService sessionService = new SessionService(accountService);
        final UserService userService = new UserService(accountService);
        final ScoreboardService scoreboardService = new ScoreboardService();


        register(sessionService);
        register(userService);
        register(scoreboardService);
    }
}