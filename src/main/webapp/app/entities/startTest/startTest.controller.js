(function() {
    'use strict';

    angular
        .module('interviewApp')
        .controller('StartTestController', StartTestController);

    StartTestController.$inject = ['$scope', '$state', '$http','paginationConstants'];

    function StartTestController ($scope, $state, $http, paginationConstants) {
        var vm = this;
        vm.test = {};
    }
})();
