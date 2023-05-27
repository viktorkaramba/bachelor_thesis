import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {FavouritedriversMenuComponent} from "../favouritedrivers-menu/favouritedrivers-menu.component";
import {FavouritedriversListComponent} from "../favouritedrivers-list/favouritedrivers-list.component";
import {FavouritedriversRoutingModule} from "./favouritedrivers-routing.module";



@NgModule({
  declarations: [FavouritedriversMenuComponent, FavouritedriversListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FavouritedriversRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    SharedModule,
    NgbPaginationModule
  ],
  exports: [FavouritedriversListComponent],
  bootstrap: [FavouritedriversListComponent]
})
export class FavouritedriversModuleModule { }
