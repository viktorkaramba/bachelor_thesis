import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Observable} from "rxjs";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {FavouriteDriverService} from "../../favouritedrivers/favouritedrivers-list/favouritedrivers.service";
import {MilitaryBonuses} from "../../../model/militarybonuses";
import {DecimalPipe} from "@angular/common";
import {MilitaryBonusService} from "./militarybonuses.service";

@Component({
  selector: 'app-militarybonuses-list',
  templateUrl: './militarybonuses-list.component.html',
  styleUrls: ['./militarybonuses-list.component.css'],
  providers: [MilitaryBonusService, DecimalPipe],
})
export class MilitarybonusesListComponent implements OnInit {

  militaryBonus$: Observable<MilitaryBonuses[]>;
  imageUrl: string ='';
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: MilitaryBonusService) {
    this.militaryBonus$ = service.militaryBonuses$;
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
