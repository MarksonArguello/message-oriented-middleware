package br.com.marksonarguello.resources;



import br.com.marksonarguello.entities.queue.services.QueueService;
import br.com.marksonarguello.message.Message;
import br.com.marksonarguello.util.BodyConverter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/send")
public class SendMessageServlet extends HttpServlet {
    private final QueueService queueService = QueueService.getInstance();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String topic = request.getParameter(Message.TOPIC);
        Message message = BodyConverter.fromJson(request.getReader(), Message.class);
        queueService.addMessageInTopic(topic, message);
    }
}
