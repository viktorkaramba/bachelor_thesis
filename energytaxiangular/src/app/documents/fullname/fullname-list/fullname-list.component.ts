import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {FullName} from "../../../model/fullname";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-fullname-list',
  templateUrl: './fullname-list.component.html',
  styleUrls: ['./fullname-list.component.css']
})
export class FullNameListComponent implements OnInit {

  fullNames: FullName[] | undefined;
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<FullName>) {
    this.service.setMap("documents/fullnames");
  }

  ngOnInit(){
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
              this.service.setMap("documents/fullnames");
              this.service.findAll().subscribe(data => {
                this.fullNames = data;
                this.isEdit?.fill(false, 0, this.fullNames?.length)
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.fullNames = data;
      this.isEdit?.fill(false, 0, this.fullNames?.length)
      console.log("get" );
      console.log(data);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(fullname: FullName, index: number){
    this.service.update(fullname).pipe(
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
              this.service.setMap("documents/fullnames");
              this.service.update(fullname).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/fullname/fullname/fullnames']);
  }
}
