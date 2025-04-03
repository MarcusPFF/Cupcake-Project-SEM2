package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutingController {
    private static CustomerMapper customerMapper = new CustomerMapper();
    private static ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static CupcakeMapper cupcakeMapper = new CupcakeMapper();
    private static OrderMapper orderMapper = new OrderMapper();


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

        app.post("/orders", ctx -> handleOrderDeletion(ctx));

        //confirm order
        app.post("/confirmorder", ctx -> handleConfirmOrder(ctx));
    }

    public static void handleConfirmOrder(Context ctx) {
        String email = ctx.sessionAttribute("email");
        ArrayList<Cupcakes> cart = ctx.sessionAttribute("cart");
        double totalPrice = ctx.sessionAttribute("totalOrderPrice");
        int customerId = 0;

        if (cart == null || cart.isEmpty()) {
            ctx.redirect("/index?error=cartEmpty");
            return;
        }

        try {
            double wallet = customerMapper.getWalletFromEmail(connectionPool, email);
            if (totalPrice <= wallet) {
                customerId = customerMapper.getCustomerIdFromEmail(connectionPool, email);
                orderMapper.executeConfirmOrder(connectionPool, customerId, cart);
                ctx.sessionAttribute("cart", null);
                customerMapper.setWallet(connectionPool, (wallet-totalPrice), email);
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        showIndexPage(ctx);
    }

    public static void handleCustomerDeletion(Context ctx) {
        String customerIdString = ctx.formParam("customerId");

        try {
            int customerId = Integer.parseInt(customerIdString);
            if (customerId != 1) {
                customerMapper.removeCustomerById(connectionPool, customerId);
            }
            showCustomersPage(ctx);

        } catch (NumberFormatException e) {
            showCustomersPage(ctx);
        } catch (DatabaseException e) {
            showCustomersPage(ctx);
        }
    }

    public static void handleOrderDeletion(Context ctx) {
        String orderIdString = ctx.formParam("orderId");

        try {
            int orderId = Integer.parseInt(orderIdString);
            orderMapper.executeRemoveOrderAndHistoryById(connectionPool, orderId);
            showOrdersPage(ctx);

        } catch (NumberFormatException e) {
            showOrdersPage(ctx);
        } catch (DatabaseException e) {
            showOrdersPage(ctx);
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
        String email = ctx.sessionAttribute("email");
        String username = ctx.sessionAttribute("username");
        try {
            List<Order> orders = new ArrayList<>();

            int customerId;
            if ("admin".equals(username)) {
                orders = orderMapper.getListOfAllCustomersOrders(connectionPool);

            } else {
                customerId = customerMapper.getCustomerIdFromEmail(connectionPool, email);
                orders = orderMapper.getListOfOneCustomersOrders(connectionPool, customerId);
            }
            if (orders.isEmpty()) {
                ctx.redirect("/index?error=ordersEmpty");
            } else {
                ctx.render("orders.html", Map.of("username", username, "orders", orders));
            }

        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving orders: " + e.getMessage());
        }
    }

    private static void showLoginPage(Context ctx) {
        ctx.render("/login.html");
    }

    private static void handleLogin(Context ctx) {
        int userId;
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        try {
            boolean validateUser = customerMapper.verifyUserCredentials(connectionPool, username, password);
            if (validateUser) {
                if (username.contains("@")) {
                    String email = username;
                    username = customerMapper.getCustomerNameFromEmail(connectionPool, email);
                    userId = customerMapper.getCustomerIdFromEmail(connectionPool, email);
                } else {
                    String email = customerMapper.getEmailFromCustomerName(connectionPool, username);
                    userId = customerMapper.getCustomerIdFromEmail(connectionPool, email);
                }

                ctx.sessionAttribute("username", username);
                ctx.sessionAttribute("customerId", userId);
                String email = customerMapper.getEmailFromCustomerName(connectionPool, username);
                ctx.sessionAttribute("email", email);
                ctx.sessionAttribute("cart", null);
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
        List<Topping> toppings = new ArrayList<>();
        List<Bottom> bottoms = new ArrayList<>();
        toppings = ctx.sessionAttribute("toppings");
        bottoms = ctx.sessionAttribute("bottoms");
        if (toppings == null || bottoms == null) {
            try {
                toppings = CupcakeMapper.getAllToppings(connectionPool);
                bottoms = CupcakeMapper.getAllBottoms(connectionPool);
                ctx.sessionAttribute("toppings", toppings);
                ctx.sessionAttribute("bottoms", bottoms);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }

        String username = ctx.sessionAttribute("username");
        List<Cupcakes> cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            ctx.sessionAttribute("cart", cart);
        }
        double totalOrderPrice = 0.0;
        for (Cupcakes cupcake : cart) {
            totalOrderPrice += cupcake.getTotalCupcakePrice();
        }
        ctx.sessionAttribute("totalOrderPrice", totalOrderPrice);

        if (username == null) {
            ctx.render("/index.html");
        } else {
            ctx.render("/index.html");
        }
    }

    private static void handleSignup(Context ctx) {
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        float wallet = 0;
        try {
            if (username.equals("admin")) {
                ctx.redirect("signup?error=cannotloginasadmin");
            } else {
                boolean succes = customerMapper.addNewCustomer(connectionPool, email, username, password, wallet);
                if (succes == true) {
                    ctx.sessionAttribute("username", username);
                    ctx.sessionAttribute("email", email);
                } else {
                    ctx.redirect("signup?error=usernamealreadyexists");
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        ctx.sessionAttribute("cart", null);
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

            Cupcakes cupcakes = new Cupcakes(new Bottom(bottomFlavour, bottomId), new Topping(toppingFlavour, toppingId), quantity, totalCupcakePrice);
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

    public static void getShowIndexPage(Context ctx) {
        showIndexPage(ctx);
    }
}