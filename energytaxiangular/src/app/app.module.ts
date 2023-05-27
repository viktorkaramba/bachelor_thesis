import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {Service} from "./service/service.service";
import {AppRoutingModule} from "./app-routing.module";
import {AuthGuardService} from "./service/auth-guard.service";
import {AuthenticationService} from "./service/authentication.service";
import {AngularFireModule} from '@angular/fire/compat'
import {environment} from "../environments/environment";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    AngularFireModule.initializeApp(environment.firebaseConfig)
  ],
  providers: [Service, AuthGuardService, AuthenticationService],
  bootstrap: [AppComponent],
})
export class AppModule { }
