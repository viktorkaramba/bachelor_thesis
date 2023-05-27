import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {NumberOfKilometers} from "../../../model/numberofkilometers";
import {Report} from "../../../model/report";
import {Order} from "../../../model/order";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-order-report-by-id',
  templateUrl: './order-report-by-id.component.html',
  styleUrls: ['./order-report-by-id.component.css']
})
export class OrderReportByIdComponent implements OnInit {

  report: Report;
  orders: Order[]
  isShow: boolean = false;
  days:number=0;
  constructor( private route: ActivatedRoute,
               private router: Router,
               private service: Service<Order>) {
    this.orders = [];
    this.report = new Report();
    this.service.setMap("indicators/order-report-by-id");
  }

  getReport(): void {
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
              this.service.setMap("indicators/order-report-by-id");
              this.service.getReport(this.report).subscribe(data =>{
                this.orders = data;
                this.isShow = true;
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.orders = data;
      this.isShow = true;
    });
  }

  ngOnInit(): void {

  }

}
