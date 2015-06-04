'use strict';

app.service('SellerModel', [
    '$http',
    '$q',
    function ($http, $q) {

        this.get = function () {
            var deferred = $q.defer();
            $http.get('/api/sellers').then(function (result) {
                deferred.resolve(result.data);
            }, function (error) {
                deferred.reject(error.data);
            });

            return deferred.promise;
        };

        this.create = function (seller) {
            var deferred = $q.defer();
            $http.post('/api/sellers', seller).then(function (result) {
                deferred.resolve(result.data);
            }, function (error) {
                deferred.reject(error.data);
            });

            return deferred.promise;
        };
    }
]);