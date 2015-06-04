'use strict';

app.controller('auctionController', [
    '$scope',
    'AuctionModel',
    'SweetAlert',
    function ($scope, AuctionModel, SweetAlert) {
        $scope.create = function () {
            AuctionModel.create($scope.auction).then(function (seller) {
                console.log(seller);
                SweetAlert.swal("Success", "Bought " + seller.product.name + " from " + seller.name + " at price " + seller.product.price, "success");
            }).catch(function (error) {
                SweetAlert.swal("Oops", "There was a problem with the auction", "error");
            });
        };
    }
]);