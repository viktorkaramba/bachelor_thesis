import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {UserMenuComponent} from "../user-menu/user-menu.component";
import {UserListComponent} from "../user-list/user-list.component";
import {UserRoutingModule} from "./user-routing.module";
import {UserFormComponent} from "../user-form/user-form.component";


@NgModule({
  declarations: [UserMenuComponent, UserListComponent, UserFormComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    UserRoutingModule,
    FormsModule
  ]
})
export class UserModuleModule { }
