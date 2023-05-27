import {Component, OnInit} from '@angular/core';
import {Car} from "../../../model/car";
import {Service} from "../../../service/service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {error} from "protractor";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {

  cars: Car[] | undefined;
  isEdit: boolean[] = [];
  error: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<Car>) {
    this.service.setMap("documents/cars");
  }

  ngOnInit(){
    this.service.findAll().pipe(
      catchError(error => {
        console.log("token: " + error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired'){
          this.service.refreshToken().subscribe(data=> {
            console.log("regresp: " + data);
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("documents/cars");
              this.service.findAll().subscribe(data=> {
                this.cars = data;
                this.isEdit?.fill(false, 0, this.cars?.length);
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        else {
          localStorage.clear();
          this.router.navigate(['login'])
        }
        return of([]);
      })
    ).subscribe(data=> {
      this.cars = data;
      this.isEdit?.fill(false, 0, this.cars?.length);
      console.log("get" );
      console.log(data);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(car: Car, index: number){
    this.service.update(car).pipe(
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
              this.service.setMap("documents/cars");
              this.service.update(car).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/car/car/cars']);
  }
}
