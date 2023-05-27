import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {WorkLoadDrivers} from "../../../model/workloaddrivers";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {DecimalPipe} from "@angular/common";
import {WorkLoadDriversService} from "./workloaddrivers.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-workloaddrivers-list',
  templateUrl: './workloaddrivers-list.component.html',
  styleUrls: ['./workloaddrivers-list.component.css'],
  providers: [WorkLoadDriversService, DecimalPipe],
})
export class WorkLoadDriversListComponent implements OnInit {

  workLoadDrivers$: Observable<WorkLoadDrivers[]>;
  total$: Observable<number>;
  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: WorkLoadDriversService) {
    this.workLoadDrivers$ = service.workLoadDrivers$;
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
