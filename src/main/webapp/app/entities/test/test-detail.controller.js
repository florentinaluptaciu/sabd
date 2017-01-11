(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestDetailController', TestDetailController);

    TestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Test', 'Candidates'];

    function TestDetailController($scope, $rootScope, $stateParams, previousState, entity, Test, Candidates) {
        var vm = this;

        vm.test = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('interviewApp:testUpdate', function(event, result) {
            vm.test = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
