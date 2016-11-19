(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('CandidatesDeleteController',CandidatesDeleteController);

    CandidatesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Candidates'];

    function CandidatesDeleteController($uibModalInstance, entity, Candidates) {
        var vm = this;

        vm.candidates = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Candidates.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
