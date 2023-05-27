import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Observable} from "rxjs";
import {UserEliteRankAchievementInfo} from "../../../model/usereliterankachievementinfo";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {
  UserEliteRankAchievementInfoService
} from "../../usereliterankachievementinfo/usereliterankachievementinfo-list/usereliterankachievementinfo.service";
import {DecimalPipe} from "@angular/common";
import {UserRankAchievementInfo} from "../../../model/userrankachievementinfo";
import {UserRankAchievementInfoService} from "./userrankachievementinfo.service";

@Component({
  selector: 'app-userrankachievementinfo-list',
  templateUrl: './userrankachievementinfo-list.component.html',
  styleUrls: ['./userrankachievementinfo-list.component.css'],
  providers: [UserRankAchievementInfoService, DecimalPipe],
})
export class UserrankachievementinfoListComponent implements OnInit {

  userRank$: Observable<UserRankAchievementInfo[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: UserRankAchievementInfoService) {
    this.userRank$ = service.userRank$;
    this.total$ = service.total$;
  }

  onSort({ column, direction }: SortEvent) {
    // resetting other headers
    // @ts-ignore
    this.headers.forEach((header) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });

    this.service.sortColumn = column;
    this.service.sortDirection = direction;
  }

  ngOnInit(): void {
  }
}
