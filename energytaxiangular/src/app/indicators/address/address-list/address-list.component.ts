import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Address} from "../../../model/address";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {AddressService} from "./addresses.service";
import {DecimalPipe} from "@angular/common";
import {Observable} from "rxjs";


@Component({
  selector: 'app-address-list',
  templateUrl: './address-list.component.html',
  styleUrls: ['./address-list.component.css'],
  providers: [AddressService, DecimalPipe],
})
export class AddressListComponent implements OnInit {

  addresses$: Observable<Address[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: AddressService) {
    this.addresses$ = service.addresses$;
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
