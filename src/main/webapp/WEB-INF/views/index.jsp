<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 12/12/2018
  Time: 3:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html data-ng-app="app"> <!-- an angular directive, defines that this is an angularjs application, give it a name-->
<head>
    <jsp:include page="/WEB-INF/views/partials/header.jsp">
        <jsp:param name="title" value="trainingapp" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container flex-center">
    <h1>Welcome!</h1>
</div>

<div class="container">
    <jsp:include page="/WEB-INF/views/partials/post.jsp" />
</div>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.5/angular.min.js"></script>
</body>
</html>
