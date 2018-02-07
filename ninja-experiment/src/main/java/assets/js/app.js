angular.module("app", [], function() {});

angular.module("app").factory("api", function($http) {
	return {
		addNumbersGet: function(a, b, onResult) {
			$http.get("/api/addNumbersGet", { params: {
				a: a,
				b: b
			}}).success(function(response) {
				onResult(response);
			});
		},
		
		addNumbersPost: function(a, b, onResult) {
			$http.post("/api/addNumbersPost", {
				a: a,
				b: b
			}).success(function(response) {
				onResult(response);
			});
		}
	};
});

angular.module("app").controller("AddNumbersController", function($scope, api) {
	$scope.a = 0;
	$scope.b = 0;
	$scope.getResult = "";
	$scope.postResult = "";
	
	$scope.run = function() {
		api.addNumbersGet($scope.a, $scope.b, function(response) {
			$scope.getResult = response; 
		});
		
		api.addNumbersPost($scope.a, $scope.b, function(response) {
			$scope.postResult = response; 
		});
	};
});