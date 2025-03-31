package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
//import app.controllers.RoutingController;
import java.util.logging.Logger;


public class Main {

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(8070);

        // Routing
        app.get("/", ctx -> ctx.render("signup.html"));
        //RoutingController.routes(app);
    }
}



