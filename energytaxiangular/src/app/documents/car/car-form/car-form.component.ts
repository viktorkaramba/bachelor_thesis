import {Component, OnInit} from '@angular/core';
import {Car} from "../../../model/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-car-form',
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent implements OnInit {

  car: Car;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<Car>) {
    this.service.setMap("documents/cars");
    this.car = new Car();
  }

  onSubmit() {
    this.service.save(this.car).pipe(
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
              this.service.save(this.car).subscribe(() => this.gotoCarList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoCarList());
  }

  gotoCarList() {
    this.router.navigate(['manager/car/car/cars']);
  }

  ngOnInit(): void {
  }

}
