'use strict';

app.controller('sellersController', [
    '$scope',
    '$modal',
    'SellerModel',
    'toastr',
    'SweetAlert',
    'sellers',
    function ($scope, $modal, SellerModel, toastr, SweetAlert, sellers) {
        $scope.items = sellers;
        $scope.selectedItem = $scope.items[0];

        $scope.selectItem = function (item) {
            for (var i = 0; i < $scope.items.length; ++i) {
                $scope.items[i].selected = false;
            }
            item.selected = true;
            $scope.selectedItem = item;
        };

        $scope.openAddModal = function () {

            var modalInstance = $modal.open({
                templateUrl: '/tpl/dialogs/SellersAddModal.html',
                controller: 'sellers.modalCtrl',
                resolve: {}
            });

            modalInstance.result.then(function (newSeller) {
                SellerModel.create(newSeller).then(function (created) {
                    $scope.items.push(newSeller);
                    $scope.selectedItem = newSeller;
                    toastr.success('Seller added', 'Success');
                }).catch(function (error) {
                    switch(error.status){
                        case 400:
                            SweetAlert.swal("Oops", "Bad request", "error");
                            break;
                        default:
                            SweetAlert.swal("Oops", "There was a problem adding the seller", "error");
                    }
                });
            });
        };
    }
]);

app.controller('sellers.modalCtrl', [
    '$scope',
    '$modalInstance',
    function ($scope, $modalInstance) {

        $scope.ok = function () {
            $modalInstance.close($scope.newItem);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }
]);