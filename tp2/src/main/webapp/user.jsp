<jsp:useBean id="user" scope="request" type="fr.univlyon1.m1if.m1if03.classes.User"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Utilisateur ${user.login}</h2>
<div>
    Login : ${user.login}<br>
    <form method="post" action="user">
        <label for="name">Prénom :
            <input type="text" name="name" id="name" value="${user.name}">
        </label>&nbsp;
        <input type="submit" value="Modifier">
        <input type="hidden" name="login" value="${user.login}">
    </form>
    <c:if test="${sessionScope.login.equals(user.login)}">
    <br>
    Resas:
    <ul>
        <c:forEach items="${applicationScope.resas}" var="resa">
            <c:if test="${resa.assignee != null && resa.assignee.equals(user)}">
                <li><a href="resalist#${resa.hashCode()}">${resa.title}</a></li>
            </c:if>
        </c:forEach>
    </ul>
</div>
</c:if>
</body>
</html>