<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 12/11/2018
  Time: 3:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="/posts">trainingapp</a>
        </div>
        <ul class="nav navbar-nav navbar-right">

            <form action="/search" class="navbar-form navbar-left">
                <div class="form-group">
                    <input type="text" name="search" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">Search</button>
            </form>

            <c:if test="${sessionScope.user == null}">
                <li><a href="/register">Register</a></li>
                <li><a href="/login">Login</a></li>
            </c:if>
            <c:if test="${sessionScope.user != null}">
                <li><a href="/posts">All posts</a></li>
                <li><a href="/posts/create">Create Post</a></li>
                <li><a href="/profile/">${user.username}'s Profile</a></li>
                <li><a href="/logout">Logout</a></li>
            </c:if>

        </ul>
    </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
