<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.univlyon1.m1if.m1if03.daos.ResaDao" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Resa" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.daos.UserDao" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Reservations</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h2>Liste des réservations</h2>
<table>
    <thead>
    <tr>
        <td>Complète</td>
        <td>Titre</td>
        <td>Date</td>
        <td>Début</td>
        <td>Fin</td>
        <td>Joueurs</td>
        <td></td>
        <td>Commentaires</td>
    </tr>
    </thead>
    <tbody>
    <%
        ResaDao resaDao = (ResaDao) application.getAttribute("resas");
        UserDao userDao = (UserDao) application.getAttribute("users");

        List<Resa> reservations = resaDao.findAllReservations();

        for (Resa resa : reservations) {
            request.setAttribute("resa", resa);
    %>
    <form method="POST" action="resalist">
        <tr id="<%= resa.hashCode() %>">
            <td><%= resa.isCompleted() ? "&#x2611;" : "&#x2610;" %></td>
            <td><em><%= resa.getTitle() %></em></td>
            <td><em><%= resa.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) %></em></td>
            <td><em><%= resa.getStartDate().format(DateTimeFormatter.ofPattern("HH:mm")) %></em></td>
            <td><em><%= resa.getEndDate().format(DateTimeFormatter.ofPattern("HH:mm")) %></em></td>
            <td><em>
                <%
                    List<String> playerLogins = resa.getPlayerLogins();
                    for (String login : playerLogins) {
                        User player = null;
                        try {
                            player = userDao.findOne(login);
                        } catch (Exception e) {
                        }
                %>
                <%= player != null ? player.getName() : login %><br>
                <%
                    }
                %>
            </em></td>
            <td><em>
                <c:if test="${resa.hasPlayer(sessionScope.user.login)}">
                    <input type='submit' name='toggle' value='Me désinscrire'>&nbsp;
                </c:if>
                <c:if test="${!resa.hasPlayer(sessionScope.user.login)}">
                    <input type='submit' name='toggle' value="M'inscrire">&nbsp;
                </c:if>
            </em></td>
            <td>
                <%
                    List<String> comments = resa.getComments();
                    for (String comment : comments) {
                %>
                <%= comment %><br>
                <%
                    }
                %>
                <textarea name='commentData'></textarea>
            </td>
            <td><input type='submit' name='comment' value='Commenter'></td>
        </tr>
        <input type='hidden' name='operation' value='update'>
        <input type='hidden' name='index' value='<%= reservations.indexOf(resa) %>'>
    </form>
    <%
        }
    %>
    </tbody>
</table>
<script>
    if (location.hash) {
        document.getElementById(location.hash.substring(1)).style = "color: red";
    }
</script>
</body>
</html>
