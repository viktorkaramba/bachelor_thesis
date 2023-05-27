import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../service/authentication.service";
import {UserRequest} from "../../model/user-request";
import {Service} from "../../service/service.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  userRequest: UserRequest;
  invalidLogin = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private loginService: AuthenticationService) {
    console.log("in loginform")
    this.userRequest = new UserRequest();
  }

  checkLogin(){
    if(this.loginService.authenticate(this.userRequest, this.router)){
      console.log("in loginform authenticate")
      this.router.navigate(['']);
      this.invalidLogin = false;
    }
    else {

      this.invalidLogin = true;
    }
  }

  ngOnInit(): void {
  }

}
