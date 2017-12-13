
var app = angular.module('cartApp', ['ui.bootstrap']);

app.filter('startFrom', function() {
    return function(input, start) {
        if(input) {
            start = +start; //parse to int
            return input.slice(start);
        }
        return [];
    }
});
app.controller('cartCrtl', function ($scope, $http, $timeout) {
	$scope.showLoader = true;
    $http.get('CartController.do?cmd=cartdetails').success(function(data){
		$scope.showLoader = false;
        $scope.list = data;
        $scope.currentPage = 1; //current page
        $scope.entryLimit = 10; //max no of items to display in a page
        $scope.filteredItems = $scope.list.length; //Initially for no filter  
        $scope.totalItems = $scope.list.length;
    });
    $scope.setPage = function(pageNo) {
        $scope.currentPage = pageNo;
    };
    $scope.filter = function() {
    	$http.get('CartController.do?cmd=cartdetails').success(function(data){
    		$scope.showLoader = false;
            $scope.list = data;
            $scope.currentPage = 1; //current page
            $scope.entryLimit = 10; //max no of items to display in a page
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
    $scope.qtyIncrement = function(item) {
        item.quantity++;
    };
    $scope.qtyDecrement = function(item) {
    	if(item.quantity > 1){
            item.quantity--;
    	}
    };
    $scope.getTotal = function(){
        var total = 0;
        for(var i = 0; i < $scope.totalItems; i++){
            var product = $scope.list[i];
            total += (product.bidamount * product.quantity);
        }
        return total;
    };
    $scope.reload = function(){
    	$http.get('CartController.do?cmd=cartdetails').success(function(data){
    		$scope.showLoader = false;
            $scope.list = data;
            $scope.currentPage = 1; //current page
            $scope.entryLimit = 10; //max no of items to display in a page
            $scope.filteredItems = $scope.list.length; //Initially for no filter  
            $scope.totalItems = $scope.list.length;
        });
    };
});
