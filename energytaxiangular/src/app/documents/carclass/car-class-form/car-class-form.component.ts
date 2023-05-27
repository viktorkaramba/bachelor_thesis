import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {CarClass} from "../../../model/carclass";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-car-class-form',
  templateUrl: './car-class-form.component.html',
  styleUrls: ['./car-class-form.component.css']
})
export class CarClassFormComponent implements OnInit {

  carClass: CarClass;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<CarClass>) {
    this.service.setMap("documents/car-classes");
    this.carClass = new CarClass();
  }

  onSubmit() {
    this.service.save(this.carClass).pipe(
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
              this.service.save(this.carClass).subscribe(result => this.gotoCarClassList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(result => this.gotoCarClassList());
  }

  gotoCarClassList() {
    this.router.navigate(['manager/car-class/car-class/car-classes']);
  }

  ngOnInit(): void {
  }
}
