import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ManagerComponent} from "./manager.component";

const routes: Routes = [
  { path: '', component: ManagerComponent,
  children: [
    { path: 'car', loadChildren:() => import('../documents/car/car-module/car-module.module').then(m => m.CarModuleModule)},
    { path: 'driver', loadChildren:() => import('../documents/driver/driver-module/driver-module.module')
        .then(m => m.DriverModuleModule) },
    { path: 'car-class', loadChildren:() => import('../documents/carclass/car-class-module/car-class-module.module')
        .then(m => m.CarClassModuleModule)},
    { path: 'fullname', loadChildren:() => import('../documents/fullname/fullname-module/fullname-module.module')
        .then(m => m.FullNameModuleModule) },
    { path: 'maintenance-costs-car', loadChildren:() => import('../documents/maintenancecostscars/maintenance-costs-cars-module/maintenance-costs-cars-module.module')
        .then(m => m.MaintenanceCostsCarsModuleModule) },
    { path: 'price-per-kilometer-by-tariff', loadChildren:() => import('../documents/priceperkilometerbytariff/price-per-kilometer-by-tariff-module/price-per-kilometer-by-tariff-module.module')
        .then(m => m.PricePerKilometerByTariffModuleModule) },
    { path: 'user', loadChildren:() => import('../documents/user/user-module/user-module.module').then(m => m.UserModuleModule) },
    { path: 'rank', loadChildren:() => import('../documents/rank/rank-module/rank-module.module').then(m => m.RankModuleModule) },
    { path: 'elite-rank', loadChildren:() => import('../documents/eliterank/eliterank-module/eliterank-module.module').then(m => m.EliterankModuleModule) },
    { path: 'address', loadChildren:() => import('../indicators/address/address-module/address-module.module').then(m => m.AddressModuleModule) },
    { path: 'driver-car-recommendation', loadChildren:() => import('../indicators/drivercarrecommendations/drivercarrecommendations-module/drivercarrecommendations-module.module').then(m => m.DriverCarRecommendationsModuleModule) },
    { path: 'driver-rating', loadChildren:() => import('../indicators/driverrating/driver-rating-module/driver-rating-module.module').then(m => m.DriverRatingModuleModule) },
    { path: 'income-car', loadChildren:() => import('../indicators/incomecars/income-cars-module/income-cars-module.module').then(m => m.IncomeCarsModuleModule) },
    { path: 'number-of-kilometer', loadChildren:() => import('../indicators/numberofkilometers/number-of-kilometers-module/number-of-kilometers-module.module').then(m => m.NumberOfKilometersModuleModule) },
    { path: 'order', loadChildren:() => import('../indicators/order/order-module/order-module.module').then(m => m.OrderModuleModule) },
    { path: 'work-load-driver', loadChildren:() => import('../indicators/workloaddrivers/work-load-drivers-module/work-load-drivers-module.module').then(m => m.WorkLoadDriversModuleModule) },
    { path: 'favourite-address', loadChildren:() => import('../indicators/favouriteaddresses/favouriteaddresses-module/favouriteaddresses-module.module')
        .then(m => m.FavouriteaddressesModuleModule) },
    { path: 'favourite-driver', loadChildren:() => import('../indicators/favouritedrivers/favouritedrivers-module/favouritedrivers-module.module')
        .then(m => m.FavouritedriversModuleModule) },
    { path: 'user-rank-achievement-info', loadChildren:() => import('../indicators/userrankachievementinfo/userrankachievementinfo-module/userrankachievementinfo-module.module')
        .then(m => m.UserrankachievementinfoModuleModule) },
    { path: 'user-elite-rank-achievement-info', loadChildren:() => import('../indicators/usereliterankachievementinfo/usereliterankachievementinfo-module/usereliterankachievementinfo-module.module')
        .then(m => m.UsereliterankachievementinfoModuleModule) },
    { path: 'military-bonus', loadChildren:() => import('../indicators/militarybonuses/militarybonuses-module/militarybonuses-module.module')
        .then(m => m.MilitarybonusesModuleModule) },
    { path: 'message', loadChildren:() => import('../message-list/message-list-module.module')
        .then(m => m.MessageListModuleModule) },
  ]},
  { path: 'logout', loadChildren:() => import('../login/log-out-module/log-out-module.module').then(m => m.LogOutModuleModule)},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagerRoutingModule { }
