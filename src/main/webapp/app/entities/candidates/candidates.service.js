(function() {
    'use strict';
    angular
        .module('interviewApp')
        .factory('Candidates', Candidates);

    Candidates.$inject = ['$resource'];

    function Candidates ($resource) {
        var resourceUrl =  'api/candidates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
