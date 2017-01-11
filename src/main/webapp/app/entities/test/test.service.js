(function() {
    'use strict';
    angular
        .module('interviewApp')
        .factory('Test', Test);

    Test.$inject = ['$resource', 'DateUtils'];

    function Test ($resource, DateUtils) {
        var resourceUrl =  'api/tests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.answerDate = DateUtils.convertLocalDateFromServer(data.answerDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.answerDate = DateUtils.convertLocalDateToServer(copy.answerDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.answerDate = DateUtils.convertLocalDateToServer(copy.answerDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
