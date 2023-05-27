import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {MilitarybonusesMenuComponent} from "../militarybonuses-menu/militarybonuses-menu.component";
import {MilitarybonusesListComponent} from "../militarybonuses-list/militarybonuses-list.component";

const routes: Routes = [
  { path: '', redirectTo: 'military-bonus', pathMatch:'full'},
  { path: 'military-bonus', component: MilitarybonusesMenuComponent,
    children:[
      { path: 'military-bonuses', component: MilitarybonusesListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MilitarybonusesRoutingModule { }
