import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MaintenanceCostsCarsMenuComponent} from "../maintenance-costs-cars-menu/maintenance-costs-cars-menu.component";
import {MaintenanceCostsCarsListComponent} from "../maintenancecostscars-list/maintenancecostscars-list.component";
import {MaintenanceCostsCarFormComponent} from "../maintenance-costs-car-form/maintenance-costs-car-form.component";


const routes: Routes = [
  { path: '', redirectTo: 'maintenance-costs-car', pathMatch:'full'},
  { path: 'maintenance-costs-car', component:MaintenanceCostsCarsMenuComponent,
    children:[
      { path: 'maintenance-costs-cars', component: MaintenanceCostsCarsListComponent},
      { path: 'add-maintenance-costs-car', component:  MaintenanceCostsCarFormComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MaintenanceCostsCarsRouterModule { }
