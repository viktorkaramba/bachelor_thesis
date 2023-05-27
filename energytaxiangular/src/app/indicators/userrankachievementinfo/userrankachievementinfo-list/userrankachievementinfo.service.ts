import {DriverRating} from "../../../model/driverrating";
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Injectable, PipeTransform} from "@angular/core";
import {DatePipe, DecimalPipe} from "@angular/common";
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {Service} from "../../../service/service.service";
import {debounceTime, delay, switchMap, tap} from "rxjs/operators";
import {UserRankAchievementInfo} from "../../../model/userrankachievementinfo";
import {Router} from "@angular/router";

interface SearchResult {
  userRank: UserRankAchievementInfo[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDateUri: string;
  searchTermUserId: string;
  searchTermRankId: string;
  searchTermNumberOfUsesSale: string;
  searchTermDateDeadline: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(userRank: UserRankAchievementInfo[], column: SortColumn, direction: string): UserRankAchievementInfo[] {
  if (direction === '' || column === '') {
    return userRank;
  } else {
    return [...userRank].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(userRanks: UserRankAchievementInfo, searchTermDateUri: string, searchTermUserId: string,
                 searchTermRankId: string,  searchTermNumberOfUsesSale:string, searchTermDateDeadline:string,
                 pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let dateUri = datePipe.transform(userRanks.dateUri, 'dd/MM/yy  HH:mm:ss');
  let deadlineDateSale = datePipe.transform(userRanks.deadlineDateSale, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    dateUri.includes(searchTermDateUri) &&
    // @ts-ignore
    deadlineDateSale.includes(searchTermDateDeadline) &&
    pipe.transform(userRanks.usersId).includes(searchTermUserId) && pipe.transform(userRanks.ranksId).includes(searchTermRankId)
    &&  pipe.transform(userRanks.numberOfUsesSale).includes(searchTermNumberOfUsesSale)
  );
}

@Injectable({ providedIn: 'root' })
export class UserRankAchievementInfoService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _userRank$ = new BehaviorSubject<UserRankAchievementInfo[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  USER_RANK_INFO: UserRankAchievementInfo[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDateUri: '',
    searchTermUserId: '',
    searchTermRankId: '',
    searchTermNumberOfUsesSale: '',
    searchTermDateDeadline: '',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<UserRankAchievementInfo>, private router: Router) {
    this.service.setMap("bonuses/user-rank-achievements-info");
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
              this.service.setMap("bonuses/user-rank-achievements-info");
              this.service.findAll().subscribe(data => {
                this.USER_RANK_INFO = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.USER_RANK_INFO = data;
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
        this._userRank$.next(result.userRank);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get userRank$() {
    return this._userRank$.asObservable();
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

  get searchTermDateUri() {
    return this._state.searchTermDateUri;
  }
  get searchTermUserId() {
    return this._state.searchTermUserId;
  }

  get searchTermRankId() {
    return this._state.searchTermRankId;
  }

  get searchTermNumberOfUsesSale() {
    return this._state.searchTermNumberOfUsesSale;
  }

  get searchTermDateDeadline() {
    return this._state.searchTermDateDeadline;
  }

  set page(page: number) {
    this._set({ page });
  }

  set pageSize(pageSize: number) {
    this._set({ pageSize });
  }

  set searchTermDateUri(searchTermDateUri: string) {
    this._set({ searchTermDateUri});
  }

  set searchTermUserId(searchTermUserId: string) {
    this._set({searchTermUserId});
  }

  set searchTermRankId(searchTermRankId: string) {
    this._set({searchTermRankId});
  }

  set searchTermNumberOfUsesSale(searchTermNumberOfUsesSale: string) {
    this._set({searchTermNumberOfUsesSale});
  }

  set searchTermDateDeadline(searchTermDateDeadline: string) {
    this._set({searchTermDateDeadline});
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
      searchTermDateUri, searchTermUserId, searchTermRankId, searchTermNumberOfUsesSale,
      searchTermDateDeadline} = this._state;

    // 1. sort

    let userRank = sort(this.USER_RANK_INFO, sortColumn, sortDirection);

    // 2. filter
    userRank = userRank.filter((data) =>
      matches(data, searchTermDateUri, searchTermUserId, searchTermRankId, searchTermNumberOfUsesSale,
        searchTermDateDeadline, this.pipe));
    const total = userRank.length;

    // 3. paginate
    userRank = userRank.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ userRank, total });
  }
}
