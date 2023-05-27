import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {DriverRating} from "../../../model/driverrating";
import {Report} from "../../../model/report";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-driver-ratings-report',
  templateUrl: './driver-ratings-report.component.html',
  styleUrls: ['./driver-ratings-report.component.css']
})
export class DriverRatingsReportComponent implements OnInit {

  report: Report;
  driverRatings: DriverRating[]
  isShow: boolean = false;
  driverId: number [];
  days:number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<DriverRating>) {
    this.driverRatings = [];
    this.driverId = [];
    this.report = new Report();
    this.service.setMap("indicators/drivers-ratings-report");
  }

  getReport(){
    let date1 = new Date(this.report.startDate);
    let date2 = new Date(this.report.endDate);
    let time = date2.getTime() - date1.getTime();
    this.days = time/(1000 * 3600 * 24);
    this.service.getReport(this.report).pipe(
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
              this.service.setMap("indicators/drivers-ratings-report");
              this.service.getReport(this.report).subscribe(data =>{
                this.driverRatings = data;
                this.isShow = true;
                this.getDriverId();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.driverRatings = data;
      this.isShow = true;
      this.getDriverId();
    });
  }

  getAverageRating(){
    let result = 0;
    for (let i of this.driverRatings){
      result+=i.rating;
    }
    return Number((result / (this.days+1)).toFixed(2));
  }

  getMax(){
    let values = [2];
    for (let i of this.driverRatings) {
      if(values[0]<=i.rating){
        values[0]=i.rating;
        values[1]=i.driverId;
      }
    }
    return values;
  }

  getMin(){
    let values = [2];
    values[0] = this.getMax()[0];
    for (let i of this.driverRatings) {
      if(values[0]>=i.rating){
        values[0]=i.rating;
        values[1]=i.driverId;
      }
    }
    return values;
  }

  getMaxByDriver(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyAverageByDriverId = 0;
      let count = 0;
      for (let i of this.driverRatings) {
        if (i.driverId == j) {
          totallyAverageByDriverId+=i.rating;
          count++;
        }
      }
      let result = totallyAverageByDriverId/count;
      if(max <= result){
        max = result
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinByDriver(){
    let min = this.getMaxByDriver()[0];
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyAverageByDriverId = 0;
      let count = 0;
      for (let i of this.driverRatings) {
        if (i.driverId == j) {
          totallyAverageByDriverId += i.rating;
          count++;
        }
      }
      let result = totallyAverageByDriverId/count;
      if(min >= result){
        min = result
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getDriverId(){
    for (let i of this.driverRatings){
      if (!this.check(i.driverId)){
        this.driverId.push(i.driverId);
      }
    }
  }

  check(id: number | undefined): boolean{
    for (let i of this.driverId){
      if(i == id){
        return true
      }
    }
    return false;
  }
  ngOnInit(): void {

  }
}
