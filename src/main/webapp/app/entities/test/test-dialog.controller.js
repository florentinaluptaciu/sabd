(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestDialogController', TestDialogController);

    TestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Test', 'Candidates','Subcategory'];

    function TestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Test, Candidates,Subcategory) {
        var vm = this;

        vm.test = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.candidates = Candidates.query();
        vm.test.subcategories = Subcategory.query();
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.test.id !== null) {
                Test.update(vm.test, onSaveSuccess, onSaveError);
            } else {
                Test.save(vm.test, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('interviewApp:testUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.answerDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
