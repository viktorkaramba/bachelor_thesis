import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DriverCarRecommendationsRoutingModule} from "./drivercarrecommendations-routing.module";
import {
  DriverCarRecommendationsListComponent
} from "../drivercarrecommendations-list/drivercarrecommendations-list.component";
import {
  DrivercarrecommendationsMenuComponent
} from "../drivercarrecommendations-menu/drivercarrecommendations-menu.component";
import {SharedModule} from "../../../shared.module";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {
  DrivercarrecommendationsReportComponent
} from "../drivercarrecommendations-report/drivercarrecommendations-report.component";
import {
  DrivercarrecommendationsReportByIdComponent
} from "../drivercarrecommendations-report-by-id/drivercarrecommendations-report-by-id.component";


@NgModule({
  declarations: [DrivercarrecommendationsMenuComponent, DriverCarRecommendationsListComponent,
  DrivercarrecommendationsReportComponent, DrivercarrecommendationsReportByIdComponent],
  imports: [
    CommonModule,
    DriverCarRecommendationsRoutingModule,
    HttpClientModule,
    NgbTypeaheadModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule,
    NgbPaginationModule
  ],
  exports: [DriverCarRecommendationsListComponent],
  bootstrap: [DriverCarRecommendationsListComponent],
})
export class DriverCarRecommendationsModuleModule { }
