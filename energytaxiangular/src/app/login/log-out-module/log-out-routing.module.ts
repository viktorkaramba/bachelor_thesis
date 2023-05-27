import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LogOutComponent} from "../log-out/log-out.component";


const routes: Routes = [
  { path: '', redirectTo: 'logout', pathMatch:'full'},
  { path: 'logout', component:LogOutComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LogOutRoutingModule { }
