import {DriverRating} from "../../../model/driverrating";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Injectable, PipeTransform} from "@angular/core";
import {DatePipe, DecimalPipe} from "@angular/common";
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {Service} from "../../../service/service.service";
import {debounceTime, delay, switchMap, tap} from "rxjs/operators";
import {FavouriteAddress} from "../../../model/favouriteaddress";
import {Router} from "@angular/router";

interface SearchResult {
  favouriteAddress: FavouriteAddress[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchUserId: string;
  searchTermAddress: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(favouriteAddress: FavouriteAddress[], column: SortColumn, direction: string): FavouriteAddress[] {
  if (direction === '' || column === '') {
    return favouriteAddress;
  } else {
    return [...favouriteAddress].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(favouriteAddresses: FavouriteAddress, searchUserId: string, searchTermAddress: string,
                 pipe: PipeTransform) {
  return (
    favouriteAddresses.address.toLowerCase().includes(searchTermAddress.toLowerCase()) &&
    pipe.transform(favouriteAddresses.userId).includes(searchUserId)
  );
}

@Injectable({ providedIn: 'root' })
export class FavouriteAddressService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _favouriteAddresses$ = new BehaviorSubject<FavouriteAddress[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  FAVOURITE_ADDRESSES: FavouriteAddress[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchUserId: '',
    searchTermAddress: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<FavouriteAddress>,private router: Router) {
    this.service.setMap("indicators/favourite-addresses");
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
              this.service.setMap("indicators/favourite-addresses");
              this.service.findAll().subscribe(data => {
                this.FAVOURITE_ADDRESSES = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.FAVOURITE_ADDRESSES = data;
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
        this._favouriteAddresses$.next(result.favouriteAddress);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get favouriteAddresses$() {
    return this._favouriteAddresses$.asObservable();
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
  get searchUserId() {
    return this._state.searchUserId;
  }
  get searchTermAddress() {
    return this._state.searchTermAddress;
  }

  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchUserId(searchUserId: string) {
    this._set({ searchUserId});
  }

  set searchTermAddress(searchTermAddress: string) {
    this._set({searchTermAddress});
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
      searchUserId, searchTermAddress} = this._state;

    // 1. sort

    let favouriteAddress = sort(this.FAVOURITE_ADDRESSES, sortColumn, sortDirection);

    // 2. filter
    favouriteAddress = favouriteAddress.filter((data) => matches(data, searchUserId, searchTermAddress,
      this.pipe));
    const total = favouriteAddress.length;

    // 3. paginate
    favouriteAddress = favouriteAddress.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ favouriteAddress, total });
  }
}
