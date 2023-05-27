import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from "../user-list/user-list.component";
import {UserMenuComponent} from "../user-menu/user-menu.component";
import {UserFormComponent} from "../user-form/user-form.component";


const routes: Routes = [
  { path: '', redirectTo: 'user', pathMatch:'full'},
  { path: 'user', component:UserMenuComponent,
    children:[
      { path: 'users', component: UserListComponent},
      { path: 'add-user', component: UserFormComponent }
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
