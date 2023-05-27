import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {Order} from "../../../model/order";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-order-report',
  templateUrl: './order-report.component.html',
  styleUrls: ['./order-report.component.css']
})
export class OrderReportComponent implements OnInit {

  report: Report;
  orders: Order[];
  isShow: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<Order>) {
    this.orders = [];
    this.report = new Report();
    this.service.setMap("indicators/orders-report");
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
              this.service.setMap("indicators/orders-report");
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
