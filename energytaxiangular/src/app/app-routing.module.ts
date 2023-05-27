import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "./service/auth-guard.service";


const routes: Routes = [

  { path: '', redirectTo: 'manager', pathMatch:'full'},
  { path: 'manager', loadChildren:() => import('./manager/manager.module').then(m => m.ManagerModule),
    canActivate:[AuthGuardService]},
  { path: 'login', loadChildren:() => import('./login/login-module/login-module.module').then(m => m.LoginModuleModule)},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
