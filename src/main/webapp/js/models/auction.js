'use strict';

app.service('AuctionModel', [
    '$http',
    '$q',
    function ($http, $q) {

        this.create = function (auction) {
            var deferred = $q.defer();
            $http.post('/api/auction', auction).then(function (result) {
                deferred.resolve(result.data);
            }, function (error) {
                deferred.reject(error.data);
            });

            return deferred.promise;
        };
    }
]);