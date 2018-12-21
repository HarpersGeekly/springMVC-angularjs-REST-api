<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

<body ng-app="myApp">
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<%--ng-init="JSON.parse('${jsonData}')--%>

<div class="container" ng-controller="editUserController as ctrl" ng-init="initMe(${user.id})">

    <h3>{{displayName}}'s profile</h3>

    <button class="btn" ng-click="toggleEditUserForm()">
        Edit Account
    </button>

    <form class="form-horizontal" ng-submit="saveUser()" ng-model="editUserForm" ng-show="editUserForm" >

        <h3>Edit User:</h3>
        <div class="container form-group">

            <input type="hidden" name="id" ng-model="originalUser.id" ng-init="originalUser.id='${user.id}'">

            <label for="userEditName">Username:</label>
            <input id="userEditName" type="text" name="username" ng-model="originalUser.username" ng-init="originalUser.username='${user.username}'" required>

            <label for="userEditEmail">Change email:</label>
            <input id="userEditEmail" type="text" name="email" ng-model="originalUser.email" ng-init="originalUser.email='${user.email}'" required>

        </div>
        <button class="btn btn-success">
             Save Changes
        </button>
    </form>

</div>


<%--<jsp:include page="/WEB-INF/views/partials/footer.jsp" />--%>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.5/angular.min.js"></script>
<script>

    let app = angular.module('myApp', []);

    app.controller('editUserController', function($scope, $http) { //$http will be used for accessing the server side data

        $scope.originalUser = {};

        $scope.editUserForm = false;
        $scope.toggleEditUserForm = function () {
            $scope.editUserForm = !$scope.editUserForm;
        };

        $scope.initMe = function(userId) {

             $http({
                 method: 'GET',
                 url: '/getUser/' + userId
             }).then(function (response) {
                 console.log("success");
                 console.log(response.data.username);
                 $scope.displayName = response.data.username;
             }, function(error) {
                 console.log("failure");
                 console.log(error);
             });

        };

        $scope.saveUser = function() {
            $http({
                method: 'POST',
                url: '/editUser/' + $scope.originalUser.id,
                data: JSON.stringify($scope.originalUser)
            }).then(function (response) {
                console.log(response);
                $scope.displayName = response.data.username;
                $scope.toggleEditUserForm();

            }, function(error) {
                console.log(error);
            })
        };
        
    });

</script>
</body>
</html>








<%--TODO--%>
<%--<button type="button" ng-click="deleteUser()" class="btn btn-danger">--%>
<%--Delete Account--%>
<%--</button>--%>

<%--TODO--%>
<%--<script src="<c:url value='/static/js/app.js' />"></script>--%>
<%--<script src="<c:url value='/static/js/service/user_service.js' />"></script>--%>
<%--<script src="<c:url value='/static/js/controller/user_controller.js' />"></script>--%>



            <%--// $scope.getUser = function () {--%>
<%--//     $http({--%>
<%--//         method: 'GET',--%>
<%--//         url: '/getUser/' + id--%>
<%--//     }).then((function (response) {--%>
<%--//         console.log(response);--%>
<%--//         $scope.username = response.data.username;--%>
<%--//         $scope.email = response.data.email;--%>
<%--//     }))--%>
<%--// };--%>

<%--// let url = '/editUser/' + $scope.originalUser.id.toString();--%>
<%--// $scope.saveUser = function () {--%>
<%--//     $http.post(url, JSON.stringify($scope.originalUser)--%>
<%--//     ).then((function (response) {--%>
<%--//         console.log(response);--%>
<%--//         $scope.displayName = response.user.username;--%>
<%--//--%>
<%--//     }, function(error) {--%>
<%--//         console.log(error);--%>
<%--//     }))--%>
<%--// };--%>


<!-- Button trigger modal -->
<%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">--%>
    <%--Edit Account--%>
<%--</button>--%>

<!-- Modal -->
<%--<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">--%>
    <%--<div class="modal-dialog" role="document">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header" id="modal-header">--%>
                <%--<h5 class="modal-title" id="modalLabel"></h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body">--%>

                <%--<div class="page">--%>
                    <%--<form>--%>

                    <%--</form>--%>

                <%--</div>--%>
            <%--</div>--%>

            <%--<div class="modal-footer" id="modal-footer">--%>

            <%--</div>--%>

        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<%--</div>--%>


