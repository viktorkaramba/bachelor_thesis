import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {NumberOfKilometers} from "../../../model/numberofkilometers";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-number-of-kilometers-report-by-id',
  templateUrl: './number-of-kilometers-report-by-id.component.html',
  styleUrls: ['./number-of-kilometers-report-by-id.component.css']
})
export class NumberOfKilometersReportByIdComponent implements OnInit {

  report: Report;
  numberOfKilometers: NumberOfKilometers[]
  isShow: boolean = false;
  days:number=0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<NumberOfKilometers>) {
    this.numberOfKilometers = [];
    this.report = new Report();
    this.service.setMap("indicators/number-of-kilometers-report-by-id");
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
              this.service.setMap("indicators/number-of-kilometers-report-by-id");
              this.service.getReport(this.report).subscribe(data =>{
                this.numberOfKilometers = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.numberOfKilometers = data;
      this.isShow = true;
    });
  }

  getTotallyInfo() {
    let result = [2].fill(0);
    result[1] = 0;
    for (let i of this.numberOfKilometers) {
      result[0] += Number((i.numbers).toFixed(2));
    }
    result[1] = Number((result[0] / (this.days+1)).toFixed(2))
    return result;
  }

  ngOnInit(): void {

  }

}
