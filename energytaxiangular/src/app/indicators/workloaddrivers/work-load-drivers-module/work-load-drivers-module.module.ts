import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {WorkLoadDriversMenuComponent} from "../work-load-drivers-menu/work-load-drivers-menu.component";
import {WorkLoadDriversListComponent} from "../workloaddrivers-list/workloaddrivers-list.component";
import {WorkLoadDriversRoutingModule} from "./work-load-drivers-routing.module";
import {SharedModule} from "../../../shared.module";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {WorkLoadDriversReportComponent} from "../work-load-drivers-report/work-load-drivers-report.component";
import {
  WorkLoadDriversReportByIdComponent
} from "../work-load-drivers-report-by-id/work-load-drivers-report-by-id.component";


@NgModule({
  declarations: [WorkLoadDriversMenuComponent, WorkLoadDriversListComponent, WorkLoadDriversReportComponent,
  WorkLoadDriversReportByIdComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        FormsModule,
        WorkLoadDriversRoutingModule,
        SharedModule,
        NgbTypeaheadModule,
        NgbPaginationModule
    ]
})
export class WorkLoadDriversModuleModule { }
