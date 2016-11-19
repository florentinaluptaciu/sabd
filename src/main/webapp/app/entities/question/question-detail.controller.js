(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Question'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Question) {
        var vm = this;

        vm.question = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('interviewApp:questionUpdate', function(event, result) {
            vm.question = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
