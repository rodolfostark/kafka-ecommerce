package br.com.alura.ecommerce;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            var email = Math.random() + "@email.com";
            var userId = UUID.randomUUID().toString();
            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(Math.random() * 5000 + 1);

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

        }
        try (var emailDispatcher = new KafkaDispatcher<Email>()) {
            var userId = UUID.randomUUID().toString();
            var emailSubject = "New order";
            var emailBody = "Thank you for your order! We are processing your order!";
            var email = new Email(emailSubject, emailBody);
            try {
                emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
