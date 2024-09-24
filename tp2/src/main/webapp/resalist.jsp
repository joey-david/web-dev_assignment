<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.daos.UserDao" %>
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
    <td>Complète</td>
    <td>Titre</td>
    <td>Date</td>
    <td>Début</td>
    <td>Fin</td>
    <td>Joueurs</td>
    <td></td>
    <td>Commentaires</td>
    </thead>
    <c:forEach var="resa" items="${resas}">
        <form method="POST" action="resalist">
            <tr id="${resa.hashCode()}">
                <td>${resa.completed ? "&#x2611;" : "&#x2610;"}</td>
                <td><em>${resa.title}</em></td>
                <td><em>${resa.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</em></td>
                <td><em>${resa.startDate.format(DateTimeFormatter.ofPattern("HH:mm"))}</em></td>
                <td><em>${resa.endDate.format(DateTimeFormatter.ofPattern("HH:mm"))}</em></td>
                <td><em>
                    <c:forEach var="playerLogin" items="${resa.playerLogins}">
                        ${((UserDao)applicationScope.users).findOne(playerLogin).name}<br>
                    </c:forEach>
                </em></td>
                <td><em>
                    <c:if test="${resa.hasPlayer(sessionScope.user.login)}">
                        <input type='submit' name='toggle' value='Me désinscrire'>&nbsp;
                    </c:if>
                    <c:if test="${!resa.hasPlayer(sessionScope.user.login)}">
                        <input type='submit' name='toggle' value='M&apos;inscrire'>&nbsp;
                    </c:if>
                </em></td>
                <td>
                    <c:forEach var="c" items="${resa.comments}">
                        ${c}<br>
                    </c:forEach>
                    <textarea name='commentData'></textarea>
                </td>
                <td><input type='submit' name='comment' value='Commenter'></td>
            </tr>
            <input type='hidden' name='operation' value='update'>
            <input type='hidden' name='index' value='${resas.indexOf(resa)}'>
        </form>
    </c:forEach>
</table>
<script>
    if(location.hash) {
        document.getElementById(location.hash.substring(1)).style = "color: red";
    }
</script>
</body>
</html>