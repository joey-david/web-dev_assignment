package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Resa;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.exceptions.MissingParameterException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette servlet gère la liste des réservations.
 * Elle permet actuellement d'afficher la liste et de créer de nouvelles réservations.
 * Elle devra aussi permettre de modifier l'état d'un réservation
 */
@WebServlet(name = "ResaList", value = "/resalist")
public class ResaList extends HttpServlet {
    // Format des dates
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter dateOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");

    //TODO Placer la map dans le contexte
    private final List<Resa> resas = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Génère "à la main" la page HTML de réponse
        PrintWriter out = response.getWriter();
        out.write("""
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                  <meta charset="UTF-8">
                  <title>Réservations</title>
                  <link rel="stylesheet" href="css/style.css">
                  <meta http-equiv="refresh" content="5">
                </head>
                <body>
                  <table>
                    <thead>
                      <td>Complète</td>
                      <td>Titre</td>
                      <td>Date</td>
                      <td>Début</td>
                      <td>Fin</td>
                      <td>Joueurs</td>
                      <td></td>
                      <td>Commentaires</td>
                     </thead>
                     <tbody>""");
        for (Resa r : resas) {
            out.println("      <form action='resalist' method='POST'>");
            out.write("        <tr><td>" + (r.isCompleted() ? "&#x2611;" : "&#x2610;") + "</td>");
            out.write("          <td><em>" + r.getTitle() + "</em></td>");
            out.write("          <td><em>" + r.getStartDate().format(dateOutputFormatter) + "</em></td>");
            out.write("          <td><em>" + r.getStartDate().format(timeOutputFormatter) + "</em></td>");
            out.write("          <td><em>" + r.getEndDate().format(timeOutputFormatter) + "</em></td>");
            out.write("          <td><em>");
            for(User p: r.getPlayers()) {
                out.print(p.getName() + "<br>");
            }
            out.write("</em></td>");
            if (r.hasPlayer((User) request.getSession().getAttribute("user"))) {
                out.write("          <td><input type='submit' name='toggle' value='Me désinscrire'></td>");
            } else {
                out.write("          <td><input type='submit' name='toggle' value='M&apos;inscrire'></td>");
            }
            out.write("          <td><textarea name='comment'></textarea></td>");
            out.write("          <td><input type='submit' name='comment' value='Commenter'></td>");
            out.println("        </tr>");
            out.println("        <input type='hidden' name='operation' value='update'>");
            out.println("        <input type='hidden' name='index' value='" + resas.indexOf(r) + "'>");
            out.println("      </form>");
        }
        out.write("""
                    </tbody>
                  </table>
                </body>
                </html>""");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            switch (request.getParameter("operation")) {
                case "add" -> {
                    if (request.getParameter("title") == null ||
                            request.getParameter("startDate") == null ||
                            request.getParameter("startDate").isEmpty() ||
                            request.getParameter("startTime") == null ||
                            request.getParameter("startTime").isEmpty() ||
                            request.getParameter("duration") == null ||
                            request.getParameter("duration").isEmpty())
                        throw new MissingParameterException("Paramètres de la réservation insuffisamment spécifiés.");

                    // crée une nouvelle réservation en fonction des paramètres et l'ajoute à la liste
                    LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate") + " " + request.getParameter("startTime"), inputFormatter);
                    if(request.getParameter("duration") != null && !request.getParameter("duration").isEmpty()) {
                        // Formatage de la durée au standard ISO8601
                        Duration duration = Duration.parse("PT" + request.getParameter("duration") + "M");
                        resas.add(new Resa(request.getParameter("title"), (User) request.getSession().getAttribute("user"), startDate, duration));
                    } else {
                        LocalDateTime endDate = LocalDateTime.parse(request.getParameter("end"));
                        resas.add(new Resa(request.getParameter("title"), (User) request.getSession().getAttribute("user"), startDate, endDate));
                    }
                }
                case "update" -> {
                    // Récupération de l'index
                    int index = Integer.parseInt(request.getParameter("index"));
                    if (index < 0 || index >= resas.size())
                        throw new StringIndexOutOfBoundsException("Pas de réservation avec l'index " + index + ".");
                    if (request.getParameter("toggle") == null && !request.getParameter("toggle").isEmpty())
                        throw new MissingParameterException("Modification à réaliser non spécifiée.");
                    Resa resa = resas.get(index);
                    if(request.getParameter("toggle").equals("M'inscrire"))
                        resa.addPlayer((User) request.getSession().getAttribute("user"));
                    else
                        resa.removePlayer((User) request.getSession().getAttribute("user"));
                }
                default -> throw new UnsupportedOperationException("Opération à réaliser non prise en charge.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format de l'index de la réservation incorrect.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        // Reprend le comportement des requêtes GET
        doGet(request, response);
    }
}
