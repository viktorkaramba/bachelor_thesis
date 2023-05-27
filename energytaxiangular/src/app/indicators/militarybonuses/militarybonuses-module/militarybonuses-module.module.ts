import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MilitarybonusesMenuComponent} from "../militarybonuses-menu/militarybonuses-menu.component";
import {MilitarybonusesListComponent} from "../militarybonuses-list/militarybonuses-list.component";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {MilitarybonusesRoutingModule} from "./militarybonuses-module.routing";



@NgModule({
  declarations: [MilitarybonusesMenuComponent, MilitarybonusesListComponent],
    imports: [
        CommonModule,
        HttpClientModule,
        MilitarybonusesRoutingModule,
        ReactiveFormsModule,
        FormsModule,
        NgbTypeaheadModule,
        SharedModule,
        NgbPaginationModule,
        NgOptimizedImage
    ],
  exports: [MilitarybonusesListComponent],
  bootstrap: [MilitarybonusesListComponent],
})
export class MilitarybonusesModuleModule { }
