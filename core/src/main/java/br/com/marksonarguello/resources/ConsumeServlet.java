package br.com.marksonarguello.resources;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.queue.services.QueueService;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@WebServlet("/consume")
public class ConsumeServlet extends HttpServlet {
    private final QueueService queueService = QueueService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        ConsumerRecord consumerRecord = queueService.consumeMessages(id);

        String body = BodyConverter.toJson(consumerRecord);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON);
        response.getWriter().write(body);
    }
}
