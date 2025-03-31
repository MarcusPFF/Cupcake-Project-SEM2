package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;

public class RoutingController {
    private static CustomerMapper customerMapper = new CustomerMapper();

    public static void routes(Javalin app) {

        //Login
        app.get("/login", ctx -> showLoginPage(ctx));
        app.post("/login", ctx -> handleLogin(ctx));

        //Logout
        app.get("/logout", ctx -> handleLogout(ctx));

        //Signup
        app.get("/signup", ctx -> showSignupPage(ctx));
        app.post("/signup", ctx -> handleSignup(ctx));

        //Index
        app.get("/index", ctx -> showIndexPage(ctx));
        app.post("/index", ctx -> handleIndex(ctx));

        //cart
        app.post("/cart", ctx -> handleCart(ctx));

    }

    private static void showLoginPage(Context ctx) {
        ctx.render("/login.html");
    }

    private static void handleLogin(Context ctx) {
        int userId;
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        try {
            boolean validateUser = customerMapper.verifyUserCredentials(ConnectionPool.getInstance(), username, password);
            if (validateUser) {
                if (username.contains("@")) {
                    String email = username;
                    username = customerMapper.getCustomerNameFromEmail(ConnectionPool.getInstance(), email);
                    userId = customerMapper.getCustomerIdFromEmail(ConnectionPool.getInstance(), email);
                } else {
                    String email = customerMapper.getEmailFromCustomerName(ConnectionPool.getInstance(), username);
                    userId = customerMapper.getCustomerIdFromEmail(ConnectionPool.getInstance(), email);
                }

                ctx.sessionAttribute("username", username);
                ctx.sessionAttribute("customerId", userId);
                showIndexPage(ctx);
            } else {
                ctx.redirect("/login");
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.redirect("/login");
        }

    }

    private static void handleLogout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void showSignupPage(Context ctx) {
        ctx.render("/signup.html");
    }

    private static void showIndexPage(Context ctx) {
        String username = ctx.sessionAttribute("username");
        if (username == null) {
            username = "Guest";
        }
        ctx.render("/index.html", Map.of("username", username));
    }

    private static void handleIndex(Context ctx) {

    }

    private static void handleSignup(Context ctx) {
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        float wallet = 0;
        try {
            customerMapper.addNewCustomer(ConnectionPool.getInstance(), email, username, password, wallet);
        } catch (DatabaseException e) {
            e.printStackTrace();

        }
        ctx.sessionAttribute("username", username);
        showIndexPage(ctx);
    }

    private static void handleCart(Context ctx) {
        String bottomId = ctx.formParam("bottomId");
        String toppingId = ctx.formParam("toppingId");
        String quantityString = ctx.formParam("quantity");
        int quantity = Integer.parseInt(quantityString);

        ctx.redirect("/cart");
    }
}