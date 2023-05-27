/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {DriverCarRecommendations} from "../../../model/drivercarrecommendations";
import {Router} from "@angular/router";

interface SearchResult {
  driverCarRecs: DriverCarRecommendations[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermDriverId: string;
  searchTermCarId: string;
  searchTermStatus: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(driverCarRecs: DriverCarRecommendations[], column: SortColumn, direction: string): DriverCarRecommendations[] {
  if (direction === '' || column === '') {
    return driverCarRecs;
  } else {
    return [...driverCarRecs].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(driverCarRec: DriverCarRecommendations, searchTermDate: string, searchTermDriverId: string,
                 searchTermCarId: string, searchTermStatus: string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(driverCarRec.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(driverCarRec.driverId).includes(searchTermDriverId) &&
    pipe.transform(driverCarRec.carId).includes(searchTermCarId) &&
    driverCarRec.status.toLowerCase().includes(searchTermStatus.toLowerCase())
  );
}

@Injectable({ providedIn: 'root' })
export class DriveCarRecService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _driverCarRec$ = new BehaviorSubject<DriverCarRecommendations[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  DRIVE_CAR_REC: DriverCarRecommendations[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermDriverId: '',
    searchTermCarId: '',
    searchTermStatus: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<DriverCarRecommendations>, private router: Router) {
    this.service.setMap("indicators/drivers-cars-recommendations");
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
              this.service.setMap("indicators/drivers-cars-recommendations");
              this.service.findAll().subscribe(data => {
                this.DRIVE_CAR_REC = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.DRIVE_CAR_REC = data;
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
        this._driverCarRec$.next(result.driverCarRecs);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get driverCarRec() {
    return this._driverCarRec$.asObservable();
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
  get searchTermCarId() {
    return this._state.searchTermCarId;
  }
  get searchTermStatus() {
    return this._state.searchTermStatus;
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
  set searchTermCarId(searchTermCarId: string) {
    this._set({searchTermCarId});
  }
  set searchTermStatus(searchTermStatus: string) {
    this._set({searchTermStatus});
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
    const { sortColumn, sortDirection, pageSize, page, searchTermDate, searchTermDriverId, searchTermCarId,
      searchTermStatus } = this._state;
    let driverCarRecs = sort(this.DRIVE_CAR_REC, sortColumn, sortDirection);

    driverCarRecs = driverCarRecs.filter((data) => matches(data, searchTermDate, searchTermDriverId,
      searchTermCarId, searchTermStatus, this.pipe));
    const total = driverCarRecs.length;

    driverCarRecs = driverCarRecs.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ driverCarRecs, total });
  }
}
