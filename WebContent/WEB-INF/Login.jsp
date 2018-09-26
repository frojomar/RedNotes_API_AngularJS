<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>RedNotes-Login</title>
    <link rel="stylesheet" href="css/master.css">
    <link rel="stylesheet" href="css/reglogin.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">
  </head>
  <body>
    <div class="content">
      <div class="logoname">
        <img class="logo" src="images/myfavicon.png" alt="">
        <h1 class="name">RedNotes</h1>
      </div>
      <div class="box">
        <c:forEach var="error" items="${messages}">       
        	<p>${error}</p>
        </c:forEach>
        <form action="LoginServlet" method="post">
          <label>E-mail or username: </label><input type="text" name="email" placeholder="example@domain.com" required>
          <br>
          <label>Password: </label><input type="password" name="password" placeholder="********" required>
          <br>
          <div id="buttons">
            <input type="submit" name="login" value="Log In">
            <input type="reset" name="reset" value="Clear">
          </div>
        </form>
        <p>Have you forget your password? <a href="pages/RememberPassword.html">Remember now</a></p>
        <div id="registerlink">
          <form action="registerpages/RegisterIndex.html" method="post">
            <label for="register"> Are you not register? </label><br>
            <input type="submit" name="register" value="Register Now!!">
          </form>
        </div>
      </div>
    </div>
  </body>

</html>