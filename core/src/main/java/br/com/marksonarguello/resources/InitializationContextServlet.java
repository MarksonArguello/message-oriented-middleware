package br.com.marksonarguello.resources;

import br.com.marksonarguello.entities.configuration.ConfigurationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationContextServlet implements ServletContextListener {

    public void contextInitialized(ServletContextEvent e) {
        ConfigurationService.loadProperties();
        ConfigurationService.loadQueues();
    }

    public void contextDestroyed(ServletContextEvent e) {

    }
}