import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {WorkLoadDrivers} from "../../../model/workloaddrivers";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-work-load-drivers-report-by-id',
  templateUrl: './work-load-drivers-report-by-id.component.html',
  styleUrls: ['./work-load-drivers-report-by-id.component.css']
})
export class WorkLoadDriversReportByIdComponent implements OnInit {

  report: Report;
  workLoadDrivers: WorkLoadDrivers[]
  isShow: boolean = false;
  days:number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<WorkLoadDrivers>) {
    this.workLoadDrivers = [];
    this.report = new Report();
    this.service.setMap("indicators/workloads-drivers-report-by-id");
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
              this.service.setMap("indicators/workloads-drivers-report-by-id");
              this.service.getReport(this.report).subscribe(data =>{
                this.workLoadDrivers = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.workLoadDrivers = data;
      this.isShow = true;
    });
  }

  getTotallyInfo(){
    let result = [10].fill(0);
    result[0]=0;
    result[1]=0;
    result[2]=0;
    result[3]=0;
    for (let i of this.workLoadDrivers){
      result[0]+=Number((i.morning).toFixed(2));
      result[1]+=Number((i.day).toFixed(2));
      result[2]+=Number((i.evening).toFixed(2));
      result[3]+=Number((i.night).toFixed(2));
    }
    result[4] = Number((result[0]+result[1]+result[2]+result[3]).toFixed(2))
    result[5] = Number((result[0] / (this.days+1)).toFixed(2))
    result[6] =  Number((result[1] / (this.days+1)).toFixed(2))
    result[7] =  Number((result[2] / (this.days+1)).toFixed(2))
    result[8] =  Number((result[3] / (this.days+1)).toFixed(2))
    result[9] =  Number((result[4] /(this.days+1)).toFixed(2))
    return result;
  }


  ngOnInit(): void {

  }

}
