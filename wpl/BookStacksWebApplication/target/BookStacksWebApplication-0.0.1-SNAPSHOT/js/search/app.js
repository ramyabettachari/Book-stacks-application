
var app = angular.module('searchApp', ['ui.bootstrap']);
var query = getParameterByName("query");
app.filter('startFrom', function() {
    return function(input, start) {
        if(input) {
            start = +start; //parse to int
            return input.slice(start);
        }
        return [];
    }
});
app.controller('searchCrtl', function ($scope, $http, $timeout) {
	$scope.showLoader = true;
    $http.get('PostsController.do?cmd=postDetails&query='+query).success(function(data){
		$scope.showLoader = false;
        $scope.list = data;
        $scope.currentPage = 1; //current page
        $scope.entryLimit = 10000; //max no of items to display in a page
        $scope.filteredItems = $scope.list.length; //Initially for no filter  
        $scope.totalItems = $scope.list.length;
    });
    $scope.setPage = function(pageNo) {
        $scope.currentPage = pageNo;
    };
    $scope.filter = function() {
    	$http.get('PostsController.do?cmd=postDetails&query=').success(function(data){
    		$scope.showLoader = false;
    		
            $scope.list = data;
            $scope.currentPage = 1; //current page
            $scope.entryLimit = 10000; //max no of items to display in a page
            $scope.filteredItems = $scope.list.length; //Initially for no filter  
            $scope.totalItems = $scope.list.length;
        });
        $timeout(function() { 
            $scope.filteredItems = $scope.filtered.length;
        }, 10);
    };
    $scope.sort_by = function(predicate) {
        $scope.predicate = predicate;
        $scope.reverse = !$scope.reverse;
    };
});
