/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {WorkLoadDrivers} from "../../../model/workloaddrivers";
import {Router} from "@angular/router";

interface SearchResult {
  workLoadDrivers: WorkLoadDrivers[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermDriverId: string;
  searchTermMorning: string;
  searchTermDay: string;
  searchTermEvening: string;
  searchTermNight: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(workLoadDrivers: WorkLoadDrivers[], column: SortColumn, direction: string): WorkLoadDrivers[] {
  if (direction === '' || column === '') {
    return workLoadDrivers;
  } else {
    return [...workLoadDrivers].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(workLoadDriver: WorkLoadDrivers, searchTermDate: string, searchTermDriverId: string,
                 searchTermMorning: string, searchTermDay: string,searchTermEvening: string,searchTermNight: string,
                 pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(workLoadDriver.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(workLoadDriver.driverId).includes(searchTermDriverId) &&
    pipe.transform(workLoadDriver.morning).includes(searchTermMorning) &&
    pipe.transform(workLoadDriver.day).includes(searchTermDay) &&
    pipe.transform(workLoadDriver.evening).includes(searchTermEvening) &&
    pipe.transform(workLoadDriver.night).includes(searchTermNight)
  );
}

@Injectable({ providedIn: 'root' })
export class WorkLoadDriversService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _workLoadDrivers$ = new BehaviorSubject<WorkLoadDrivers[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  WORKLOAD_DRIVERS: WorkLoadDrivers[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermDriverId: '',
    searchTermMorning: '',
    searchTermDay: '',
    searchTermEvening: '',
    searchTermNight: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<WorkLoadDrivers>,   private router: Router) {
    this.service.setMap("indicators/workloads-drivers");
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
              this.service.setMap("indicators/workloads-drivers");
              this.service.findAll().subscribe(data => {
                this.WORKLOAD_DRIVERS = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.WORKLOAD_DRIVERS = data;
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
        this._workLoadDrivers$.next(result.workLoadDrivers);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get workLoadDrivers$() {
    return this._workLoadDrivers$.asObservable();
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
  get searchTermMorning() {
    return this._state.searchTermMorning;
  }
  get searchTermDay() {
    return this._state.searchTermDay;
  }
  get searchTermEvening() {
    return this._state.searchTermEvening;
  }
  get searchTermNight() {
    return this._state.searchTermNight;
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
  set searchTermMorning(searchTermMorning: string) {
    this._set({searchTermMorning});
  }
  set searchTermDay(searchTermDay: string) {
    this._set({searchTermDay});
  }
  set searchTermEvening(searchTermEvening: string) {
    this._set({searchTermEvening});
  }
  set searchTermNight(searchTermNight: string) {
    this._set({searchTermNight});
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
    const { sortColumn, sortDirection, pageSize, page, searchTermDate, searchTermDriverId, searchTermMorning, searchTermDay
    ,searchTermEvening, searchTermNight} = this._state;

    // 1. sort

    let workLoadDrivers = sort(this.WORKLOAD_DRIVERS, sortColumn, sortDirection);

    // 2. filter
    workLoadDrivers = workLoadDrivers.filter((data) => matches(data, searchTermDate, searchTermDriverId,
      searchTermMorning,searchTermDay,searchTermEvening, searchTermNight,  this.pipe));
    const total = workLoadDrivers.length;

    // 3. paginate
    workLoadDrivers = workLoadDrivers.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ workLoadDrivers, total });
  }
}
