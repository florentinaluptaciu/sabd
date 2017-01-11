(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$scope', '$state', 'Test'];

    function TestController ($scope, $state, Test) {
        var vm = this;
        
        vm.tests = [];

        loadAll();

        function loadAll() {
            Test.query(function(result) {
                vm.tests = result;
            });
        }
    }
})();
