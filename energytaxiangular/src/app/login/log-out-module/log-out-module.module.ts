import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LogOutRoutingModule} from "./log-out-routing.module";
import {LogOutComponent} from "../log-out/log-out.component";


@NgModule({
  declarations: [LogOutComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    LogOutRoutingModule
  ]
})
export class LogOutModuleModule { }
