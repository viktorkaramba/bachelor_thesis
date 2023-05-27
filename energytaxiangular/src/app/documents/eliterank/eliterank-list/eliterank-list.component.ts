import { Component, OnInit } from '@angular/core';
import {Car} from "../../../model/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {EliteRank} from "../../../model/eliterank";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-eliterank-list',
  templateUrl: './eliterank-list.component.html',
  styleUrls: ['./eliterank-list.component.css']
})
export class EliterankListComponent implements OnInit {

  eliteRanks: EliteRank[] | undefined;
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<EliteRank>) {
    this.service.setMap("bonuses/elite-ranks");
  }

  ngOnInit(){
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
              this.service.setMap("bonuses/elite-ranks");
              this.service.findAll().subscribe(data => {
                this.eliteRanks = data;
                this.isEdit?.fill(false, 0, this.eliteRanks?.length);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.eliteRanks = data;
      this.isEdit?.fill(false, 0, this.eliteRanks?.length);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  delete(id:number){
    console.log(id);
    this.service.deleteById(id).pipe(
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
              this.service.setMap("bonuses/elite-ranks");
              this.service.deleteById(id).subscribe(data => {
                location.reload();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      location.reload();
    });
  }

  update(eliteRank: EliteRank, index: number){
    this.service.update(eliteRank).pipe(
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
              this.service.setMap("bonuses/elite-ranks");
              this.service.update(eliteRank).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/elite-rank/elite-rank/elite-ranks']);
  }
}
