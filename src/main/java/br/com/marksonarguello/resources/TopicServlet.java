package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.broker.services.BrokerService;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@WebServlet("/topics")
public class TopicServlet extends HttpServlet {

    private final BrokerService brokerService = BrokerService.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String body = BodyConverter.toJson(brokerService.findAllTopics());

        response.setContentType(MediaType.APPLICATION_JSON);
        response.getWriter().write(body);
    }
}
