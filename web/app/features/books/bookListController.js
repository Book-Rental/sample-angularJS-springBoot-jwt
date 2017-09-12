(function() {

  var routeConfig = function($routeProvider) {
    $routeProvider
      .when('/books', {
        templateUrl: 'features/books/bookList.html'
      });
  };
  routeConfig.$inject = ['$routeProvider'];
  angular.module('app').config(routeConfig);

  var BookListController = function($scope, $http, $location, BookService, $routeParams) {

    var initDatatableEvents = function(dataTableElement) {
      dataTableElement.find('tbody tr').on('dblclick', function() {
        var selectedBook = dataTableElement.DataTable().row(this).data();
        $location.path('/book/' + selectedBook.id);
        $scope.$apply();
      });
    };

    var initDataTable = function(bookList) {
      var bookTable = $('#example');
      bookTable.DataTable({
        dom: 'Bfrtip',
        buttons: [
          'copy', 'csv', 'excel', 'pdf', 'print'
        ],
        data: bookList,
        columns: [
          {data: 'title'},
          {data: 'author'},
          {data: 'publicationDate'}
        ]
      });
      initDatatableEvents(bookTable);
    };

    BookService.get().then(function(response) {
      $scope.books = response.data;
      initDataTable($scope.books);
    });

  };
  BookListController.$inject = ['$scope', '$http', '$location', 'BookService', '$routeParams'];

  angular.module('app').controller('BookListController', BookListController);

}());
