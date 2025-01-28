package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.queue.services.QueueService;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/subscribe")
public class SubscribeServlet extends HttpServlet {

    QueueService queueService = QueueService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        System.out.println("Subscribing");

        List topics = BodyConverter.fromJson(
                request.getReader(),
                List.class
        );
        try {
            queueService.subscribe(id, topics);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(e.getMessage());
            return;
        }


        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
