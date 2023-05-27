import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {OrderMenuComponent} from "../order-menu/order-menu.component";
import {OrderRoutingModule} from "./order-routing.module";
import {OrderListComponent} from "../order-list/order-list.component";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {OrderReportComponent} from "../order-report/order-report.component";
import {OrderReportByIdComponent} from "../order-report-by-id/order-report-by-id.component";

@NgModule({
  declarations: [OrderMenuComponent, OrderListComponent, OrderReportComponent, OrderReportByIdComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        OrderRoutingModule,
        NgbTypeaheadModule,
        SharedModule,
        NgbPaginationModule
    ],
  exports: [OrderListComponent],
  bootstrap: [OrderListComponent]
})
export class OrderModuleModule { }
