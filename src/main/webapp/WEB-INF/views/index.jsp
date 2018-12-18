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
    <title>Hello AngularJS</title>
</head>
<body>

    <div data-ng-controller="Hello">

        Your name: <input data-ng-model="name">
        You entered: {{ name }}
        <button data-ng-click="update()" data-ng-enter="update()">Update</button>

        <p>The count is {{greeting.id}}</p>
        <p>The content is {{greeting.content}}</p>

    </div>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.5/angular.min.js"></script>
<script>

    let app = angular.module('app', []);

    app.controller('Hello', function($scope, $http) { //name is the same as the controller in index.jsp
        //$http wil be used for accessing the server side data
        // will are using a get call, accessing the greeting path
        $http({
            method: 'GET',
            url: '/greeting'
        }).then((function(response) {
            console.log(response);
                $scope.greeting = response.data; // in the view, index.jsp, this will refer and synchronized to {{greeting.id}} {greeting.content}}. Technically, {{$scope.greeting.id}}
            }),

        $scope.update = function() {
            $http.get('greeting', {
                params: {name: $scope.name}
            }).then((function(response) {
                $scope.greeting = response.data;
            }))
        })

    })

</script>
</body>
</html>
