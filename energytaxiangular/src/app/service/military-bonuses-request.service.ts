import { Injectable } from '@angular/core';
import {Driver} from "../model/driver";
import {Service} from "./service.service";
import {Router} from "@angular/router";
import {catchError, of} from "rxjs";
import {MilitaryBonuses} from "../model/militarybonuses";

@Injectable({
  providedIn: 'root'
})
export class MilitaryBonusesRequestService {
  private _militaryBonuses: MilitaryBonuses[] = [];
  private service: Service<MilitaryBonuses>;
  constructor(service: Service<MilitaryBonuses>, private router: Router) {
    this.service = service;
    this.service.setMap("bonuses/military-bonuses-waiting");
  }

  getMilitaryBonusesInfo(){
    this.service.setMap("bonuses/military-bonuses-waiting");
    return this.service.findAll().pipe(
      catchError(error => {
        console.log(error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired'){
          this.service.refreshToken().subscribe(data=> {
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("bonuses/military-bonuses-waiting");
              this.service.findAll().subscribe(data => {
                this._militaryBonuses = data;
              });
            }
          });
        }
        return of([]);
      })
    )
  }

  setAnswer(bonus: MilitaryBonuses, answer: string){
    this.service.setMap("bonuses/military-bonuses");
    bonus.militaryBonusStatus = answer;
    this.service.update(bonus).pipe(
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
              this.service.setMap("bonuses/military-bonuses");
              this.service.update(bonus).subscribe(() =>   location.reload());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() =>   location.reload());
  }

  get militaryBonuses(): MilitaryBonuses[] {
    return this._militaryBonuses;
  }
}
