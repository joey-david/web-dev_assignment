<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Users</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Liste des utilisateurs connectés</h2>
<p>Il y a actuellement ${fn:length(users)} utilisateur(s) connect&eacute;(s) :</p>
<ul>
    <c:forEach var="u" items="${users}">
        <li>${u.login} : <strong><a href="user?user=${u.login}">${u.name}</a></strong></li>
    </c:forEach>
</ul>
<p><a href="interface.jsp">Retour aux réservations</a></p>
</body>
</html>