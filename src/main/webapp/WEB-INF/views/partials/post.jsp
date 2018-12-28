<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<c:forEach var="post" items="${posts}">
    <h1><c:out value="${post.title}"/></h1>
    <h3><c:out value="${post.subtitle}"/></h3>
    <div>
        By: <a href="/profile/${post.user.id}/${post.user.username}"><c:out value="${post.user.username}"/></a>
        <span style="margin-left: 20px">${post.hoursMinutes}</span>
        <span style="margin-left: 20px">${post.date}</span>
    </div>
    <img src="<c:out value="${post.leadImage}"/>" alt="image"/>
</c:forEach>

</body>
</html>
