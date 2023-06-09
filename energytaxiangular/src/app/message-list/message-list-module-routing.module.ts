import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MessageListComponent} from "./message-list.component";

const routes: Routes = [
  { path: '', redirectTo: 'message', pathMatch:'full'},
  { path: 'message', component:MessageListComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MessageListModuleRoutingModule { }
