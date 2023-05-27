/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {Address} from "../../../model/address";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {Router} from "@angular/router";

interface SearchResult {
  addresses: Address[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermName: string;
  searchTermNumber: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(addresses: Address[], column: SortColumn, direction: string): Address[] {
  if (direction === '' || column === '') {
    return addresses;
  } else {
    return [...addresses].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(address: Address, searchTermName: string, searchTermNumber: string, pipe: PipeTransform) {
  return (
    address.name.toLowerCase().includes(searchTermName.toLowerCase()) &&
    pipe.transform(address.numberOfTimes).includes(searchTermNumber)
  );
}

@Injectable({ providedIn: 'root' })
export class AddressService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _addresses$ = new BehaviorSubject<Address[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  ADDRESSES: Address[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermName: '',
    searchTermNumber:'',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<Address>,  private router: Router) {
    this.service.setMap("indicators/addresses");
    this.service.findAll().pipe(
      catchError(error => {
        console.log(error.error.error_message === 'Token expired');
        if (error.error.error_message === 'Token expired'){
          this.service.refreshToken().subscribe(data=> {
            console.log("regresp: " + data);
            if (data.accessToken === 'Token expired') {
              localStorage.clear();
              this.router.navigate(['login'])
            } else {
              localStorage.setItem("accessToken", data.accessToken)
              this.service.setMap("indicators/addresses");
              this.service.findAll().subscribe(data => {
                this.ADDRESSES = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.ADDRESSES = data;
      console.log("get" );
      console.log(data);
    });
    this._search$
      .pipe(
        tap(() => this._loading$.next(true)),
        debounceTime(200),
        switchMap(() => this._search()),
        delay(200),
        tap(() => this._loading$.next(false)),
      )
      .subscribe((result) => {
        this._addresses$.next(result.addresses);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get addresses$() {
    return this._addresses$.asObservable();
  }
  get total$() {
    return this._total$.asObservable();
  }
  get loading$() {
    return this._loading$.asObservable();
  }
  get page() {
    return this._state.page;
  }
  get pageSize() {
    return this._state.pageSize;
  }
  get searchTermName() {
    return this._state.searchTermName;
  }
  get searchTermNumber() {
    return this._state.searchTermNumber;
  }
  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermName(searchTermName: string) {
    this._set({ searchTermName});
  }

  set searchTermNumber(searchTermNumber: string) {
    this._set({searchTermNumber});
  }

  set sortColumn(sortColumn: SortColumn) {
    this._set({ sortColumn });
  }
  set sortDirection(sortDirection: SortDirection) {
    this._set({ sortDirection });
  }

  private _set(patch: Partial<State>) {
    Object.assign(this._state, patch);
    this._search$.next();
  }

  private _search(): Observable<SearchResult> {
    const { sortColumn, sortDirection, pageSize, page, searchTermName, searchTermNumber } = this._state;
    let addresses = sort(this.ADDRESSES, sortColumn, sortDirection);

    addresses = addresses.filter((address) => matches(address, searchTermName, searchTermNumber, this.pipe));
    const total = addresses.length;

    addresses = addresses.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ addresses, total });
  }
}
