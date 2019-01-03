<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<c:forEach var="post" items="${posts}">
    <h1>${post.htmlTitle}</h1>
    <h3>${post.htmlSubtitle}</h3>
    <div>
        By: <a href="/profile/${post.user.id}/${post.user.username}"><c:out value="${post.user.username}"/></a>
        <span style="margin-left: 20px">${post.hoursMinutes}</span>
        <span style="margin-left: 20px">${post.date}</span>
    </div>
    <div>${post.htmlLeadImage}</div>
</c:forEach>

</body>
</html>
