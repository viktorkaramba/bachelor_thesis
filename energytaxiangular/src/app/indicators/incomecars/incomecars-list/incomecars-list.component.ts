import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {IncomeCars} from "../../../model/incomecars";
import {DecimalPipe} from "@angular/common";
import {IncomeCarsService} from "./income.service";
import {Observable} from "rxjs";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";

@Component({
  selector: 'app-incomecars-list',
  templateUrl: './incomecars-list.component.html',
  styleUrls: ['./incomecars-list.component.css'],
  providers: [IncomeCarsService, DecimalPipe],
})
export class IncomeCarsListComponent implements OnInit {

  incomeCars$: Observable<IncomeCars[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: IncomeCarsService) {
    this.incomeCars$ = service.incomeCars$;
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
