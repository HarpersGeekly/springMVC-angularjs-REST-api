<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 1/3/2019
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/views/partials/header.jsp">
        <jsp:param name="title" value="${post.title}" />
    </jsp:include>
</head>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />
<body>
<jsp:include page="/WEB-INF/views/partials/post.jsp" />
</body>
</html>
