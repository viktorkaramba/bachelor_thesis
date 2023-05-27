import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LoginFormComponent} from "../login-form/login-form.component";
import {LoginRoutingModule} from "./login-routing.module";


@NgModule({
  declarations: [LoginFormComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    LoginRoutingModule
  ]
})
export class LoginModuleModule { }
