import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {
  UsereliterankachievementinfoMenuComponent
} from "../usereliterankachievementinfo-menu/usereliterankachievementinfo-menu.component";
import {
  UsereliterankachievementinfoListComponent
} from "../usereliterankachievementinfo-list/usereliterankachievementinfo-list.component";

const routes: Routes = [
  { path: '', redirectTo: 'user-elite-rank-achievement-info', pathMatch:'full'},
  { path: 'user-elite-rank-achievement-info', component: UsereliterankachievementinfoMenuComponent,
    children:[
      { path: 'users-elite-rank-achievement-info', component: UsereliterankachievementinfoListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsereliterankachievementinfoRoutingModule { }
