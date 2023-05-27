import {RouterModule, Routes} from "@angular/router";
import {DriverRatingMenuComponent} from "../../driverrating/driver-rating-menu/driver-rating-menu.component";
import {NgModule} from "@angular/core";
import {FavouriteaddressesListComponent} from "../favouriteaddresses-list/favouriteaddresses-list.component";
import {FavouriteaddressesMenuComponent} from "../favouriteaddresses-menu/favouriteaddresses-menu.component";

const routes: Routes = [
  { path: '', redirectTo: 'favourite-address', pathMatch:'full'},
  { path: 'favourite-address', component: FavouriteaddressesMenuComponent,
    children:[
      { path: 'favourite-addresses', component: FavouriteaddressesListComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FavouriteaddressesRoutingModule { }
