/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {NumberOfKilometers} from "../../../model/numberofkilometers";
import {Router} from "@angular/router";

interface SearchResult {
  numberOfKilometers: NumberOfKilometers[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermDriverId: string;
  searchTermNumbers: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(numberOfKilometers: NumberOfKilometers[], column: SortColumn, direction: string): NumberOfKilometers[] {
  if (direction === '' || column === '') {
    return numberOfKilometers;
  } else {
    return [...numberOfKilometers].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(numberOfKilometers: NumberOfKilometers, searchTermDate: string, searchTermDriverId: string,
                 searchTermNumbers: string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(numberOfKilometers.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(numberOfKilometers.driverId).includes(searchTermDriverId) &&
    pipe.transform(numberOfKilometers.numbers).includes(searchTermNumbers)
  );
}

@Injectable({ providedIn: 'root' })
export class NumberOfKilometersService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _numberOfKilometers$ = new BehaviorSubject<NumberOfKilometers[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  NUMBER_OF_KILOMETERS: NumberOfKilometers[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermDriverId:'',
    searchTermNumbers:'',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<NumberOfKilometers>, private router: Router) {
    this.service.setMap("indicators/number-of-kilometers");
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
              this.service.setMap("indicators/number-of-kilometers");
              this.service.findAll().subscribe(data => {
                this.NUMBER_OF_KILOMETERS = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.NUMBER_OF_KILOMETERS = data;
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
        this._numberOfKilometers$.next(result.numberOfKilometers);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get numberOfKilometers$() {
    return this._numberOfKilometers$.asObservable();
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
  get searchTermDate() {
    return this._state.searchTermDate;
  }
  get searchTermDriverID() {
    return this._state.searchTermDriverId;
  }
  get searchTermNumbers() {
    return this._state.searchTermNumbers
  }
  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermDate(searchTermDate: string) {
    this._set({ searchTermDate});
  }
  set searchTermDriverID(searchTermDriverId: string) {
    this._set({ searchTermDriverId});
  }
  set searchTermNumbers(searchTermNumbers: string) {
    this._set({searchTermNumbers});
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
    const { sortColumn, sortDirection, pageSize, page, searchTermDate,searchTermDriverId, searchTermNumbers } = this._state;

    // 1. sort

    let numberOfKilometers = sort(this.NUMBER_OF_KILOMETERS, sortColumn, sortDirection);

    // 2. filter
    numberOfKilometers = numberOfKilometers.filter((date) => matches(date, searchTermDate, searchTermDriverId,
      searchTermNumbers, this.pipe));
    const total = numberOfKilometers.length;

    // 3. paginate
    numberOfKilometers = numberOfKilometers.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ numberOfKilometers, total });
  }
}
