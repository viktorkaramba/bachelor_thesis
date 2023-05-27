import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DriverRatingMenuComponent} from "../driver-rating-menu/driver-rating-menu.component";
import {DriverRatingListComponent} from "../driverrating-list/driverrating-list.component";
import {DriverRatingsReportByIdComponent} from "../driver-ratings-report-by-id/driver-ratings-report-by-id.component";
import {DriverRatingsReportComponent} from "../driver-ratings-report/driver-ratings-report.component";


const routes: Routes = [
  { path: '', redirectTo: 'driver-rating', pathMatch:'full'},
  { path: 'driver-rating', component: DriverRatingMenuComponent,
    children:[
      { path: 'driver-ratings', component: DriverRatingListComponent},
      { path: 'driver-ratings-report', component: DriverRatingsReportComponent},
      { path: 'driver-ratings-report-by-id', component: DriverRatingsReportByIdComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRatingRoutingModule { }
