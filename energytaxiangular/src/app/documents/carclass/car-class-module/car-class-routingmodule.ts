import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CarClassListComponent} from "../carclass-list/carclass-list.component";
import {CarClassMenuComponent} from "../car-class-menu/car-class-menu.component";
import {CarClassFormComponent} from "../car-class-form/car-class-form.component";


const routes: Routes = [
  { path: '', redirectTo: 'car-class', pathMatch:'full'},
  { path: 'car-class', component:CarClassMenuComponent,
    children:[
      { path: 'car-classes', component: CarClassListComponent},
      { path: 'add-car-class', component:  CarClassFormComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarClassRoutingModule { }
