(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Question', 'Subcategory'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Question, Subcategory) {
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
