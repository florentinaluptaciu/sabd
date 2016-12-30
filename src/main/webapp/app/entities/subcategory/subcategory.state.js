(function() {
    'use strict';

    angular
        .module('interviewApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subcategory', {
            parent: 'entity',
            url: '/subcategory',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Subcategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subcategory/subcategories.html',
                    controller: 'SubcategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('subcategory-detail', {
            parent: 'entity',
            url: '/subcategory/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Subcategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subcategory/subcategory-detail.html',
                    controller: 'SubcategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Subcategory', function($stateParams, Subcategory) {
                    return Subcategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subcategory',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subcategory-detail.edit', {
            parent: 'subcategory-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subcategory/subcategory-dialog.html',
                    controller: 'SubcategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subcategory', function(Subcategory) {
                            return Subcategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subcategory.new', {
            parent: 'subcategory',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subcategory/subcategory-dialog.html',
                    controller: 'SubcategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subcategoryName: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subcategory', null, { reload: 'subcategory' });
                }, function() {
                    $state.go('subcategory');
                });
            }]
        })
        .state('subcategory.edit', {
            parent: 'subcategory',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subcategory/subcategory-dialog.html',
                    controller: 'SubcategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subcategory', function(Subcategory) {
                            return Subcategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subcategory', null, { reload: 'subcategory' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subcategory.delete', {
            parent: 'subcategory',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subcategory/subcategory-delete-dialog.html',
                    controller: 'SubcategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Subcategory', function(Subcategory) {
                            return Subcategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subcategory', null, { reload: 'subcategory' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
