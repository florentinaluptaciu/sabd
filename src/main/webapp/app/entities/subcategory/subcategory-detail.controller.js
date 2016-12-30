(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('SubcategoryDetailController', SubcategoryDetailController);

    SubcategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subcategory', 'Category'];

    function SubcategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Subcategory, Category) {
        var vm = this;

        vm.subcategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('interviewApp:subcategoryUpdate', function(event, result) {
            vm.subcategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
