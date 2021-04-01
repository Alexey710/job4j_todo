package ru.job4j.todo.servlets;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmTODO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String encodedJson = br.readLine();
        System.out.println(encodedJson);
        String decodedJson = null;
        try {
            decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.name());
            System.out.println(decodedJson);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        System.out.println(decodedJson.contains("="));
        
        User user = (User) req.getSession().getAttribute("user");
        
        HbmTODO hbmTODO = new HbmTODO();
        if (decodedJson.contains("=")) {
            String description = decodedJson.split("=")[1];
            Timestamp time = new Timestamp(1000 * (System.currentTimeMillis() / 1000));
            Task task = new Task(
                    time, description, false, user);
            hbmTODO.add(task);
        }
        
        List<Task> list = hbmTODO.findByUndone(user);
        System.out.println(list);
        req.setAttribute("tasks", list);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
