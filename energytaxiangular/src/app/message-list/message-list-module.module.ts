import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import { MessageListModuleRoutingModule } from './message-list-module-routing.module';
import {MessageListComponent} from "./message-list.component";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Service} from "../service/service.service";
import {AppComponent} from "../app.component";

@NgModule({
  declarations: [MessageListComponent],
  imports: [
    CommonModule,
    MessageListModuleRoutingModule,
    HttpClientModule,
    FormsModule,
    NgOptimizedImage
  ],
  providers: [Service],
  bootstrap: [AppComponent]
})
export class MessageListModuleModule { }
