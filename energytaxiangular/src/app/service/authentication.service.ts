import {Injectable} from '@angular/core';
import {LoginResponse} from "../model/login-info";
import {Service} from "./service.service";
import {Observable, Subject} from "rxjs";
import {UserRequest} from "../model/user-request";
import {User} from "../model/user";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  response: boolean | undefined;

  constructor(private service: Service<UserRequest>) {
    this.service.setMap("auth/admin-authenticate");
  }

  authenticate(userRequest: UserRequest, router: Router){
    const subject = new Subject<boolean>();
    let loginResponse: LoginResponse;
    this.service.setMap("auth/admin-authenticate");
    this.service.auth(userRequest).subscribe( result => {
      loginResponse = result;
      if(loginResponse.accessToken !== "Bad credentials"){
        localStorage.setItem('accessToken', loginResponse.accessToken);
        localStorage.setItem('refreshToken',  loginResponse.refreshToken);
        localStorage.setItem('username',  loginResponse.user.username);
        localStorage.setItem('role',  loginResponse.user.role);
        this.response = true;
        subject.next(this.response);
        router.navigate(['']);
      }
      else {
        console.log("bad credentials")
        alert('Invalid credentials');
        this.response = false;
        subject.next(this.response);
      }
    });
    console.log(subject.asObservable());
    return subject.asObservable();
  }

  isUserLoggedIn(){
    let token = localStorage.getItem('accessToken');
    console.log("in isLogged");
    console.log(!(token === null));
    return !(token === null);
  }

  logOut(){
    this.service.setMap("auth/logout");
    this.service.logout().subscribe(date =>{
      console.log(date);
    });
  }
}
