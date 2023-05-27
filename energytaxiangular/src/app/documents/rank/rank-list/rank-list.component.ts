import { Component, OnInit } from '@angular/core';
import {Car} from "../../../model/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {Rank} from "../../../model/rank";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-rank-list',
  templateUrl: './rank-list.component.html',
  styleUrls: ['./rank-list.component.css']
})
export class RankListComponent implements OnInit {

  ranks: Rank[] | undefined;
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<Rank>) {
    this.service.setMap("bonuses/ranks");
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
              this.service.setMap("bonuses/ranks");
              this.service.findAll().subscribe(data => {
                this.ranks = data;
                this.isEdit?.fill(false, 0, this.ranks?.length);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.ranks = data;
      this.isEdit?.fill(false, 0, this.ranks?.length);
    });
  }

  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  update(rank: Rank, index: number){
    this.service.update(rank).pipe(
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
              this.service.setMap("bonuses/ranks");
              this.service.update(rank).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/rank/rank/ranks']);
  }

}
