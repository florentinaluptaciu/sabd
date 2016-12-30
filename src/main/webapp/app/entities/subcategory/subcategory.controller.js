(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('SubcategoryController', SubcategoryController);

    SubcategoryController.$inject = ['$scope', '$state', 'Subcategory'];

    function SubcategoryController ($scope, $state, Subcategory) {
        var vm = this;
        
        vm.subcategories = [];

        loadAll();

        function loadAll() {
            Subcategory.query(function(result) {
                vm.subcategories = result;
            });
        }
    }
})();
