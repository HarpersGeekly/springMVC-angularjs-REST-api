<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<h3>{{post.title}}</h3>
<h4>{{post.subtitle}}</h4>
<div>
    <img style="width:200px" src="{{post.leadImage}}"/>
</div>
<div>{{post.hoursMinutes}} <span style="margin-left:20px">{{post.date}}</span></div>

    <button ng-click="deletePost(post)">Delete Post</button>

</body>
</html>
