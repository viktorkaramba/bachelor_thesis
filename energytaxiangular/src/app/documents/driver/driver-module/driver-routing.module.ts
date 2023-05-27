import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DriverMenuComponent} from "../driver-menu/driver-menu.component";
import {DriverListComponent} from "../driver-list/driver-list.component";


const routes: Routes = [
  { path: '', redirectTo: 'driver', pathMatch:'full'},
  { path: 'driver', component:DriverMenuComponent,
    children:[
      { path: 'drivers', component: DriverListComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
