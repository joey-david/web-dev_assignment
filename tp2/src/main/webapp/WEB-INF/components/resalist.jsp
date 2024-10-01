<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Reservations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
    <%--@elvariable id="reservations" type="java.util.List"--%>
    <c:forEach var="resa" items="${reservations}" varStatus="status">
        <form method="POST" action="resalist">
            <tr id="${resa.hashCode()}">
                <td>${resa.completed ? "&#x2611;" : "&#x2610;"}</td>
                <td><em>${resa.title}</em></td>
                <td><em>${resa.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</em></td>
                <td><em>${resa.startDate.format(DateTimeFormatter.ofPattern("HH:mm"))}</em></td>
                <td><em>${resa.endDate.format(DateTimeFormatter.ofPattern("HH:mm"))}</em></td>
                <td><em>
                    <c:forEach var="login" items="${resa.playerLogins}">
                        <%--@elvariable id="userDao" type="fr.univlyon1.m1if.m1if03.daos.UserDao"--%>
                        <c:set var="player" value="${userDao.findOne(login)}" />
                        ${player != null ? player.name : login}<br>
                    </c:forEach>
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
                    <c:forEach var="comment" items="${resa.comments}">
                        ${comment}<br>
                    </c:forEach>
                    <textarea name='commentData'></textarea>
                </td>
                <td><input type='submit' name='comment' value='Commenter'></td>
            </tr>
            <input type='hidden' name='operation' value='update'>
            <input type='hidden' name='index' value='${status.index}'>
        </form>
    </c:forEach>
    </tbody>
</table>
<script>
    if (location.hash) {
        document.getElementById(location.hash.substring(1)).style = "color: red";
    }
</script>
</body>
</html>
