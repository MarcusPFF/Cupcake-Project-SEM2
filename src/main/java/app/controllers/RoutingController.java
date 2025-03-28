package app.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class RoutingController {

    public static void routes(Javalin app) {

        // Login
        app.get("/login", ctx -> showLoginPage(ctx));
        app.post("/login", ctx -> handleLogin(ctx));

        //Signup
        app.get("/signup", ctx -> showSignupPage(ctx));
        app.post("/signup", ctx -> handleSignup(ctx));

        app.get("/index", ctx -> showIndexPage(ctx));
        app.post("/index", ctx -> handleIndex(ctx));

        app.post("/cart", ctx -> handleCart(ctx));

    }

    private static void showLoginPage(Context ctx) {
        ctx.render("/login.html");
    }

    private static void handleLogin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

    }

    private static void showSignupPage(Context ctx) {
        ctx.render("/signup.html");
    }

    private static void showIndexPage(Context ctx) {
        ctx.render("/index.html");
    }

    private static void handleIndex(Context ctx) {

    }

    private static void handleSignup(Context ctx) {
        String username = ctx.formParam("username");
    }

    private static void handleCart(Context ctx) {
        String bottomId = ctx.formParam("bottomId");
        String toppingId = ctx.formParam("toppingId");
        String quantityString = ctx.formParam("quantity");
        int quantity = Integer.parseInt(quantityString);

        ctx.redirect("/cart");
    }
}