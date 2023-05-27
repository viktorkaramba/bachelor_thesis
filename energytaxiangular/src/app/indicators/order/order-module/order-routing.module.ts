import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OrderMenuComponent} from "../order-menu/order-menu.component";
import {OrderListComponent} from "../order-list/order-list.component";
import {OrderReportComponent} from "../order-report/order-report.component";
import {OrderReportByIdComponent} from "../order-report-by-id/order-report-by-id.component";

const routes: Routes = [
  { path: '', redirectTo: 'order', pathMatch:'full'},
  { path: 'order', component:OrderMenuComponent,
    children:[
      { path: 'orders', component: OrderListComponent},
      { path: 'orders-report', component: OrderReportComponent},
      { path: 'orders-report-by-id', component: OrderReportByIdComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
