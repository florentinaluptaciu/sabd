(function() {
    'use strict';

    angular
        .module('interviewApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('candidates', {
            parent: 'entity',
            url: '/candidates?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Candidates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/candidates/candidates.html',
                    controller: 'CandidatesController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('candidates-detail', {
            parent: 'entity',
            url: '/candidates/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Candidates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/candidates/candidates-detail.html',
                    controller: 'CandidatesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Candidates', function($stateParams, Candidates) {
                    return Candidates.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'candidates',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('candidates-detail.edit', {
            parent: 'candidates-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/candidates/candidates-dialog.html',
                    controller: 'CandidatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Candidates', function(Candidates) {
                            return Candidates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('candidates.new', {
            parent: 'candidates',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/candidates/candidates-dialog.html',
                    controller: 'CandidatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phoneNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('candidates', null, { reload: 'candidates' });
                }, function() {
                    $state.go('candidates');
                });
            }]
        })
        .state('candidates.edit', {
            parent: 'candidates',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/candidates/candidates-dialog.html',
                    controller: 'CandidatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Candidates', function(Candidates) {
                            return Candidates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('candidates', null, { reload: 'candidates' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('candidates.delete', {
            parent: 'candidates',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/candidates/candidates-delete-dialog.html',
                    controller: 'CandidatesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Candidates', function(Candidates) {
                            return Candidates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('candidates', null, { reload: 'candidates' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
