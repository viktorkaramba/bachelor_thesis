import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {IncomeCarsMenuComponent} from "../income-cars-menu/income-cars-menu.component";
import {IncomeCarsListComponent} from "../incomecars-list/incomecars-list.component";
import {IncomeCarsRoutingModule} from "./income-cars-routing.module";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {IncomeCarsReportComponent} from "../income-cars-report/income-cars-report.component";
import {IncomeCarsReportByIdComponent} from "../income-cars-report-by-id/income-cars-report-by-id.component";

@NgModule({
  declarations: [IncomeCarsMenuComponent, IncomeCarsListComponent, IncomeCarsReportComponent, IncomeCarsReportByIdComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        FormsModule,
        IncomeCarsRoutingModule,
        ReactiveFormsModule,
        NgbTypeaheadModule,
        SharedModule,
        NgbPaginationModule
    ],
  exports: [IncomeCarsListComponent],
  bootstrap: [IncomeCarsListComponent],
})
export class IncomeCarsModuleModule { }
