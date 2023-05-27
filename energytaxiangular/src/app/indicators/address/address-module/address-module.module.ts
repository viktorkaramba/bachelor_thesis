import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddressRoutingModule} from "./address-routing.module";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AddressMenuComponent} from "../address-menu/address-menu.component";
import {AddressListComponent} from "../address-list/address-list.component";
import {NgbPaginationModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {SharedModule} from "../../../shared.module";

@NgModule({
  declarations: [AddressMenuComponent, AddressListComponent],
    imports: [
        CommonModule,
        AddressRoutingModule,
        HttpClientModule,
        NgbTypeaheadModule,
        ReactiveFormsModule,
        FormsModule,
        SharedModule,
        NgbPaginationModule
    ],
  exports: [AddressListComponent],
  bootstrap: [AddressListComponent],
})
export class AddressModuleModule { }
