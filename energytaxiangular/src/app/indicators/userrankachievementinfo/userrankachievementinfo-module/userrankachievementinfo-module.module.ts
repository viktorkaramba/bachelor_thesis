import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {
  UserrankachievementinfoMenuComponent
} from "../userrankachievementinfo-menu/userrankachievementinfo-menu.component";
import {UserrankachievementinfoRoutingModule} from "./userrankachievementinfo-routing.module";
import {
  UserrankachievementinfoListComponent
} from "../userrankachievementinfo-list/userrankachievementinfo-list.component";

@NgModule({
  declarations: [UserrankachievementinfoMenuComponent, UserrankachievementinfoListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    UserrankachievementinfoRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    SharedModule,
    NgbPaginationModule
  ],
  exports: [UserrankachievementinfoListComponent],
  bootstrap: [UserrankachievementinfoListComponent]
})
export class UserrankachievementinfoModuleModule { }
