package ru.job4j.todo.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.HbmTODO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class ShowAllTasksServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("start ShowAllTasksServlet");

        HbmTODO hbmTODO = new HbmTODO();
        List<Task> list = hbmTODO.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(list);
        System.out.println(response);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(response);
    }
}
