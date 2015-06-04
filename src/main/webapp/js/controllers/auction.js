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
                console.log(error);
                switch(error.status){
                    case 400:
                        SweetAlert.swal("Oops", "Bad request", "error");
                        break;
                    case 404:
                        SweetAlert.swal("Oops", "No sellers found", "error");
                        break;
                    default:
                        SweetAlert.swal("Oops", "There was a problem with the auction", "error");
                }
            });
        };
    }
]);