import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {CarClass} from "../../../model/carclass";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-carclass-list',
  templateUrl: './carclass-list.component.html',
  styleUrls: ['./carclass-list.component.css']
})
export class CarClassListComponent implements OnInit {

  carClasses: CarClass[] | undefined;
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<CarClass>) {
    this.service.setMap("documents/car-classes");
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
                  this.service.setMap("documents/car-classes");
                  this.service.findAll().subscribe(data => {
                    this.carClasses = data;
                    this.isEdit?.fill(false, 0, this.carClasses?.length)
                    console.log("get" );
                    console.log(data);
                  });
                }
              });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.carClasses = data;
      this.isEdit?.fill(false, 0, this.carClasses?.length)
      console.log("get" );
      console.log(data);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(carClass: CarClass, index: number){
    this.service.update(carClass).subscribe(() => this.gotoCarList());
    this.service.update(carClass).pipe(
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
              this.service.setMap("documents/car-classes");
              this.service.update(carClass).subscribe(() => this.gotoCarList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoCarList());
    this.setIsEdit(false, index);
  }

  gotoCarList() {
    this.router.navigate(['manager/car-class/car-class/car-classes']);
  }
}
