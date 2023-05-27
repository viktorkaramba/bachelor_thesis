import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AddressListComponent} from "../address-list/address-list.component";
import {AddressMenuComponent} from "../address-menu/address-menu.component";


const routes: Routes = [
  { path: '', redirectTo: 'address', pathMatch:'full'},
  { path: 'address', component: AddressMenuComponent,
    children:[
      { path: 'addresses', component: AddressListComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AddressRoutingModule { }
