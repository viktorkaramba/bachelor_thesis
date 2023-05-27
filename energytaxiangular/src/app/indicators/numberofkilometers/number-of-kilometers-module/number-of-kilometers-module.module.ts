import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NumberOfKilometersMenuComponent} from "../number-of-kilometers-menu/number-of-kilometers-menu.component";
import {NumberOfKilometersListComponent} from "../numberofkilometers-list/numberofkilometers-list.component";
import {NumberOfKilometersRoutingModule} from "./number-of-kilometers-routing.module";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {NumberOfKilometersReportComponent} from "../number-of-kilometers-report/number-of-kilometers-report.component";
import {
  NumberOfKilometersReportByIdComponent
} from "../number-of-kilometers-report-by-id/number-of-kilometers-report-by-id.component";

@NgModule({
  declarations: [NumberOfKilometersMenuComponent, NumberOfKilometersListComponent, NumberOfKilometersReportComponent,
  NumberOfKilometersReportByIdComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        FormsModule,
        NumberOfKilometersRoutingModule,
        ReactiveFormsModule,
        NgbTypeaheadModule,
        SharedModule,
        NgbPaginationModule
    ],
  exports: [NumberOfKilometersListComponent],
  bootstrap: [NumberOfKilometersListComponent],
})
export class NumberOfKilometersModuleModule { }
