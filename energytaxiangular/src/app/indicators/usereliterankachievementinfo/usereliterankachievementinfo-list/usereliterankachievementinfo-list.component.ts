import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Observable} from "rxjs";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {FavouriteDriverService} from "../../favouritedrivers/favouritedrivers-list/favouritedrivers.service";
import {UserEliteRankAchievementInfo} from "../../../model/usereliterankachievementinfo";
import {UserEliteRankAchievementInfoService} from "./usereliterankachievementinfo.service";
import {DecimalPipe} from "@angular/common";

@Component({
  selector: 'app-usereliterankachievementinfo-list',
  templateUrl: './usereliterankachievementinfo-list.component.html',
  styleUrls: ['./usereliterankachievementinfo-list.component.css'],
  providers: [UserEliteRankAchievementInfoService, DecimalPipe],
})
export class UsereliterankachievementinfoListComponent implements OnInit {


  userEliteRank$: Observable<UserEliteRankAchievementInfo[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: UserEliteRankAchievementInfoService) {
    this.userEliteRank$ = service.userEliteRank$;
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
