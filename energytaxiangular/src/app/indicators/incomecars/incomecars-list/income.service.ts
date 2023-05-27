/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {IncomeCars} from "../../../model/incomecars";
import {Router} from "@angular/router";

interface SearchResult {
  incomeCars: IncomeCars[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermCarId: string;
  searchTermExpenses: string;
  searchTermIncome: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(incomeCars: IncomeCars[], column: SortColumn, direction: string): IncomeCars[] {
  if (direction === '' || column === '') {
    return incomeCars;
  } else {
    return [...incomeCars].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(incomeCar: IncomeCars, searchTermDate: string, searchTermCarID: string, searchTermExpenses:
                   string,searchTermIncome: string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(incomeCar.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(incomeCar.carId).includes(searchTermCarID) &&
    pipe.transform(incomeCar.expenses).includes(searchTermExpenses) &&
    pipe.transform(incomeCar.income).includes(searchTermIncome)
  );
}

@Injectable({ providedIn: 'root' })
export class IncomeCarsService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _incomeCars$ = new BehaviorSubject<IncomeCars[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  INCOME_CARS: IncomeCars[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermCarId:'',
    searchTermExpenses:'',
    searchTermIncome:'',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<IncomeCars>, private router: Router) {
    this.service.setMap("indicators/income-cars");
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
              this.service.setMap("indicators/income-cars");
              this.service.findAll().subscribe(data => {
                this.INCOME_CARS = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.INCOME_CARS = data;
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
        this._incomeCars$.next(result.incomeCars);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get incomeCars$() {
    return this._incomeCars$.asObservable();
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
  get searchTermCarId() {
    return this._state.searchTermCarId;
  }
  get searchTermExpenses() {
    return this._state.searchTermExpenses;
  }
  get searchTermIncome() {
    return this._state.searchTermIncome;
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

  set searchTermCarId(searchTermCarId: string) {
    this._set({searchTermCarId});
  }

  set searchTermExpenses(searchTermExpenses: string) {
    this._set({searchTermExpenses});
  }
  set searchTermIncome(searchTermIncome: string) {
    this._set({searchTermIncome});
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
    const { sortColumn, sortDirection, pageSize, page, searchTermDate, searchTermCarId, searchTermExpenses,
      searchTermIncome } = this._state;

    // 1. sort

    let incomeCars = sort(this.INCOME_CARS, sortColumn, sortDirection);

    // 2. filter
    incomeCars = incomeCars.filter((data) => matches(data, searchTermDate, searchTermCarId,
      searchTermExpenses, searchTermIncome, this.pipe));
    const total = incomeCars.length;

    // 3. paginate
    incomeCars = incomeCars.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ incomeCars, total });
  }
}
