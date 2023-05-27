import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(
    private router:Router,
    private authService: AuthenticationService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state:RouterStateSnapshot){
    if(this.authService.isUserLoggedIn()){
      console.log("in guard true")
      return true;
    }
    else {
      console.log("in guard false")
      this.router.navigate(['login']);
      return false;
    }
  }
}
