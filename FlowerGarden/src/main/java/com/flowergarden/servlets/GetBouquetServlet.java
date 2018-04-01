package com.flowergarden.servlets;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.context.ApplicationContextDAO;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.dao.json.JsonDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 01.04.2018.
 */
@WebServlet(name = "getBouquetServlet", urlPatterns = "/bouquet")
public class GetBouquetServlet extends HttpServlet {
    ApplicationContext context;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context =
                WebApplicationContextUtils
                        .getRequiredWebApplicationContext(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        BouquetDAO dao = (BouquetDAO) context.getBean("bouquetDAO");
        MarriedBouquet bouquetFromDB = dao.getByKey(id);
        JsonDAO jsonDAO = (JsonDAO) context.getBean("jsonDAO");
        jsonDAO.create(bouquetFromDB);
        List <String> resultLines = Files.readAllLines(Paths.get("bouquet.json"), Charset.defaultCharset());
        req.setAttribute("result", resultLines);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("bouquet.jsp");
        requestDispatcher.forward(req, resp);
    }
}
