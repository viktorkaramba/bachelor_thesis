import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {CarClassMenuComponent} from "../car-class-menu/car-class-menu.component";
import {CarClassListComponent} from "../carclass-list/carclass-list.component";
import {CarClassRoutingModule} from "./car-class-routingmodule";
import {CarClassFormComponent} from "../car-class-form/car-class-form.component";


@NgModule({
  declarations: [CarClassMenuComponent, CarClassListComponent, CarClassFormComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    CarClassRoutingModule
  ]
})
export class CarClassModuleModule { }
