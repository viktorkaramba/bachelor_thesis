import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {DriverRating} from "../../../model/driverrating";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-driver-ratings-report-by-id',
  templateUrl: './driver-ratings-report-by-id.component.html',
  styleUrls: ['./driver-ratings-report-by-id.component.css']
})
export class DriverRatingsReportByIdComponent implements OnInit {

  report: Report;
  driverRatings: DriverRating[]
  isShow: boolean = false;
  days:number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<DriverRating>) {
    this.driverRatings = [];
    this.report = new Report();
    this.service.setMap("indicators/drivers-ratings-report-by-id");
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
              this.service.setMap("indicators/drivers-ratings-report-by-id");
              this.service.getReport(this.report).subscribe(data =>{
                this.driverRatings = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.driverRatings = data;
      this.isShow = true;
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

  ngOnInit(): void {

  }

}
