import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Order} from "../../../model/order";
import {DecimalPipe} from "@angular/common";
import {Observable} from "rxjs";
import {OrdersService} from "./order.service";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";


@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css'],
  providers: [DecimalPipe, OrdersService],
})
export class OrderListComponent implements OnInit {

  orders$: Observable<Order[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: OrdersService) {
    this.orders$ = service.orders$;
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
