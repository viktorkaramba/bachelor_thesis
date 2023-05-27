import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {NumberOfKilometers} from "../../../model/numberofkilometers";
import {Observable} from "rxjs";
import {NumberOfKilometersService} from "./numberofkilometers.service";
import {DecimalPipe} from "@angular/common";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";

@Component({
  selector: 'app-numberofkilometers-list',
  templateUrl: './numberofkilometers-list.component.html',
  styleUrls: ['./numberofkilometers-list.component.css'],
  providers: [NumberOfKilometersService, DecimalPipe],
})
export class NumberOfKilometersListComponent implements OnInit {

  numberOfKilometers$: Observable<NumberOfKilometers[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;
  constructor(public service: NumberOfKilometersService) {
    this.numberOfKilometers$ = service.numberOfKilometers$;
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
