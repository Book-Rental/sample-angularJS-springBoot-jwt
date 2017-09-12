(function() {

  angular.module('app').config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/login', {
        templateUrl: 'common/login/login.html'
      });
  }]);

  var LoginController = function($window, $http, $location, $rootScope, $scope, UserService,
  $cookieStore, $localStorage, $route, StorageService) {

    $scope.credentials = {};
    $scope.login = function() {
      var data = $.param({login: $scope.credentials.login, password: $scope.credentials.password});
      UserService.login(data).then(function(response) {
        var accessToken = response.data;
        StorageService.setLocalItem('access_token', accessToken);
        $route.reload();
      });
    };
  };

  LoginController.$inject = ['$window', '$http', '$location', '$rootScope', '$scope', 'UserService',
    '$cookieStore', '$localStorage', '$route', 'StorageService'];
  angular.module('app').controller('LoginController', LoginController);

}());
