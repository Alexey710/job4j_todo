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
import ru.job4j.todo.model.User;

public class ShowAllTasksServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("start ShowAllTasksServlet");
        User user = (User) req.getSession().getAttribute("user");
        HbmTODO hbmTODO = new HbmTODO();
        List<Task> list = hbmTODO.findAll(user);

        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(list);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(response);
    }
}
