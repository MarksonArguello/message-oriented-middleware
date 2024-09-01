package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.queue.services.QueueService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@WebServlet("/register")
public class RegisterConsumerServlet extends HttpServlet {

    QueueService queueService = QueueService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = queueService.register();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON);
        response.getWriter().write(id);
    }
}
