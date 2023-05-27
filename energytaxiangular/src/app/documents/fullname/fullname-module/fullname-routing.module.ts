import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FullNameMenuComponent} from "../fullname-menu/fullname-menu.component";
import {FullNameListComponent} from "../fullname-list/fullname-list.component";


const routes: Routes = [
  { path: '', redirectTo: 'fullname', pathMatch:'full'},
  { path: 'fullname', component:FullNameMenuComponent,
    children:[
      { path: 'fullnames', component: FullNameListComponent}
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FullNameRoutingModule { }
