package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.broker.services.BrokerService;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/queue")
public class QueueServlet extends HttpServlet {

    private final BrokerService brokerService = new BrokerService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        QueueCreateDTO queueCreateDTO = BodyConverter.fromJson(request.getReader(), QueueCreateDTO.class);

        String responseBody = brokerService.createQueue(queueCreateDTO);

        response.setStatus(HttpServletResponse.SC_CREATED);

        response.getWriter().write(responseBody);
    }
}
