import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  DrivercarrecommendationsMenuComponent
} from "../drivercarrecommendations-menu/drivercarrecommendations-menu.component";
import {
  DriverCarRecommendationsListComponent
} from "../drivercarrecommendations-list/drivercarrecommendations-list.component";
import {
  DrivercarrecommendationsReportComponent
} from "../drivercarrecommendations-report/drivercarrecommendations-report.component";
import {
  DrivercarrecommendationsReportByIdComponent
} from "../drivercarrecommendations-report-by-id/drivercarrecommendations-report-by-id.component";


const routes: Routes = [
  { path: '', redirectTo: 'driver-car-recommendation', pathMatch:'full'},
  { path: 'driver-car-recommendation', component:DrivercarrecommendationsMenuComponent,
    children:[
      { path: 'driver-car-recommendations', component: DriverCarRecommendationsListComponent},
      { path: 'driver-car-recommendations-report', component: DrivercarrecommendationsReportComponent},
      { path: 'driver-car-recommendations-report-by-id', component: DrivercarrecommendationsReportByIdComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverCarRecommendationsRoutingModule { }
