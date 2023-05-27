import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {DriverMenuComponent} from "../driver-menu/driver-menu.component";
import {DriverListComponent} from "../driver-list/driver-list.component";
import {DriverRoutingModule} from "./driver-routing.module";


@NgModule({
  declarations: [DriverMenuComponent, DriverListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    DriverRoutingModule
  ]
})
export class DriverModuleModule { }
