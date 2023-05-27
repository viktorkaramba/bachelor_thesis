import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {
  PricePerKilometerByTariffMenuComponent
} from "../price-per-kilometer-by-tariff-menu/price-per-kilometer-by-tariff-menu.component";
import {
  PricePerKilometerByTariffListComponent
} from "../priceperkilometerbytariff-list/priceperkilometerbytariff-list.component";
import {PricePerKilometerByTariffRoutingModule} from "./price-per-kilometer-by-tariff-routing.module";


@NgModule({
  declarations: [
    PricePerKilometerByTariffMenuComponent,
    PricePerKilometerByTariffListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    PricePerKilometerByTariffRoutingModule
  ]
})
export class PricePerKilometerByTariffModuleModule { }
