package br.com.marksonarguello;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hello-world")
public class TesteServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("Hello-World");
    }
}
