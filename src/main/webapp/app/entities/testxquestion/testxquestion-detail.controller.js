(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestxquestionDetailController', TestxquestionDetailController);

    TestxquestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Testxquestion', 'Test', 'Question'];

    function TestxquestionDetailController($scope, $rootScope, $stateParams, previousState, entity, Testxquestion, Test, Question) {
        var vm = this;

        vm.testxquestion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('interviewApp:testxquestionUpdate', function(event, result) {
            vm.testxquestion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
