import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DriverRating} from "../../../model/driverrating";
import {DriverRatingService} from "./driverrating.service";
import {Observable} from "rxjs";
import {DecimalPipe} from "@angular/common";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";

@Component({
  selector: 'app-driverrating-list',
  templateUrl: './driverrating-list.component.html',
  styleUrls: ['./driverrating-list.component.css'],
  providers: [DriverRatingService, DecimalPipe],
})
export class DriverRatingListComponent implements OnInit {

  driverRating$: Observable<DriverRating[]>;
  total$: Observable<number>;
  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: DriverRatingService) {
    this.driverRating$ = service.driverRating$;
    this.total$ = service.total$;
  }

  onSort({column, direction}: SortEvent) {

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

  ngOnInit(){
  }

}
