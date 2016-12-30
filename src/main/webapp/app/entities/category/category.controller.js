(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('CategoryController', CategoryController);

    CategoryController.$inject = ['$scope', '$state', 'Category'];

    function CategoryController ($scope, $state, Category) {
        var vm = this;
        
        vm.categories = [];

        loadAll();

        function loadAll() {
            Category.query(function(result) {
                vm.categories = result;
            });
        }
    }
})();
