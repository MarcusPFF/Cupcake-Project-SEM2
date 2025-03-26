package app.persistence;

public class CustomerMapper {
    public String getCustomerNameFromEmail(ConnectionPool connectionPool, String customerEmail) throws DatabaseException {
        String sql = "SELECT customer_name FROM cupcake_customers WHERE customer_email = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("customer_name");
                } else {
                    throw new DatabaseException(null, "Could not find customer name for the given email.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch name from database.");
        }
    }

    /*
    //bruges inde i handleLogin inde i controlleren
     if (validUser) {
        if (username.contains("@")) {
            String username1 = subscriberMapper.getUsernameFromEmail(ConnectionPool.getInstance(), username);
            ctx.sessionAttribute("username", username1);
            ctx.sessionAttribute("email", username);
        } else {
            String email = subscriberMapper.getEmailFromUsername(ConnectionPool.getInstance(), username);
            ctx.sessionAttribute("username", username);
            ctx.sessionAttribute("email", email);
        }
        showDashboardPage(ctx);
    } else {
        ctx.redirect(TEAM_PREFIX + "/login");
    }*/
}
