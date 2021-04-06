package ru.job4j.todo.servlets;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmTODO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import ru.job4j.todo.model.Category;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HbmTODO hbmTODO = new HbmTODO();
        User user = hbmTODO.findByCredential(email, password);
        List<Category> list = hbmTODO.findAllCategory();
        
        
        if (list.isEmpty()) {
            hbmTODO.add(Category.of("срочная"));
            hbmTODO.add(Category.of("несрочная"));
            list = hbmTODO.findAllCategory();
        }
        System.out.println("List<Category> = " + list);
        
        
        if (user != null) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
   
            sc.setAttribute("allCategories", list);
            
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}

