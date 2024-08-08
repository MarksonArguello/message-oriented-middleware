package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.broker.services.BrokerService;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@WebServlet("/queue")
public class QueueServlet extends HttpServlet {

    private final BrokerService brokerService = BrokerService.getInstance();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        QueueCreateDTO queueCreateDTO = BodyConverter.fromJson(request.getReader(), QueueCreateDTO.class);

        String responseBody = brokerService.createQueue(queueCreateDTO);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType(MediaType.APPLICATION_JSON);
        response.getWriter().write(responseBody);
    }
}
