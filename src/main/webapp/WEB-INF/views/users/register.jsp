<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 12/11/2018
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html ng-app="myApp">
<head>
    <jsp:include page="/WEB-INF/views/partials/header.jsp">
        <jsp:param name="title" value="Register" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<% String username = request.getParameter("username");
    if (username == null) username = "";
    String email = request.getParameter("email");
    if (email == null) email = "";
%>


<h3>Register here:</h3>

<div class="container">

    <form:form action="/register" method="POST" modelAttribute="user">

        <input id="id" name="id" type="hidden">
        <input id="joinDate" name="joinDate" type="hidden">

        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" name="username" class="form-control" value="<%=username%>">
            <form:errors path="username" />
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" name="email" class="form-control" type="text" value="<%=email%>">
            <div><form:errors path="email" /></div>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" class="form-control" type="password">
            <form:errors path="password" />
        </div>

        <div class="form-group">
            <label for="confirm_password">Confirm Password</label>
            <input id="confirm_password" name="confirm_password" class="form-control" type="password">
        </div>

        <input type="submit" class="btn btn-primary btn-block" value="Register">

    </form:form>
</div>
<jsp:include page="/WEB-INF/views/partials/footer.jsp"/>
</body>
</html>
