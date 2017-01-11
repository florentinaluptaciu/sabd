(function() {
    'use strict';
    angular
        .module('interviewApp')
        .factory('Testxquestion', Testxquestion);

    Testxquestion.$inject = ['$resource'];

    function Testxquestion ($resource) {
        var resourceUrl =  'api/testxquestions/:id';

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
