<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.daos.Dao" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="user" beanName="user" type="fr.univlyon1.m1if.m1if03.classes.User" scope="session"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Resas Web</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <h1 class="header-titre">Réservation de courts de tennis</h1>
    <p class="header-user">Bonjour <strong><a href="user?user=<jsp:getProperty name="user" property="login"/>"><jsp:getProperty name="user" property="name"/></a></strong>,<br>
        il y a actuellement <%=((Dao<User>) (application.getAttribute("users"))).findAll().size()%> utilisateur(s) connect&eacute;(s).
    </p>
</header>

<div class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <div>
            <a href="users">Utilisateurs</a>
            <br>
            <a href="resas?operation=disconnect">D&eacute;connexion</a>
        </div>
    </aside>

    <article class="contenu">
        <iframe src="resalist" name="list" style="border: none; width: 100%; height: 300px;"></iframe>
        <hr>
        <form method="post" action="resalist" target="list">
            <p>
                <em>Ajouter une réservation</em>
                <br>
                Titre :
                <label>
                    <input type="text" name="title">
                </label>
                Date :
                <label>
                    <input type="date" name="startDate">
                </label>
                Heure :
                <label>
                    <input type="time" name="startTime">
                </label>
                Durée :
                <label>
                    <input type="number" size="3" name="duration" value="60">
                </label>
                minutes

                <input type="submit" value="Envoyer">
                <input type="hidden" name="operation" value="add">
            </p>
        </form>
    </article>
</div>

<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img
            alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;"
            src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png"/></a></div>
</footer>
</body>
</html>