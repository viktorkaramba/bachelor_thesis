import { Injectable } from '@angular/core';
import {Driver} from "../model/driver";
import {Service} from "./service.service";
import {catchError, of} from "rxjs";
import {Router} from "@angular/router";
import {MilitaryBonuses} from "../model/militarybonuses";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RegisterResponse} from "../model/register-response";
import {DriverInfo} from "../model/driverInfo";
import {AngularFireDatabase} from "@angular/fire/compat/database";

@Injectable({
  providedIn: 'root'
})
export class DriverResumeRequestService {

  private _driversResume: Driver[] = [];
  private service: Service<Driver>;
  constructor(service: Service<Driver>, private router: Router, private http: HttpClient, private db: AngularFireDatabase) {
    this.service = service;
    this.service.setMap("documents/drivers-resume-waiting");
  }

  getDriverResumeInfo(){
    this.service.setMap("documents/drivers-resume-waiting");
    // інакше, відправити запит до сервера та повернути список повідомлень
    return this.service.findAll().pipe(
      catchError(error => {
        console.log(error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired'){
          this.service.refreshToken().subscribe(data=> {
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("documents/drivers-resume-waiting");
              this.service.findAll().subscribe(data => {
                this._driversResume = data;
                location.reload();
              });
            }
          });
        }
        return of([]);
      })
    )
  }

  setAnswer(driver: Driver, answer: string, carId: number){
    driver.resumeStatus = answer;
    if(answer === 'COMPLETE'){
      this.completeResume(driver, carId);
    }
    else{
      this.rejectResume(driver);
    }
  }

  completeResume(driver: Driver, carId: number){
    this.service.setMap("documents/drivers");
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
              this.service.update(driver).subscribe(() =>{
                this.getCarInfo(driver.driverId);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => {
      this.getCarInfo(driver.driverId);
      this.updateCar(driver, carId);
    });
  }

  updateCar(driver: Driver, carId: number){
    if(driver.carId != carId){
      let httpHeaders = new HttpHeaders()
        .set('Content-type','application/Json')
        .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
      let options={
        headers:httpHeaders
      };
      this.service.setMap("documents/cars-resume-answer");
      let response = new CarUpdateRequest(driver.carId, carId);
      this.http.put<CarUpdateRequest>(this.service.url, response, options).pipe(
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
                this.service.setMap("documents/cars-resume-answer");
                this.http.put<CarUpdateRequest>(this.service.url, response, options)
                  .subscribe(() =>   location.reload());
              }
            });
          }
          return of([]);
        })
      ).subscribe(() =>   location.reload());
    }
  }
  getCarInfo(id: number){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    this.service.setMap("documents/driver-info/" + id);
    this.http.get<DriverInfo>(this.service.url, options).pipe(
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
              this.service.setMap("documents/driver-info/" + id);
              this.http.get<DriverInfo>(this.service.url, options)
                .subscribe(data=>{
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
    ).subscribe(data=>{
      let result = data as DriverInfo;
      console.log("driver-info: " + result);
      this.db.object("drivers-info/driver-" + id).set(result).then(r => {
        location.reload();
      });
    });
  }

  rejectResume(driver: Driver){
    this.service.setMap("documents/drivers-reject-resume");
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
              this.service.setMap("documents/drivers-reject-resume");
              this.service.update(driver).subscribe(() => location.reload());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => location.reload());
  }
}

class CarUpdateRequest {
  newCarId: number = -1;
  oldCarId: number = -1;

  constructor(newCarId: number, oldCarId: number) {
    this.newCarId = newCarId;
    this.oldCarId = oldCarId;
  }
}
