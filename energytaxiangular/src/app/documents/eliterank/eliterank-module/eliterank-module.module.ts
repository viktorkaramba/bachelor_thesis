import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {EliterankMenuComponent} from "../eliterank-menu/eliterank-menu.component";
import {EliterankListComponent} from "../eliterank-list/eliterank-list.component";
import {EliterankFormComponent} from "../eliterank-form/eliterank-form.component";
import {CarRoutingModule} from "../../car/car-module/car-routing.module";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Service} from "../../../service/service.service";
import {AppComponent} from "../../../app.component";
import {EliteRankRoutingModule} from "./eliterank-routing.module";



@NgModule({
  declarations: [EliterankMenuComponent, EliterankListComponent, EliterankFormComponent],
  imports: [
    CommonModule,
    EliteRankRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [Service],
  bootstrap: [AppComponent]
})
export class EliterankModuleModule { }
