import { Component, OnInit } from '@angular/core';
import {Car} from "../../../model/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {Rank} from "../../../model/rank";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-rank-form',
  templateUrl: './rank-form.component.html',
  styleUrls: ['./rank-form.component.css']
})
export class RankFormComponent implements OnInit {

  rank: Rank;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<Rank>) {
    this.service.setMap("bonuses/ranks");
    this.rank = new Rank();
  }

  onSubmit() {
    this.service.save(this.rank).pipe(
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
              this.service.save(this.rank).subscribe(() => this.gotoRankList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoRankList());
  }

  gotoRankList() {
    this.router.navigate(['manager/rank/rank/ranks']);
  }

  ngOnInit(): void {
  }
}
