import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {User} from "../../../model/user";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  user: User;
  roles = ["SUPER_ADMIN", "ADMIN", "USER", "DRIVER"];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<User>) {
    this.service.setMap("documents/users");
    this.user = new User();
  }

  onSubmit() {
    this.service.save(this.user).pipe(
      catchError(error => {
        console.log(error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired'){
          this.service.refreshToken().subscribe(data=> {
            console.log("regresp: " + data);
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("documents/users");
              this.service.save(this.user).subscribe(() => this.gotoUserList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoUserList());
  }

  gotoUserList() {
    this.router.navigate(['manager/user/user/users']);
  }

  ngOnInit(): void {
  }

}
