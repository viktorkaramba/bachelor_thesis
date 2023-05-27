import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {EliteRank} from "../../../model/eliterank";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-eliterank-form',
  templateUrl: './eliterank-form.component.html',
  styleUrls: ['./eliterank-form.component.css']
})
export class EliterankFormComponent implements OnInit {

  eliteRank: EliteRank;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<EliteRank>) {
    this.service.setMap("bonuses/elite-ranks");
    this.eliteRank = new EliteRank();
  }

  onSubmit() {
    this.service.save(this.eliteRank).pipe(
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
              this.service.save(this.eliteRank).subscribe(() => this.gotoEliteRankList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoEliteRankList());
  }

  gotoEliteRankList() {
    this.router.navigate(['manager/elite-rank/elite-rank/elite-ranks']);
  }

  ngOnInit(): void {
  }
}
