(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestxquestionDialogController', TestxquestionDialogController);

    TestxquestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Testxquestion', 'Test', 'Question'];

    function TestxquestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Testxquestion, Test, Question) {
        var vm = this;

        vm.testxquestion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tests = Test.query();
        vm.questions = Question.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.testxquestion.id !== null) {
                Testxquestion.update(vm.testxquestion, onSaveSuccess, onSaveError);
            } else {
                Testxquestion.save(vm.testxquestion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('interviewApp:testxquestionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
