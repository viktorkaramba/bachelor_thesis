import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {User} from "../../../model/user";
import {catchError, of, Subject} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[] | undefined;
  drivers: User[] | undefined;
  admins: User[] | undefined;
  superAdmins: User[] | undefined;
  isEdit: boolean[] = [];
  role: any;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<User>) {
    this.service.setMap("documents/users");
    this.role = localStorage.getItem('role');
    console.log("role");
    console.log(this.role);
  }

  ngOnInit() {
    let usersS = new Subject<User[]>();
    let driversS = new Subject<User[]>();
    let adminsS = new Subject<User[]>();
    let superAdminsS = new Subject<User[]>();
    this.service.findAll().pipe(
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
              this.service.findAll().subscribe(data=>{
                  this.isEdit?.fill(false, 0, data.length);
                  let u: User[] = [];
                  let d: User[] = [];
                  let a: User[] = [];
                  let s: User[] = [];
                  for(let user of data){
                    if(user.role=="DRIVER"){
                      d.push(user);
                    }
                    else if(user.role==="USER"){
                      u.push(user);
                    }
                    else if(user.role==="ADMIN"){
                      a.push(user);
                    }
                    else{
                      s.push(user);
                    }
                  }
                  usersS.next(u);
                  driversS.next(d);
                  adminsS.next(a);
                  superAdminsS.next(s);
                  console.log("get");
                  console.log( usersS.asObservable());
                });
                usersS.asObservable().subscribe( data =>{
                  this.users = data;
                });
                driversS.asObservable().subscribe( data =>{
                  this.drivers = data;
                });
                adminsS.asObservable().subscribe( data =>{
                  this.admins = data;
                });
                superAdminsS.asObservable().subscribe(data =>{
                  this.superAdmins = data;
                });
              }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.isEdit?.fill(false, 0, data.length);
      let u: User[] = [];
      let d: User[] = [];
      let a: User[] = [];
      let s: User[] = [];
      for(let user of data){
        if(user.role=="DRIVER"){
          d.push(user);
        }
        else if(user.role==="USER"){
          u.push(user);
        }
        else if(user.role==="ADMIN"){
          a.push(user);
        }
        else{
          s.push(user);
        }
      }
      usersS.next(u);
      driversS.next(d);
      adminsS.next(a);
      superAdminsS.next(s);
      console.log("get");
      console.log( usersS.asObservable());
    });
    usersS.asObservable().subscribe( data =>{
      this.users = data;
    });
    driversS.asObservable().subscribe( data =>{
      this.drivers = data;
    });
    adminsS.asObservable().subscribe( data =>{
      this.admins = data;
    });
    superAdminsS.asObservable().subscribe(data =>{
      this.superAdmins = data;
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }
  delete(id:number){
    console.log(id);
    this.service.deleteById(id).pipe(
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
              this.service.deleteById(id).subscribe(data=>{
                location.reload();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data=>{
      location.reload();
    });
  }
  update(user: User, index: number){
    this.service.update(user).pipe(
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
              this.service.update(user).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/user/user/users']);
  }
}
