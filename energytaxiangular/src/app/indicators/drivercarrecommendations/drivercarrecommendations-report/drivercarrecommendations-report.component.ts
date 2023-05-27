import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {DriverCarRecommendations} from "../../../model/drivercarrecommendations";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-drivercarrecommendations-report',
  templateUrl: './drivercarrecommendations-report.component.html',
  styleUrls: ['./drivercarrecommendations-report.component.css']
})
export class DrivercarrecommendationsReportComponent implements OnInit {

  report: Report;
  driverCarRecs: DriverCarRecommendations[];
  isShow: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<DriverCarRecommendations>) {
    this.driverCarRecs = [];
    this.report = new Report();
    this.service.setMap("indicators/drivers-cars-recommendations-report");
  }

  getReport(){
    console.log(this.report)
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
              this.service.setMap("indicators/drivers-cars-recommendations-report");
              this.service.getReport(this.report).subscribe(data =>{
                this.driverCarRecs = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.driverCarRecs = data;
      this.isShow = true;
    });
  }

  ngOnInit(): void {
  }
}
