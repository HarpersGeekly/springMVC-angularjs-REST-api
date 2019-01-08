<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ryan.c.harper
  Date: 1/3/2019
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="app">
<head>
    <jsp:include page="/WEB-INF/views/partials/header.jsp">
        <jsp:param name="title" value="${post.title}" />
    </jsp:include>
</head>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />
<body>

<div ng-controller="postController">
    <div class="container" ng-init="fetchPost(${post.id})">

        <h3 ng-bind-html="post.htmlTitle">{{post.title}}</h3> <%-- use ng-bind-html for parsing the markdown to html--%>
        <h4 ng-bind-html="post.htmlSubtitle">{{post.subtitle}}</h4>

        By: <a href="/profile/{{post.user.id}}/{{post.user.username}}">{{post.user.username}}</a>
        <span>{{post.hoursMinutes}} <span>{{post.date}}</span></span>
        <i class="fas fa-thumbs-up"></i>{{post.voteCount}}<i class="far fa-eye"></i>{{post.hitCount}}
        <a href="/posts/{{post.id}}/{{post.title}}"><div ng-bind-html="post.htmlLeadImage" id="index-post-image">{{post.leadImage}}</div></a>

    </div>
</div>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
<script>

    let app = angular.module('app', []);

    app.controller('postController', function($scope, $http) {

        $scope.fetchPost = function(post) {
            $http({
                method: 'GET',
                url: '/posts/fetch/' + post.id,
            }).then(function (response) {
                console.log("success");
                console.log(response.data);
                $scope.posts = response.data;
                // $scope.postLimit = 3;  | limitTo:postLimit"
            }, function (error) {
                console.log("Get posts error: " + error);
            });
        };


    })

</script>




</body>
</html>
