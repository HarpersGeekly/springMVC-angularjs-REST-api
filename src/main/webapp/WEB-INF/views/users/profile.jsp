<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 12/17/2018
  Time: 12:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/views/partials/header.jsp">
        <jsp:param name="title" value="${user}'s Profile" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<h3>${message}</h3>
<h3>${user.username}'s profile</h3>

</body>
</html>
