package app.controllers;

import app.entities.Cupcakes;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.CustomerMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutingController {
    private static CustomerMapper customerMapper = new CustomerMapper();
    private static ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static CupcakeMapper cupcakeMapper = new CupcakeMapper();


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

        //Orders
        app.get("/orders", ctx -> showOrdersPage(ctx));

        //Cart
        app.post("/cart", ctx -> handleCart(ctx));

        //customers
        app.get("/customers", ctx -> showCustomersPage(ctx));

        //delete customer
        app.post("/customers", ctx -> handleCustomerDeletion(ctx));

    }

    public static void handleCustomerDeletion(Context ctx) {
        String customerIdString = ctx.formParam("customerId");

        try {
            int customerId = Integer.parseInt(customerIdString);
            customerMapper.removeCustomerById(connectionPool, customerId);
            showCustomersPage(ctx);

        } catch (NumberFormatException e) {
            showCustomersPage(ctx);
        } catch (DatabaseException e) {
            showCustomersPage(ctx);
        }
    }


    public static void showCustomersPage(Context ctx) {
        try {
            String username = ctx.sessionAttribute("username");
            List<Customer> customers = customerMapper.getAllCustomers(connectionPool);
            ctx.render("/customers.html", Map.of("customers", customers, "username", username));

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private static void showOrdersPage(Context ctx) {
        String username = ctx.sessionAttribute("username");
        ctx.render("/orders.html", Map.of("username", username));
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
                ctx.redirect("/login?error=wrongcredentials");
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.redirect("/login?error=somethingwentwrong404");
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
        List<Cupcakes> cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        if (username == null) {
            ctx.render("/index.html");
        } else {
            ctx.render("/index.html", Map.of("username", username, "cart", cart));

        }
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
        String username = ctx.sessionAttribute("username");
        String bottomIdString = ctx.formParam("bottomId");
        String toppingIdString = ctx.formParam("toppingId");
        String quantityString = ctx.formParam("quantity");

        int bottomId = Integer.parseInt(bottomIdString);
        int toppingId = Integer.parseInt(toppingIdString);
        int quantity = Integer.parseInt(quantityString);

        if (username == null) {
            ctx.redirect("index?error=mustbeloggedin");
        }
        if (quantity <= 0) {
            ctx.redirect("index?error=mustbeabovezero");
            return;
        }
        try {
            //Getting the top and bottom flavour from database
            String bottomFlavour = cupcakeMapper.getBottomFlavourFromBottomId(connectionPool, bottomId);
            String toppingFlavour = cupcakeMapper.getToppingFlavourFromToppingId(connectionPool, toppingId);

            //Calculating one cupcake price and multiply by quantity
            float oneCupcakePrice = cupcakeMapper.executeGetTotalCupcakePrice(connectionPool, toppingId, bottomId);
            float totalCupcakePrice = oneCupcakePrice * quantity;

            Cupcakes cupcakes = new Cupcakes(bottomFlavour, toppingFlavour, quantity, totalCupcakePrice);
            List<Cupcakes> cart = ctx.sessionAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }
            cart.add(cupcakes);
            ctx.sessionAttribute("cart", cart);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        showIndexPage(ctx);
    }
}