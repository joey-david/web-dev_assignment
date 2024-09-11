<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Modifier Mon Compte</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1> Modifier Mon Compte</h1>
<jsp:useBean id="user" type="fr.univlyon1.m1if.m1if03.classes.User" scope="session"/>
<form method="post" action="resas">
    <p>
        Modifiez vos informations ci-dessous :<br>
        <label>Login : <input type="text" name="login" value="<%=user.getLogin()%>" readonly></label> <br>
        <label>Prénom : <input type="text" name="prénom" value="<%= user.getName()%>"> </label> <br>
        <input type="submit" value="Connexion">
    </p>
</form>
</body>
</html>
