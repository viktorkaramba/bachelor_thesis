import {DriverRating} from "../../../model/driverrating";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Injectable, PipeTransform} from "@angular/core";
import {DatePipe, DecimalPipe} from "@angular/common";
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {Service} from "../../../service/service.service";
import {debounceTime, delay, switchMap, tap} from "rxjs/operators";
import {FavouriteAddress} from "../../../model/favouriteaddress";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {Router} from "@angular/router";

interface SearchResult {
  favouriteDriver: FavouriteDriver[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDriverId: string;
  searchTermUserId: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(favouriteDriver: FavouriteDriver[], column: SortColumn, direction: string): FavouriteDriver[] {
  if (direction === '' || column === '') {
    return favouriteDriver;
  } else {
    return [...favouriteDriver].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(favouriteDrivers: FavouriteDriver, searchTermDriverId: string, searchTermUserId: string,
                 pipe: PipeTransform) {
  return (
    pipe.transform(favouriteDrivers.driverId).includes(searchTermDriverId) &&
    pipe.transform(favouriteDrivers.userId).includes(searchTermUserId)
  );
}

@Injectable({ providedIn: 'root' })
export class FavouriteDriverService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _favouriteDrivers$ = new BehaviorSubject<FavouriteDriver[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  FAVOURITE_DRIVERS: FavouriteDriver[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDriverId: '',
    searchTermUserId: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<FavouriteDriver>,private router: Router) {
    this.service.setMap("indicators/favourite-drivers");
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
              this.service.setMap("indicators/favourite-drivers");
              this.service.findAll().subscribe(data => {
                this.FAVOURITE_DRIVERS = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.FAVOURITE_DRIVERS = data;
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
        this._favouriteDrivers$.next(result.favouriteDriver);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get favouriteDrivers$() {
    return this._favouriteDrivers$.asObservable();
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
  get searchTermDriverId() {
    return this._state.searchTermDriverId;
  }
  get searchTermUserId() {
    return this._state.searchTermUserId;
  }

  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermDriverId(searchTermDriverId: string) {
    this._set({ searchTermDriverId});
  }

  set searchTermUserId(searchTermUserId: string) {
    this._set({searchTermUserId});
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
    const { sortColumn, sortDirection, pageSize, page,
      searchTermDriverId, searchTermUserId} = this._state;

    // 1. sort

    let favouriteDriver = sort(this.FAVOURITE_DRIVERS, sortColumn, sortDirection);

    // 2. filter
    favouriteDriver = favouriteDriver.filter((data) => matches(data, searchTermDriverId, searchTermUserId,
      this.pipe));
    const total = favouriteDriver.length;

    // 3. paginate
    favouriteDriver = favouriteDriver.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ favouriteDriver, total });
  }
}
