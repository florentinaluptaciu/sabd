(function() {
    'use strict';

    angular
        .module('interviewApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('test', {
            parent: 'entity',
            url: '/test',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test/tests.html',
                    controller: 'TestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('test-detail', {
            parent: 'entity',
            url: '/test/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Test'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test/test-detail.html',
                    controller: 'TestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Test', function($stateParams, Test) {
                    return Test.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'test',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('test-detail.edit', {
            parent: 'test-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test/test-dialog.html',
                    controller: 'TestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Test', function(Test) {
                            return Test.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test.new', {
            parent: 'test',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test/test-dialog.html',
                    controller: 'TestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                answerDate: null,
                                testCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('test', null, { reload: 'test' });
                }, function() {
                    $state.go('test');
                });
            }]
        })
        .state('test.edit', {
            parent: 'test',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test/test-dialog.html',
                    controller: 'TestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Test', function(Test) {
                            return Test.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test', null, { reload: 'test' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test.delete', {
            parent: 'test',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test/test-delete-dialog.html',
                    controller: 'TestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Test', function(Test) {
                            return Test.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test', null, { reload: 'test' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
