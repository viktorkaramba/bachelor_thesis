import {DriverRating} from "../../../model/driverrating";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Injectable, PipeTransform} from "@angular/core";
import {DatePipe, DecimalPipe} from "@angular/common";
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {Service} from "../../../service/service.service";
import {debounceTime, delay, switchMap, tap} from "rxjs/operators";
import {FavouriteAddress} from "../../../model/favouriteaddress";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {MilitaryBonuses} from "../../../model/militarybonuses";
import {Router} from "@angular/router";

interface SearchResult {
  militaryBonus: MilitaryBonuses[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermUserId: string;
  searchTermStatus: string;
  searchTermDate: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(militaryBonus: MilitaryBonuses[], column: SortColumn, direction: string): MilitaryBonuses[] {
  if (direction === '' || column === '') {
    return militaryBonus;
  } else {
    return [...militaryBonus].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(militaryBonuses: MilitaryBonuses, searchTermUserId: string, searchTermStatus: string,
                 searchTermDate: string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(militaryBonuses.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(militaryBonuses.userId).includes(searchTermUserId) &&
    militaryBonuses.militaryBonusStatus.toLowerCase().includes(searchTermStatus.toLowerCase())
  );
}

@Injectable({ providedIn: 'root' })
export class MilitaryBonusService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _militaryBonuses$ = new BehaviorSubject<MilitaryBonuses[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  MILITARY_BONUSES: MilitaryBonuses[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermUserId: '',
    searchTermStatus: '',
    searchTermDate: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<MilitaryBonuses>, private router: Router) {
    this.service.setMap("bonuses/military-bonuses");
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
              this.service.setMap("bonuses/military-bonuses");
              this.service.findAll().subscribe(data => {
                this.MILITARY_BONUSES = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.MILITARY_BONUSES = data;
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
        this._militaryBonuses$.next(result.militaryBonus);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get militaryBonuses$() {
    return this._militaryBonuses$.asObservable();
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
  get searchTermUserId() {
    return this._state.searchTermUserId
  }
  get searchTermStatus() {
    return this._state.searchTermStatus;
  }

  get searchTermDate() {
    return this._state.searchTermDate;
  }


  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermUserId(searchTermUserId: string) {
    this._set({ searchTermUserId});
  }

  set searchTermStatus(searchTermStatus: string) {
    this._set({searchTermStatus});
  }

  set searchTermDate(searchTermDate: string) {
    this._set({searchTermDate});
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
      searchTermUserId, searchTermStatus, searchTermDate} = this._state;

    // 1. sort

    let militaryBonus = sort(this.MILITARY_BONUSES, sortColumn, sortDirection);

    // 2. filter
    militaryBonus = militaryBonus.filter((data) => matches(data, searchTermUserId, searchTermStatus, searchTermDate,
      this.pipe));
    const total = militaryBonus.length;

    // 3. paginate
    militaryBonus = militaryBonus.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ militaryBonus, total });
  }
}
