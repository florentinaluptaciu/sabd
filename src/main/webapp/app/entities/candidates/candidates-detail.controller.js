(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('CandidatesDetailController', CandidatesDetailController);

    CandidatesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Candidates'];

    function CandidatesDetailController($scope, $rootScope, $stateParams, previousState, entity, Candidates) {
        var vm = this;

        vm.candidates = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('interviewApp:candidatesUpdate', function(event, result) {
            vm.candidates = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
