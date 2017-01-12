(function () {
        'use strict';

        angular
            .module('interviewApp')
            .config(stateConfig);

        stateConfig.$inject = ['$stateProvider'];

        function stateConfig($stateProvider) {
            $stateProvider

                .state('startTest', {
                    parent: 'app',
                    url: '/startTest',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'Start Test'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/startTest/startTest.html',
                            controller: 'StartTestController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {}


                })
                .state('displayTest', {
                    parent: 'app',
                    url: '/displayTest/{code}',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'displayTest'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/startTest/displayTest.html',
                            controller: 'DisplayTestController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {}


                })
        }
    }

    )();
