'use strict';

/**
 * Config for the router
 */
angular.module('app')
    .run(
    [
        '$rootScope',
        '$state',
        '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ])
    .config(
    [
        '$stateProvider',
        '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider
                .otherwise(function ($injector, $location) {
                    var $state = $injector.get("$state");
                    $state.go("app");
                });
            $stateProvider
                .state('app', {
                    templateUrl: 'tpl/root.html',
                    resolve: {}
                });
            $stateProvider
                .state('app.sellers', {
                    url: '/sellers',
                    templateUrl: 'tpl/sellers.html',
                    controller: 'sellersController',
                    resolve: {
                        sellers: [
                            'SellerModel',
                            function (SellerModel) {
                                return SellerModel.get();
                            }
                        ]
                    }
                });
            $stateProvider
                .state('app.auction', {
                    url: '/auction',
                    templateUrl: 'tpl/auction.html',
                    controller: 'auctionController',
                    resolve: {}
                });
        }
    ]);
