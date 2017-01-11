(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestxquestionDeleteController',TestxquestionDeleteController);

    TestxquestionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Testxquestion'];

    function TestxquestionDeleteController($uibModalInstance, entity, Testxquestion) {
        var vm = this;

        vm.testxquestion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Testxquestion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
