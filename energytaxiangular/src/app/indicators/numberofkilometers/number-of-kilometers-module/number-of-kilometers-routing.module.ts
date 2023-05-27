import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NumberOfKilometersMenuComponent} from "../number-of-kilometers-menu/number-of-kilometers-menu.component";
import {NumberOfKilometersListComponent} from "../numberofkilometers-list/numberofkilometers-list.component";
import {NumberOfKilometersReportComponent} from "../number-of-kilometers-report/number-of-kilometers-report.component";
import {
  NumberOfKilometersReportByIdComponent
} from "../number-of-kilometers-report-by-id/number-of-kilometers-report-by-id.component";


const routes: Routes = [
  { path: '', redirectTo: 'number-of-kilometer', pathMatch:'full'},
  { path: 'number-of-kilometer', component:NumberOfKilometersMenuComponent,
    children:[
      { path: 'number-of-kilometers', component: NumberOfKilometersListComponent},
      { path: 'number-of-kilometers-report', component: NumberOfKilometersReportComponent},
      { path: 'number-of-kilometers-report-by-id', component: NumberOfKilometersReportByIdComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NumberOfKilometersRoutingModule { }
