
(function() {

  angular.module('app').config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when("/", {
        templateUrl: "features/home/home.html"
      });
  }]);

  var HomeController = function() {

  };

  angular.module('app').controller('HomeController', HomeController);

}());
