package br.com.alura.ecommerce;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        var email = req.getParameter("email");
        var amount = new BigDecimal(req.getParameter("amount"));

        var orderId = UUID.randomUUID().toString();
        var order = new Order(orderId, amount, email);

        try {
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);
            System.out.println("New order sent successfully.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully.");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        var userId = UUID.randomUUID().toString();
        var emailSubject = "New order";
        var emailBody = "Thank you for your order! We are processing your order!";
        var emailCode = new Email(emailSubject, emailBody);
        try {
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, emailCode);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
