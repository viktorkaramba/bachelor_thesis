import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CarListComponent} from "../car-list/car-list.component";
import {CarFormComponent} from "../car-form/car-form.component";
import {CarMenuComponent} from "../car-menu/car-menu.component";


const routes: Routes = [
  { path: '', redirectTo: 'car', pathMatch:'full'},
  { path: 'car', component:CarMenuComponent,
  children:[
    { path: 'cars', component: CarListComponent},
    { path: 'add-car', component: CarFormComponent }
  ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarRoutingModule { }
