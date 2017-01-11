(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestxquestionController', TestxquestionController);

    TestxquestionController.$inject = ['$scope', '$state', 'Testxquestion'];

    function TestxquestionController ($scope, $state, Testxquestion) {
        var vm = this;
        
        vm.testxquestions = [];

        loadAll();

        function loadAll() {
            Testxquestion.query(function(result) {
                vm.testxquestions = result;
            });
        }
    }
})();
