import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WorkLoadDriversMenuComponent} from "../work-load-drivers-menu/work-load-drivers-menu.component";
import {WorkLoadDriversListComponent} from "../workloaddrivers-list/workloaddrivers-list.component";
import {WorkLoadDriversReportComponent} from "../work-load-drivers-report/work-load-drivers-report.component";
import {
  WorkLoadDriversReportByIdComponent
} from "../work-load-drivers-report-by-id/work-load-drivers-report-by-id.component";

const routes: Routes = [
  { path: '', redirectTo: 'work-load-driver', pathMatch:'full'},
  { path: 'work-load-driver', component:WorkLoadDriversMenuComponent,
    children:[
      { path: 'work-load-drivers', component: WorkLoadDriversListComponent},
      { path: 'work-load-drivers-report', component: WorkLoadDriversReportComponent},
      { path: 'work-load-drivers-report-by-id', component: WorkLoadDriversReportByIdComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WorkLoadDriversRoutingModule { }
