import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {
  UserrankachievementinfoListComponent
} from "../userrankachievementinfo-list/userrankachievementinfo-list.component";
import {
  UserrankachievementinfoMenuComponent
} from "../userrankachievementinfo-menu/userrankachievementinfo-menu.component";


const routes: Routes = [
  { path: '', redirectTo: 'user-rank-achievement-info', pathMatch:'full'},
  { path: 'user-rank-achievement-info', component: UserrankachievementinfoMenuComponent,
    children:[
      { path: 'users-rank-achievement-info', component: UserrankachievementinfoListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserrankachievementinfoRoutingModule { }
