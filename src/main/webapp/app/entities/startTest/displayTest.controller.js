(function () {
    'use strict';

    angular
        .module('interviewApp')
        .controller('DisplayTestController', DisplayTestController);

    DisplayTestController.$inject = ['$scope', '$state', '$http', 'paginationConstants', '$stateParams'];

    function DisplayTestController($scope, $state, $http, paginationConstants, $stateParams) {
        var vm = this;

        vm.test = {};
        vm.show = true;
        console.log($stateParams.code)
        function getTests() {
            var params = {};
            params.code = $stateParams.code;
            $http({
                method: 'GET',
                url: 'api/startTest',
                params: params,
            }).then(function successCallback(response) {
                console.log(response)
                vm.questions = response.data;
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }

        getTests();

       vm.submit= function () {
            var params = {};
            params.code = $stateParams.code;
            $http({
                method: 'POST',
                url: 'api/displayTest',
                data: vm.questions,
                params:params
            }).then(function successCallback(response) {
                console.log(response)
                vm.show=false;
                vm.message = response.data.right;
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }
    }
})();
