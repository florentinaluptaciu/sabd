(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('CandidatesDialogController', CandidatesDialogController);

    CandidatesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Candidates'];

    function CandidatesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Candidates) {
        var vm = this;

        vm.candidates = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.candidates.id !== null) {
                Candidates.update(vm.candidates, onSaveSuccess, onSaveError);
            } else {
                Candidates.save(vm.candidates, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('interviewApp:candidatesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
