import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DriverCarRecommendations} from "../../../model/drivercarrecommendations";
import {NgbdSortableHeader, SortEvent,} from "../../../service/sortable.directive";
import {Observable} from "rxjs";
import {DriveCarRecService} from "./drivercarrecommendations.service";
import {DecimalPipe} from "@angular/common";

@Component({
  selector: 'app-drivercarrecommendations-list',
  templateUrl: './drivercarrecommendations-list.component.html',
  styleUrls: ['./drivercarrecommendations-list.component.css'],
  providers: [DriveCarRecService, DecimalPipe],
})
export class DriverCarRecommendationsListComponent implements OnInit {

  driverCarRecs$: Observable<DriverCarRecommendations[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: DriveCarRecService) {
    this.driverCarRecs$ = service.driverCarRec;
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
