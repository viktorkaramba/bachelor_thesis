import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EliterankMenuComponent} from "../eliterank-menu/eliterank-menu.component";
import {EliterankListComponent} from "../eliterank-list/eliterank-list.component";
import {EliterankFormComponent} from "../eliterank-form/eliterank-form.component";


const routes: Routes = [
  { path: '', redirectTo: 'elite-rank', pathMatch:'full'},
  { path: 'elite-rank', component:EliterankMenuComponent,
    children:[
      { path: 'elite-ranks', component: EliterankListComponent},
      { path: 'add-elite-rank', component: EliterankFormComponent }
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EliteRankRoutingModule { }
