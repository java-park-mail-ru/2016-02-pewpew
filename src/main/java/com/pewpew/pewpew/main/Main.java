package com.pewpew.pewpew.main;

import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.rest.ScoreboardService;
import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import com.pewpew.pewpew.websoket.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class Main {
    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        String staticPath = "";
        if (args.length == 2) {
            port = Integer.valueOf(args[0]);
            staticPath = String.valueOf(args[1]);
        } else {
            System.err.println("Specify port");
            System.exit(1);
        }

        final Server server = new Server();
        final ServerConnector connector = new ServerConnector(server);
        connector.setHost(Settings.SERVER_HOST);
        connector.setPort(port);
        server.addConnector(connector);

        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        final Context context = new Context();
        try {
            context.put(AccountService.class, new AccountServiceImpl());
            final ResourceConfig config = new ResourceConfig(SessionService.class,
                    UserService.class, ScoreboardService.class, GsonMessageBodyHandler.class);
            config.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(context);
                }
            });

            final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

            contextHandler.addServlet(servletHolder, "/*");

            final WebSocketService webSocketService = new WebSocketServiceImpl();
            final GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService);
            contextHandler.addServlet(new ServletHolder(new GameSocketServelet(
                    webSocketService, gameMechanics)), "/ws");


            final ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase(staticPath);

            final HandlerCollection handlerCollection = new HandlerCollection();
            handlerCollection.setHandlers(new Handler[]{resourceHandler,
                    contextHandler, new DefaultHandler()});

            server.setHandler(handlerCollection);



            server.start();
            gameMechanics.run();
//            server.join();

        } catch (InterruptedException e) {
            System.err.println("Database error");
            System.exit(1);
        }
    }
}


