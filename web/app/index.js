(function() {

  angular.module('templates', []);
  var app = angular.module('app', ['ngStorage', 'ngRoute', 'ngCookies', 'templates', 'angular-jwt']);

  /* ************************  Config app module ***************************** */
  var appConfig = function($qProvider, $routeProvider, $locationProvider, $httpProvider, jwtOptionsProvider) {

    $qProvider.errorOnUnhandledRejections(false);
    $locationProvider.hashPrefix('');
    $locationProvider.html5Mode(true);

    var tokenGetter = function(options) {
      // Skip authentication for any requests ending in .html
      if (options.url.substr(options.url.length - 5) == '.html') {
        return null;
      }
      return localStorage.getItem('access_token');
    };
    tokenGetter.$inject = ['options'];
    var jwtOptionsConfig = {
      tokenGetter: tokenGetter,
      whiteListedDomains: ['localhost']
    };
    jwtOptionsProvider.config(jwtOptionsConfig);
    $httpProvider.interceptors.push('jwtInterceptor');

    /* Register error provider that shows message on failed requests or redirects to login page on
    unauthenticated requests */
    var ResponseIterceptor = function($q, $rootScope, $location) {
      var responseError = function(rejection) {
        var status = rejection.status;
        var method = rejection.config.method;
        var url = rejection.config.url;
        if (status == 401) {
          $location.path("/login");
        } else {
          $rootScope.error = method + " on " + url + " failed with status " + status;
        }
        return $q.reject(rejection);
      };
      return {
        responseError: responseError
      };
    };
    ResponseIterceptor.$inject = ['$q', '$rootScope', '$location'];
    $httpProvider.interceptors.push(ResponseIterceptor);

    /* Registers auth token interceptor, auth token is either passed by header 
    or by query parameter as soon as there is an authenticated user */
    var RequestInterceptor = function($q, $rootScope, $location, $cookieStore, $localStorage) {
      var request = function(config) {
        var userToken = $localStorage.userToken;
        if (angular.isDefined(userToken)) {
          config.headers['X-AUTH-TOKEN'] = JSON.stringify(userToken);
        }
        if (angular.isDefined(userToken) && $location.path() === '/login') {
          $location.path('/');
        }
        if (angular.isDefined($rootScope.accesstoken)) {
          config.headers.Authorization = 'Bearer ' + $rootScope.accesstoken.access_token;
        }
        return config;
      };
      return {
        request: request
      };
    };
    RequestInterceptor.$inject = ['$q', '$rootScope', '$location', '$cookieStore', '$localStorage'];
    $httpProvider.interceptors.push(RequestInterceptor);
  };

  appConfig.$inject = ['$qProvider', '$routeProvider', '$locationProvider', '$httpProvider', 'jwtOptionsProvider'];
  app.config(appConfig);

  /* *********************************************************************** */

  /* ************************  Run app module ***************************** */


  var AppRun = function($rootScope, $location, $cookieStore,
  UserService, $localStorage) {

  /* Reset error when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function() {
      delete $rootScope.error;
    });

    // Go to home page when the url is invalid
    $rootScope.$on('$stateNotFound', function() {
    //event.preventDefault();
      if (angular.isDefined($localStorage.userToken) && $localStorage.userToken !== null) {
        $location.path('/');
      }
    });

    $rootScope.$on('$routeChangeStart', function() {
      console.log('Route is changed');
    });

    $rootScope.initialized = true;
  };

  AppRun.$inject = ['$rootScope', '$location', '$cookieStore',
    'UserService', '$localStorage'];
  app.run(AppRun);

  /* *********************************************************************** */

}());
