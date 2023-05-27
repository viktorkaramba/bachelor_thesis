import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {MaintenanceCostsCarsMenuComponent} from "../maintenance-costs-cars-menu/maintenance-costs-cars-menu.component";
import {MaintenanceCostsCarsListComponent} from "../maintenancecostscars-list/maintenancecostscars-list.component";
import {MaintenanceCostsCarsRouterModule} from "./maintenance-costs-cars-router.module";
import {MaintenanceCostsCarFormComponent} from "../maintenance-costs-car-form/maintenance-costs-car-form.component";


@NgModule({
  declarations: [MaintenanceCostsCarsMenuComponent, MaintenanceCostsCarsListComponent, MaintenanceCostsCarFormComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    MaintenanceCostsCarsRouterModule
  ]
})
export class MaintenanceCostsCarsModuleModule { }
