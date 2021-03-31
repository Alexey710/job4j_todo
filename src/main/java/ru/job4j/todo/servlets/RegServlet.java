package ru.job4j.todo.servlets;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmTODO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HbmTODO hbmTODO = new HbmTODO();
        User user = hbmTODO.findByCredential(email, password);
        if (user == null) {
            hbmTODO.addUser(User.of(name, email, password));
            req.setAttribute("status", "Пользователь зарегистрирован.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            req.setAttribute("status", "E-mail уже существует. Введите новый e-mail.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
