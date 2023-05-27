import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {Driver} from "../../../model/driver";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, of} from "rxjs";
import {AngularFireDatabase} from "@angular/fire/compat/database";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ResponseMessage} from "../../../model/message";
import {DriverInfo} from "../../../model/driverInfo";

@Component({
  selector: 'app-driver-list',
  templateUrl: './driver-list.component.html',
  styleUrls: ['./driver-list.component.css']
})
export class DriverListComponent implements OnInit {

  drivers: Driver[] | undefined;
  isEdit: boolean[] = [];
  carsId: number[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private db: AngularFireDatabase,
    private http: HttpClient,
    private service: Service<Driver>) {
    this.service.setMap("documents/drivers");
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
              this.service.setMap("documents/drivers");
              this.service.findAll().subscribe(data => {
                this.drivers = data;
                this.drivers.forEach(value => {
                  this.carsId.push(value.carId);
                })
                this.isEdit?.fill(false, 0, this.drivers?.length)
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.drivers = data;
      this.drivers.forEach(value => {
        this.carsId.push(value.carId);
      })
      this.isEdit?.fill(false, 0, this.drivers?.length)
      console.log("get" );
      console.log(data);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(driver: Driver, index: number){
    this.service.update(driver).pipe(
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
              this.service.setMap("documents/drivers");
              this.service.update(driver).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    if(driver.carId === 0){
      this.updateCar(this.carsId[index], "NO", driver.driverId);
    }
    else {
      if(this.carsId[index] === 0){
        this.updateCar(driver.carId, "YES", driver.driverId);
      }
    }
    this.setIsEdit(false, index);
  }

  updateCar(carId: number, answer: string, driverId: number){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    this.service.setMap("documents/car-status");
    console.log(carId);
    let id = carId.toString();
    let message = new ResponseMessage(id, answer);
    console.log(message);
    this.http.post<number>(this.service.url, message, options).pipe(
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
              this.service.setMap("documents/car-status");
              this.http.post<number>(this.service.url, message, options)
                .subscribe(() =>   {
                  if(answer === "NO"){
                    this.db.object("drivers-info/driver-" + driverId).remove().then(()=>location.reload());
                  }
                  else {
                    this.getCarInfo(driverId);
                  }
                });
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => {
      if(answer === "NO"){
        this.db.object("drivers-info/driver-" + driverId).remove().then(()=>location.reload());
      }
      else {
        this.getCarInfo(driverId);
      }
    });
  }

  getCarInfo(id: number) {
    let httpHeaders = new HttpHeaders()
      .set('Content-type', 'application/Json')
      .set('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
    let options = {
      headers: httpHeaders
    };
    this.service.setMap("documents/driver-info/" + id);
    this.http.get<DriverInfo>(this.service.url, options).pipe(
      catchError(error => {
        console.log(error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired') {
          this.service.refreshToken().subscribe(data => {
            console.log("regresp: " + data);
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("documents/driver-info/" + id);
              this.http.get<DriverInfo>(this.service.url, options)
                .subscribe(data => {
                  let result = data as DriverInfo;
                  console.log("driver-info: " + result);
                  this.db.object("drivers-info/driver-" + id).set(result).then(r => {
                    location.reload();
                  });
                });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      let result = data as DriverInfo;
      console.log("driver-info: " + result);
      this.db.object("drivers-info/driver-" + id).set(result).then(r => {
        location.reload();
      });
    });
  }

  gotoList() {
    this.router.navigate(['manager/driver/driver/drivers']);
  }
}
