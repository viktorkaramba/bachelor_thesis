import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {UsereliterankachievementinfoRoutingModule} from "./usereliterankachievementinfo-routing.module";
import {
  UsereliterankachievementinfoMenuComponent
} from "../usereliterankachievementinfo-menu/usereliterankachievementinfo-menu.component";
import {
  UsereliterankachievementinfoListComponent
} from "../usereliterankachievementinfo-list/usereliterankachievementinfo-list.component";

@NgModule({
  declarations: [UsereliterankachievementinfoMenuComponent, UsereliterankachievementinfoListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    UsereliterankachievementinfoRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    SharedModule,
    NgbPaginationModule
  ],
  exports: [UsereliterankachievementinfoListComponent],
  bootstrap: [UsereliterankachievementinfoListComponent]
})
export class UsereliterankachievementinfoModuleModule { }
