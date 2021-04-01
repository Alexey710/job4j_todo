package ru.job4j.todo.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.HbmTODO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import ru.job4j.todo.model.User;

public class DoTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("start DoTaskServlet");

        String stringTrimmed = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            String encodedJson = br.readLine();
            System.out.println(encodedJson);
            String decodedJson = null;
            decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.name());
            System.out.println(decodedJson);
            stringTrimmed = encodedJson.substring(1, encodedJson.length() - 2); //было (1, encodedJson.length() - 1)
            System.out.println(stringTrimmed);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        
       
        
        HbmTODO hbmTODO = new HbmTODO();
        if (!"0".equals(stringTrimmed)) {
            Task found = hbmTODO.findById(stringTrimmed);
            hbmTODO.done(found);
        }
        
        User user = (User) req.getSession().getAttribute("user");
                
        List<Task> list = hbmTODO.findByUndone(user);

        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(list);
       
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(response);

    }
}
