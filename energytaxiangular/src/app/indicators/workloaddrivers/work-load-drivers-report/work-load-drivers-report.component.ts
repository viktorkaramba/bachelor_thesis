import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {WorkLoadDrivers} from "../../../model/workloaddrivers";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-work-load-drivers-report',
  templateUrl: './work-load-drivers-report.component.html',
  styleUrls: ['./work-load-drivers-report.component.css']
})
export class WorkLoadDriversReportComponent implements OnInit {

  report: Report;
  workLoadDrivers: WorkLoadDrivers[]
  isShow: boolean = false;
  driverId: number [];
  days:number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<WorkLoadDrivers>) {
    this.workLoadDrivers = [];
    this.driverId = [];
    this.report = new Report();
    this.service.setMap("indicators/workloads-drivers-report");
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
              this.service.setMap("indicators/workloads-drivers-report");
              this.service.getReport(this.report).subscribe(data =>{
                this.workLoadDrivers = data;
                this.isShow = true;
                this.getDriverId();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.workLoadDrivers = data;
      this.isShow = true;
      this.getDriverId();
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
    result[9] =  Number((result[4] / (this.days+1)).toFixed(2))
    return result;
  }

  getMaxTimesByDriver(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyTimesByDriverId = 0;
      for (let i of this.workLoadDrivers) {
        if (i.driverId == j) {
          totallyTimesByDriverId+=i.morning + i.day + i.evening + i.night;
        }
      }
      if(max <= totallyTimesByDriverId){
        max = Number((totallyTimesByDriverId).toFixed(2))
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinTimesByDriver(){
    let min = this.getMaxTimesByDriver()[0];
    let id = 0;
    let values = [];
    for(let j of this.driverId) {
      let totallyTimesByDriverId = 0;
      for (let i of this.workLoadDrivers) {
        if (i.driverId == j) {
          totallyTimesByDriverId+=i.morning + i.day + i.evening + i.night;
        }
      }
      if(min >= totallyTimesByDriverId){
        min = Number((totallyTimesByDriverId).toFixed(2))
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getDriverId(){
    for (let i of this.workLoadDrivers){
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
