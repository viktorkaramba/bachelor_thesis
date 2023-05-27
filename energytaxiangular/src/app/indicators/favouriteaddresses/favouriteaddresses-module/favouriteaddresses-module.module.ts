import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FavouriteaddressesMenuComponent} from "../favouriteaddresses-menu/favouriteaddresses-menu.component";
import {FavouriteaddressesListComponent} from "../favouriteaddresses-list/favouriteaddresses-list.component";
import {HttpClientModule} from "@angular/common/http";
import {DriverRatingRoutingModule} from "../../driverrating/driver-rating-module/driver-rating-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";
import {FavouriteaddressesRoutingModule} from "./favouriteaddresses-routing.module";

@NgModule({
  declarations: [FavouriteaddressesMenuComponent, FavouriteaddressesListComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FavouriteaddressesRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    SharedModule,
    NgbPaginationModule
  ],
  exports: [FavouriteaddressesListComponent],
  bootstrap: [FavouriteaddressesListComponent],
})
export class FavouriteaddressesModuleModule { }
