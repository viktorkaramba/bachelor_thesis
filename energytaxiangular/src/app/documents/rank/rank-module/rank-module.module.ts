import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RankMenuComponent} from "../rank-menu/rank-menu.component";
import {RankListComponent} from "../rank-list/rank-list.component";
import {RankFormComponent} from "../rank-form/rank-form.component";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {RankRoutingModule} from "./rank-routing.module";
import {Service} from "../../../service/service.service";
import {AppComponent} from "../../../app.component";



@NgModule({
  declarations: [RankMenuComponent, RankListComponent, RankFormComponent],
  imports: [
    CommonModule,
    RankRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [Service],
  bootstrap: [AppComponent]
})
export class RankModuleModule { }
