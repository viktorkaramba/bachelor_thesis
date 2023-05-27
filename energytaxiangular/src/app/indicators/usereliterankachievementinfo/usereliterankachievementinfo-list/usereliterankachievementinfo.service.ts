import {DriverRating} from "../../../model/driverrating";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Injectable, PipeTransform} from "@angular/core";
import {DatePipe, DecimalPipe} from "@angular/common";
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {Service} from "../../../service/service.service";
import {debounceTime, delay, switchMap, tap} from "rxjs/operators";
import {FavouriteAddress} from "../../../model/favouriteaddress";
import {FavouriteDriver} from "../../../model/favouritedriver";
import {
  UsereliterankachievementinfoModuleModule
} from "../usereliterankachievementinfo-module/usereliterankachievementinfo-module.module";
import {UserEliteRankAchievementInfo} from "../../../model/usereliterankachievementinfo";
import {Router} from "@angular/router";

interface SearchResult {
  userEliteRank: UserEliteRankAchievementInfo[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDateUeraiId: string;
  searchTermUserId: string;
  searchTermErId: string;
  searchTermNumberOfUsesFreeOrder: string;
  searchTermDateDeadline: string;
  searchTermCcID: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(userEliteRank: UserEliteRankAchievementInfo[], column: SortColumn, direction: string): UserEliteRankAchievementInfo[] {
  if (direction === '' || column === '') {
    return userEliteRank;
  } else {
    return [...userEliteRank].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(userEliteRanks: UserEliteRankAchievementInfo, searchTermDateUeraiId: string, searchTermUserId: string,
                 searchTermErId: string,  searchTermNumberOfUsesFreeOrder:string, searchTermDateDeadline:string,
                 searchTermCcID: string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let dateUerai = datePipe.transform(userEliteRanks.dateUerai, 'dd/MM/yy  HH:mm:ss');
  let deadlineDateFreeOrder = datePipe.transform(userEliteRanks.deadlineDateFreeOrder, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    dateUerai.includes(searchTermDateUeraiId) &&
    // @ts-ignore
    deadlineDateFreeOrder.includes(searchTermDateDeadline) &&
    pipe.transform(userEliteRanks.usersId).includes(searchTermUserId) && pipe.transform(userEliteRanks.eliteRanksId).includes(searchTermErId)
    &&  pipe.transform(userEliteRanks.numberOfUsesFreeOrder).includes(searchTermNumberOfUsesFreeOrder)
    &&  pipe.transform(userEliteRanks.carClassId).includes(searchTermCcID)
  );
}

@Injectable({ providedIn: 'root' })
export class UserEliteRankAchievementInfoService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _userEliteRank$ = new BehaviorSubject<UserEliteRankAchievementInfo[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  USER_ELITE_RANK_INFO: UserEliteRankAchievementInfo[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDateUeraiId: '',
    searchTermUserId: '',
    searchTermErId: '',
    searchTermNumberOfUsesFreeOrder: '',
    searchTermDateDeadline: '',
    searchTermCcID: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<UserEliteRankAchievementInfo>, private router: Router) {
    this.service.setMap("bonuses/user-elite-rank-achievements-info");
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
              this.service.setMap("bonuses/user-elite-rank-achievements-info");
              this.service.findAll().subscribe(data => {
                this.USER_ELITE_RANK_INFO = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.USER_ELITE_RANK_INFO = data;
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
        this._userEliteRank$.next(result.userEliteRank);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get userEliteRank$() {
    return this._userEliteRank$.asObservable();
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

  get searchTermDateUeraiId() {
    return this._state.searchTermDateUeraiId;
  }
  get searchTermUserId() {
    return this._state.searchTermUserId;
  }

  get searchTermErId() {
    return this._state.searchTermErId;
  }

  get searchTermNumberOfUsesFreeOrder() {
    return this._state.searchTermNumberOfUsesFreeOrder;
  }

  get searchTermDateDeadline() {
    return this._state.searchTermDateDeadline;
  }
  get searchTermCcID() {
    return this._state.searchTermCcID;
  }
  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermDateUeraiId(searchTermDateUeraiId: string) {
    this._set({ searchTermDateUeraiId});
  }

  set searchTermUserId(searchTermUserId: string) {
    this._set({searchTermUserId});
  }

  set searchTermErId(searchTermErId: string) {
    this._set({searchTermErId});
  }

  set searchTermNumberOfUsesFreeOrder(searchTermNumberOfUsesFreeOrder: string) {
    this._set({searchTermNumberOfUsesFreeOrder});
  }

  set searchTermDateDeadline(searchTermDateDeadline: string) {
    this._set({searchTermDateDeadline});
  }

  set searchTermCcID(searchTermCcID: string) {
    this._set({searchTermCcID});
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
      searchTermDateUeraiId, searchTermUserId, searchTermErId, searchTermNumberOfUsesFreeOrder,
    searchTermDateDeadline, searchTermCcID} = this._state;

    // 1. sort

    let userEliteRank = sort(this.USER_ELITE_RANK_INFO, sortColumn, sortDirection);

    // 2. filter
    userEliteRank = userEliteRank.filter((data) =>
      matches(data, searchTermDateUeraiId, searchTermUserId, searchTermErId, searchTermNumberOfUsesFreeOrder,
        searchTermDateDeadline, searchTermCcID, this.pipe));
    const total = userEliteRank.length;

    // 3. paginate
    userEliteRank = userEliteRank.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ userEliteRank, total });
  }
}
