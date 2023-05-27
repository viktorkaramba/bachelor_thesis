import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {IncomeCarsMenuComponent} from "../income-cars-menu/income-cars-menu.component";
import {IncomeCarsListComponent} from "../incomecars-list/incomecars-list.component";
import {IncomeCarsReportByIdComponent} from "../income-cars-report-by-id/income-cars-report-by-id.component";
import {IncomeCarsReportComponent} from "../income-cars-report/income-cars-report.component";


const routes: Routes = [
  { path: '', redirectTo: 'income-car', pathMatch:'full'},
  { path: 'income-car', component:IncomeCarsMenuComponent,
    children:[
      { path: 'income-cars', component: IncomeCarsListComponent},
      { path: 'income-cars-report', component: IncomeCarsReportComponent},
      { path: 'income-cars-report-by-id', component: IncomeCarsReportByIdComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IncomeCarsRoutingModule { }
