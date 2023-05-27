import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {FullNameMenuComponent} from "../fullname-menu/fullname-menu.component";
import {FullNameListComponent} from "../fullname-list/fullname-list.component";
import {FullNameRoutingModule} from "./fullname-routing.module";


@NgModule({
  declarations: [FullNameMenuComponent, FullNameListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    FullNameRoutingModule
  ]
})
export class FullNameModuleModule { }
