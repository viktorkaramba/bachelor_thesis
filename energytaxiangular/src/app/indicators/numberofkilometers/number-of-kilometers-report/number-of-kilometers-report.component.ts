import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {NumberOfKilometers} from "../../../model/numberofkilometers";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-number-of-kilometers-report',
  templateUrl: './number-of-kilometers-report.component.html',
  styleUrls: ['./number-of-kilometers-report.component.css']
})
export class NumberOfKilometersReportComponent implements OnInit {

  report: Report;
  numberOfKilometers: NumberOfKilometers[]
  isShow: boolean = false;
  driverId: number [];
  days: number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<NumberOfKilometers>) {
    this.numberOfKilometers = [];
    this.driverId = [];
    this.report = new Report();
    this.service.setMap("indicators/number-of-kilometers-report");
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
              this.service.setMap("indicators/number-of-kilometers-report");
              this.service.getReport(this.report).subscribe(data =>{
                this.numberOfKilometers = data;
                this.isShow = true;
                this.getDriverId()
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.numberOfKilometers = data;
      this.isShow = true;
      this.getDriverId()
    });
  }

  getTotallyInfo(){
    let result = [2].fill(0);
    result[1]=0;
    for (let i of this.numberOfKilometers){
      result[0]+=Number((i.numbers).toFixed(2));
    }
    result[1] = Number((result[0] / (this.days+1)).toFixed(2))
    return result;
  }

  getMaxNumbersByDriver(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyNumbersByCarId = 0;
      for (let i of this.numberOfKilometers) {
        if (i.driverId == j) {
          totallyNumbersByCarId+=i.numbers;
        }
      }
      if(max <= totallyNumbersByCarId){
        max = Number((totallyNumbersByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinNumbersByDriver(){
    let min = this.getMaxNumbersByDriver()[0];
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyNumbersByCarId = 0;
      for (let i of this.numberOfKilometers) {
        if (i.driverId == j) {
          totallyNumbersByCarId += i.numbers;
        }
      }
      if(min >= totallyNumbersByCarId){
        min = Number((totallyNumbersByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getDriverId(){
    for (let i of this.numberOfKilometers){
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
