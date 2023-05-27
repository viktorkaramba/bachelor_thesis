import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CarMenuComponent} from "../car-menu/car-menu.component";
import {CarListComponent} from "../car-list/car-list.component";
import {CarFormComponent} from "../car-form/car-form.component";
import {CarRoutingModule} from "./car-routing.module";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {Service} from "../../../service/service.service";
import {AppComponent} from "../../../app.component";


@NgModule({
  declarations: [CarMenuComponent, CarListComponent, CarFormComponent],
  imports: [
    CommonModule,
    CarRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [Service],
  bootstrap: [AppComponent]
})
export class CarModuleModule { }
