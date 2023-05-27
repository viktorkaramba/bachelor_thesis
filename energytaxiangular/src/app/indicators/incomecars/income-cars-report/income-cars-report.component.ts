import {Component, OnInit} from '@angular/core';
import {Report} from "../../../model/report";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {IncomeCars} from "../../../model/incomecars";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-income-cars-report',
  templateUrl: './income-cars-report.component.html',
  styleUrls: ['./income-cars-report.component.css']
})
export class IncomeCarsReportComponent implements OnInit {

  report: Report;
  incomeCars: IncomeCars[]
  isShow: boolean = false;
  carId: number [];
  days:number = 0;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<IncomeCars>) {
    this.incomeCars = [];
    this.carId = [];
    this.report = new Report();
    this.service.setMap("indicators/income-cars-report");
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
              this.service.setMap("indicators/income-cars-report");
              this.service.getReport(this.report).subscribe(data =>{
                this.incomeCars = data;
                this.isShow = true;
                this.getCarId();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data =>{
      this.incomeCars = data;
      this.isShow = true;
      this.getCarId();
    })
  }

  getTotallyInfo(){
    let result = [6].fill(0);
    result[1]=0;
    for (let i of this.incomeCars){
      result[0]+=Number((i.income).toFixed(2));
      result[1]+=Number((i.expenses).toFixed(2));
    }
    result[2] = Number((result[0]-result[1]).toFixed(2))
    result[3] = Number((result[0] / (this.days+1)).toFixed(2))
    result[4] =  Number((result[1] / (this.days+1)).toFixed(2))
    result[5] =  Number((result[2] / (this.days+1)).toFixed(2))
    return result;
  }

  getMaxProfitByCar(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyIncomeByCarId = 0;
      let totallyExpensesByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyIncomeByCarId+=i.income;
          totallyExpensesByCarId+=i.expenses;
        }
      }
      let result = Number((totallyIncomeByCarId-totallyExpensesByCarId).toFixed(2));
      if(max <= result){
        max = result
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinProfitByCar(){
    let min = this.getMaxProfitByCar()[0];
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyIncomeByCarId = 0;
      let totallyExpensesByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyIncomeByCarId += i.income;
        }
      }
      let result = Number((totallyIncomeByCarId-totallyExpensesByCarId).toFixed(2));
      if(min >= result){
        min = result
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getMaxIncomeByCar(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyIncomeByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyIncomeByCarId+=i.income;
        }
      }
      if(max <= totallyIncomeByCarId){
        max = Number((totallyIncomeByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinIncomeByCar(){
    let min = this.getMaxIncomeByCar()[0];
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyIncomeByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyIncomeByCarId += i.income;
        }
      }
      if(min >= totallyIncomeByCarId){
        min = Number((totallyIncomeByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getMaxExpenseByCar(){
    let max = 0;
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyExpensesByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyExpensesByCarId+=i.expenses;
        }
      }
      if(max <= totallyExpensesByCarId){
        max = Number((totallyExpensesByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(max, id);
    return values;
  }

  getMinExpensesByCar(){
    let min = this.getMaxExpenseByCar()[0];
    let id = 0;
    let values = [];
    for(let j of this.carId) {
      let totallyExpensesByCarId = 0;
      for (let i of this.incomeCars) {
        if (i.carId == j) {
          totallyExpensesByCarId += i.expenses;
        }
      }
      if(min >= totallyExpensesByCarId){
        min = Number((totallyExpensesByCarId).toFixed(2))
        id = j;
      }
    }
    values.push(min, id);
    return values;
  }

  getCarId(){
    for (let i of this.incomeCars){
      if (!this.check(i.carId)){
        this.carId.push(i.carId);
      }
    }
  }

  check(id: number | undefined): boolean{
    for (let i of this.carId){
      if(i == id){
        return true
      }
    }
    return false;
  }

  ngOnInit(): void {

  }
}
