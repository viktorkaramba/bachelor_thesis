import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Observable} from "rxjs";
import {FavouriteAddress} from "../../../model/favouriteaddress";
import {NgbdSortableHeader, SortEvent} from "../../../service/sortable.directive";
import {FavouriteAddressService} from "../../favouriteaddresses/favouriteaddresses-list/favouriteaddresses.service";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {FavouriteDriverService} from "./favouritedrivers.service";
import {DecimalPipe} from "@angular/common";

@Component({
  selector: 'app-favouritedrivers-list',
  templateUrl: './favouritedrivers-list.component.html',
  styleUrls: ['./favouritedrivers-list.component.css'],
  providers: [FavouriteDriverService, DecimalPipe],
})
export class FavouritedriversListComponent implements OnInit {


  favouriteDriver$: Observable<FavouriteDriver[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader> | undefined;

  constructor(public service: FavouriteDriverService) {
    this.favouriteDriver$ = service.favouriteDrivers$;
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
