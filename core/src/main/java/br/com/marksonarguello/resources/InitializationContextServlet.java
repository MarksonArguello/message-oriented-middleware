package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.queue.services.QueueService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationContextServlet implements ServletContextListener {

    private final QueueService queueService = QueueService.getInstance();
    public void contextInitialized(ServletContextEvent e) {
        System.out.println("Initializing queues");
        queueService.loadQueues();
    }

    public void contextDestroyed(ServletContextEvent e) {

    }
}