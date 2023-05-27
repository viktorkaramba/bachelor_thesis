import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DriverRatingMenuComponent} from "../driver-rating-menu/driver-rating-menu.component";
import {DriverRatingRoutingModule} from "./driver-rating-routing.module";
import {DriverRatingListComponent} from "../driverrating-list/driverrating-list.component";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {DriverRatingsReportComponent} from "../driver-ratings-report/driver-ratings-report.component";
import {DriverRatingsReportByIdComponent} from "../driver-ratings-report-by-id/driver-ratings-report-by-id.component";

@NgModule({
  declarations: [DriverRatingMenuComponent,DriverRatingListComponent, DriverRatingsReportComponent,
    DriverRatingsReportByIdComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        DriverRatingRoutingModule,
        ReactiveFormsModule,
        FormsModule,
        NgbTypeaheadModule,
        SharedModule,
        NgbPaginationModule
    ],
  exports: [DriverRatingListComponent],
  bootstrap: [DriverRatingListComponent],
})
export class DriverRatingModuleModule { }
