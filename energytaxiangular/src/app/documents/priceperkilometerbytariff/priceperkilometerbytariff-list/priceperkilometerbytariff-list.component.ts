import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {PricePerKilometerByTariff} from "../../../model/priceperkilometerbytarifff";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-priceperkilometerbytariff-list',
  templateUrl: './priceperkilometerbytariff-list.component.html',
  styleUrls: ['./priceperkilometerbytariff-list.component.css']
})
export class PricePerKilometerByTariffListComponent implements OnInit {

  pricePerKilometerByTariffs: PricePerKilometerByTariff[];
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<PricePerKilometerByTariff>) {
    this.service.setMap("documents/price-per-kilometer-by-tariff");
    this.pricePerKilometerByTariffs = [];
  }


  ngOnInit() {
    this.service.findAll().pipe(
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
              this.service.setMap("documents/price-per-kilometer-by-tariff");
              this.service.findAll().subscribe(data => {
                this.pricePerKilometerByTariffs = data;
                this.isEdit?.fill(false, 0, this.pricePerKilometerByTariffs?.length)
                console.log("get");
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.pricePerKilometerByTariffs = data;
      this.isEdit?.fill(false, 0, this.pricePerKilometerByTariffs?.length)
      console.log("get");
      console.log(data);
    });
  }
  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(pricePerKilometerByTariff: PricePerKilometerByTariff, index: number){
    this.service.update(pricePerKilometerByTariff).pipe(
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
              this.service.setMap("documents/price-per-kilometer-by-tariff");
              this.service.update(pricePerKilometerByTariff).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/price-per-kilometer-by-tariff/price-per-kilometer-by-tariff/price-per-kilometer-by-tariffs']);
  }
}
