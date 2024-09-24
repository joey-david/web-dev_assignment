package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Resa;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.ResaDao;
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

/**
 * Cette servlet gère la liste des réservations.
 * Elle permet actuellement d'afficher la liste et de créer de nouvelles réservations.
 * Elle devra aussi permettre de modifier l'état d'un réservation
 */
@WebServlet(name = "ResaList", value = "/resalist")
public class ResaList extends HttpServlet {
    // Format des dates
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ResaDao resas;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.resas = (ResaDao) config.getServletContext().getAttribute("resas");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("resas", resas.findAllReservations());
        request.getRequestDispatcher("resalist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            switch (request.getParameter("operation")) {
                case "add" -> {
                    if (request.getParameter("title") == null ||
                            request.getParameter("startDate") == null ||
                            request.getParameter("startDate").isEmpty() ||
                            request.getParameter("startTime") == null ||
                            request.getParameter("startTime").isEmpty() ||
                            request.getParameter("duration") == null ||
                            request.getParameter("duration").isEmpty()) {
                        throw new MissingParameterException("Paramètres de la réservation insuffisamment spécifiés.");
                    }

                    // crée une nouvelle réservation en fonction des paramètres et l'ajoute à la liste
                    LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate") + " " + request.getParameter("startTime"), FORMATTER);
                    String userLogin = ((User) request.getSession().getAttribute("user")).getLogin();
                    if(request.getParameter("duration") != null && !request.getParameter("duration").isEmpty()) {
                        // Formatage de la durée au standard ISO8601
                        Duration duration = Duration.parse("PT" + request.getParameter("duration") + "M");
                        resas.add(new Resa(request.getParameter("title"), userLogin, startDate, duration));
                    } else {
                        LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end"));
                        resas.add(new Resa(request.getParameter("title"), userLogin, startDate, endDate));
                    }
                    // Reprend le comportement des requêtes GET
                    doGet(request, response);
                }
                case "update" -> {
                    // Récupération de l'index
                    int index = Integer.parseInt(request.getParameter("index"));
                    if (index < 0) {
                        throw new StringIndexOutOfBoundsException("Pas de réservation avec l'index " + index + ".");
                    }
                    Resa resa = resas.findOne(index);
                    if (request.getParameter("toggle") != null && !request.getParameter("toggle").isEmpty()) {
                        String userLogin = ((User) request.getSession().getAttribute("user")).getLogin();
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

                    // Reprend le comportement des requêtes GET
                    doGet(request, response);
                }
                default -> throw new UnsupportedOperationException("Opération à réaliser non prise en charge.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format de l'index de la réservation incorrect.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }
}
