import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  PricePerKilometerByTariffMenuComponent
} from "../price-per-kilometer-by-tariff-menu/price-per-kilometer-by-tariff-menu.component";
import {
  PricePerKilometerByTariffListComponent
} from "../priceperkilometerbytariff-list/priceperkilometerbytariff-list.component";

const routes: Routes = [
  { path: '', redirectTo: 'price-per-kilometer-by-tariff', pathMatch:'full'},
  { path: 'price-per-kilometer-by-tariff', component:PricePerKilometerByTariffMenuComponent,
    children:[
      { path: 'price-per-kilometer-by-tariffs', component: PricePerKilometerByTariffListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PricePerKilometerByTariffRoutingModule { }
