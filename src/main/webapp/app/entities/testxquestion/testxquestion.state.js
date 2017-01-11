(function() {
    'use strict';

    angular
        .module('interviewApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('testxquestion', {
            parent: 'entity',
            url: '/testxquestion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Testxquestions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/testxquestion/testxquestions.html',
                    controller: 'TestxquestionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('testxquestion-detail', {
            parent: 'entity',
            url: '/testxquestion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Testxquestion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/testxquestion/testxquestion-detail.html',
                    controller: 'TestxquestionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Testxquestion', function($stateParams, Testxquestion) {
                    return Testxquestion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'testxquestion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('testxquestion-detail.edit', {
            parent: 'testxquestion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testxquestion/testxquestion-dialog.html',
                    controller: 'TestxquestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Testxquestion', function(Testxquestion) {
                            return Testxquestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('testxquestion.new', {
            parent: 'testxquestion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testxquestion/testxquestion-dialog.html',
                    controller: 'TestxquestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                answer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('testxquestion', null, { reload: 'testxquestion' });
                }, function() {
                    $state.go('testxquestion');
                });
            }]
        })
        .state('testxquestion.edit', {
            parent: 'testxquestion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testxquestion/testxquestion-dialog.html',
                    controller: 'TestxquestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Testxquestion', function(Testxquestion) {
                            return Testxquestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('testxquestion', null, { reload: 'testxquestion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('testxquestion.delete', {
            parent: 'testxquestion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testxquestion/testxquestion-delete-dialog.html',
                    controller: 'TestxquestionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Testxquestion', function(Testxquestion) {
                            return Testxquestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('testxquestion', null, { reload: 'testxquestion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
