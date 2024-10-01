package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Resa;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.ResaDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import fr.univlyon1.m1if.m1if03.exceptions.MissingParameterException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Cette servlet gère la liste des réservations.
 * Elle permet actuellement d'afficher la liste et de créer de nouvelles réservations.
 * Elle devra aussi permettre de modifier l'état d'un réservation
 */
@WebServlet(name = "ResaList", value = "/resalist")
public class ResaList extends HttpServlet {
    // Format des dates
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ResaDao resaDao;
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.resaDao = (ResaDao) config.getServletContext().getAttribute("resas");
        this.userDao = (UserDao) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Resa> reservations = resaDao.findAllReservations();
        request.setAttribute("reservations", reservations);
        request.setAttribute("userDao", userDao);
        request.getRequestDispatcher("resalist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            switch (request.getParameter("operation")) {
                case "add" -> handleAddOperation(request);
                case "update" -> handleUpdateOperation(request);
                default -> throw new UnsupportedOperationException("Opération à réaliser non prise en charge.");
            }
            doGet(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void handleAddOperation(HttpServletRequest request) throws MissingParameterException {
        if (request.getParameter("title") == null ||
                request.getParameter("startDate") == null ||
                request.getParameter("startDate").isEmpty() ||
                request.getParameter("startTime") == null ||
                request.getParameter("startTime").isEmpty() ||
                request.getParameter("duration") == null ||
                request.getParameter("duration").isEmpty()) {
            throw new MissingParameterException("Paramètres de la réservation insuffisamment spécifiés.");
        }

        LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate") + " " + request.getParameter("startTime"), FORMATTER);
        String userLogin = ((User) request.getSession().getAttribute("user")).getLogin();
        if(request.getParameter("duration") != null && !request.getParameter("duration").isEmpty()) {
            Duration duration = Duration.parse("PT" + request.getParameter("duration") + "M");
            resaDao.add(new Resa(request.getParameter("title"), userLogin, startDate, duration));
        } else {
            LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end"));
            resaDao.add(new Resa(request.getParameter("title"), userLogin, startDate, endDate));
        }
    }

    private void handleUpdateOperation(HttpServletRequest request) throws Exception {
        int index = Integer.parseInt(request.getParameter("index"));
        if (index < 0) {
            throw new StringIndexOutOfBoundsException("Pas de réservation avec l'index " + index + ".");
        }
        Resa resa = resaDao.findOne(index);
        String userLogin = ((User) request.getSession().getAttribute("user")).getLogin();

        if (request.getParameter("toggle") != null && !request.getParameter("toggle").isEmpty()) {
            if(request.getParameter("toggle").equals("M'inscrire")) {
                resa.addPlayer(userLogin);
            } else {
                resa.removePlayer(userLogin);
            }
        } else if (request.getParameter("comment") != null && !request.getParameter("comment").isEmpty()) {
            resa.addComment(request.getParameter("commentData"));
        } else {
            throw new MissingParameterException("Modification à réaliser non spécifiée.");
        }
    }
}