import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {IncomeCars} from "../../../model/incomecars";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-income-cars-report-by-id',
  templateUrl: './income-cars-report-by-id.component.html',
  styleUrls: ['./income-cars-report-by-id.component.css']
})
export class IncomeCarsReportByIdComponent implements OnInit {

  report: Report;
  incomeCars: IncomeCars[]
  isShow: boolean = false;
  days:number=0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<IncomeCars>) {
    this.incomeCars = [];
    this.report = new Report();
    this.service.setMap("indicators/income-cars-report-by-id");
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
              this.service.setMap("indicators/income-cars-report-by-id");
              this.service.getReport(this.report).subscribe(data =>{
                this.incomeCars = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.incomeCars = data;
      this.isShow = true;
    });
  }

  getTotallyInfo() {
    let result = [6].fill(0);
    result[1] = 0;
    for (let i of this.incomeCars) {
      result[0] += Number((i.income).toFixed(2));
      result[1] += Number((i.expenses).toFixed(2));
    }
    result[2] = Number((result[0] - result[1]).toFixed(2))
    result[3] = Number((result[0] / (this.days+1)).toFixed(2))
    result[4] = Number((result[1] / (this.days+1)).toFixed(2))
    result[5] = Number((result[2] / (this.days+1)).toFixed(2))
    return result;
  }

  getMaxProfit(){
    let max = 0;
    for (let i of this.incomeCars){
      let profit = Number((i.income).toFixed(2)) - Number((i.expenses).toFixed(2))
      if(max <=profit){
        max = profit;
      }
    }
    return max;
  }

  getMinProfit(){
    let min = this.getMaxProfit();
    for (let i of this.incomeCars){
      let profit = Number((i.income).toFixed(2)) - Number((i.expenses).toFixed(2))
      if(min >= profit){
        min = profit;
      }
    }
    return min;
  }

  ngOnInit(): void {

  }

}
