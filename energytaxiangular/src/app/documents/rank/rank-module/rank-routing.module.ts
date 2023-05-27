import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RankMenuComponent} from "../rank-menu/rank-menu.component";
import {RankListComponent} from "../rank-list/rank-list.component";
import {RankFormComponent} from "../rank-form/rank-form.component";


const routes: Routes = [
  { path: '', redirectTo: 'rank', pathMatch:'full'},
  { path: 'rank', component:RankMenuComponent,
    children:[
      { path: 'ranks', component: RankListComponent},
      { path: 'add-rank', component: RankFormComponent }
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RankRoutingModule { }
