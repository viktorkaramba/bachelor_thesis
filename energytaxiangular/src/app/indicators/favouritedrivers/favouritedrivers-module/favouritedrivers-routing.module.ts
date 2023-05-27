import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {FavouritedriversListComponent} from "../favouritedrivers-list/favouritedrivers-list.component";
import {FavouritedriversMenuComponent} from "../favouritedrivers-menu/favouritedrivers-menu.component";

const routes: Routes = [
  { path: '', redirectTo: 'favourite-driver', pathMatch:'full'},
  { path: 'favourite-driver', component: FavouritedriversMenuComponent,
    children:[
      { path: 'favourite-drivers', component: FavouritedriversListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FavouritedriversRoutingModule { }
