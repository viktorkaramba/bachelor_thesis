import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DecimalPipe} from "@angular/common";
import {FavouriteAddressService} from "./favouriteaddresses.service";
import {Observable} from "rxjs";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {FavouriteAddress} from "../../../model/favouriteaddress";

@Component({
  selector: 'app-favouriteaddresses-list',
  templateUrl: './favouriteaddresses-list.component.html',
  styleUrls: ['./favouriteaddresses-list.component.css'],
  providers: [FavouriteAddressService, DecimalPipe],
})
export class FavouriteaddressesListComponent implements OnInit {

 favouriteAddress$: Observable<FavouriteAddress[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: FavouriteAddressService) {
    this.favouriteAddress$ = service.favouriteAddresses$;
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
