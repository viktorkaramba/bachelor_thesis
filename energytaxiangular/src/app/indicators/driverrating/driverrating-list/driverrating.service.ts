/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {DriverRating} from "../../../model/driverrating";
import {Router} from "@angular/router";

interface SearchResult {
  driverRating: DriverRating[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermDriverId: string;
  searchTermRating: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(driverRating: DriverRating[], column: SortColumn, direction: string): DriverRating[] {
  if (direction === '' || column === '') {
    return driverRating;
  } else {
    return [...driverRating].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(driverRating: DriverRating, searchTermDate: string, searchTermDriverId: string,
                 searchTermRating: string,pipe: PipeTransform) {
  console.log(driverRating.date);
  console.log(searchTermDate);
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(driverRating.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(driverRating.driverId).includes(searchTermDriverId) &&
    pipe.transform(driverRating.rating).includes(searchTermRating)
  );
}

@Injectable({ providedIn: 'root' })
export class DriverRatingService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _driverRating$ = new BehaviorSubject<DriverRating[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  DRIVER_RATING: DriverRating[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermDriverId: '',
    searchTermRating: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<DriverRating>,private router: Router) {
    this.service.setMap("indicators/drivers-ratings");
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
              this.service.setMap("indicators/drivers-ratings");
              this.service.findAll().subscribe(data => {
                this.DRIVER_RATING = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.DRIVER_RATING = data;
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
        this._driverRating$.next(result.driverRating);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get driverRating$() {
    return this._driverRating$.asObservable();
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
  get searchTermDriverId() {
    return this._state.searchTermDriverId;
  }
  get searchTermRating() {
    return this._state.searchTermRating;
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

  set searchTermDriverId(searchTermDriverId: string) {
    this._set({searchTermDriverId});
  }
  set searchTermRating(searchTermRating: string) {
    this._set({searchTermRating});
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
    const { sortColumn, sortDirection, pageSize, page, searchTermDate, searchTermDriverId, searchTermRating } = this._state;

    // 1. sort

    let driverRating = sort(this.DRIVER_RATING, sortColumn, sortDirection);

    // 2. filter
    driverRating = driverRating.filter((data) => matches(data, searchTermDate, searchTermDriverId,
      searchTermRating, this.pipe));
    const total = driverRating.length;

    // 3. paginate
    driverRating = driverRating.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ driverRating, total });
  }
}
