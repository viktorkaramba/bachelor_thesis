/* eslint-disable @typescript-eslint/adjacent-overload-signatures */
import {Injectable, PipeTransform} from '@angular/core';

import {BehaviorSubject, catchError, Observable, of, Subject} from 'rxjs';


import {DatePipe, DecimalPipe} from '@angular/common';
import {debounceTime, delay, switchMap, tap} from 'rxjs/operators';
import {SortColumn, SortDirection} from "../../../service/sortable.directive";
import {Service} from "../../../service/service.service";
import {Order} from "../../../model/order";
import {Router} from "@angular/router";

interface SearchResult {
  orders: Order[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTermDate: string;
  searchTermDriverId: string;
  searchTermCustomerName: string;
  searchTermAddressCustomer: string;
  searchTermDeliveryAddress: string;
  searchTermCustomerTelephone: string;
  searchTermPrice: string;
  searchTermRating: string;
  searchTermNumberOfKilometers: string;
  searchTermUserId: string;
  searchTermUserComment: string;
  searchTermIsUseSale:string;
  searchTermIsFreeOrder:string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

function sort(orders: Order[], column: SortColumn, direction: string): Order[] {
  if (direction === '' || column === '') {
    return orders;
  } else {
    return [...orders].sort((a, b) => {
      // @ts-ignore
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(orders: Order, searchTermDate: string, searchTermDriverId: string, searchTermCustomerName: string,
                 searchTermAddressCustomer: string, searchTermDeliveryAddress: string, searchTermCustomerTelephone: string,
                 searchTermPrice: string, searchTermRating: string, searchTermNumberOfKilometers: string,
                 searchTermUserId: string, searchTermUserComment: string, searchTermIsUseSale:string,
                 searchTermIsFreeOrder:string, pipe: PipeTransform) {
  const datePipe = new DatePipe('en-US');
  let date = datePipe.transform(orders.date, 'dd/MM/yy  HH:mm:ss');
  return (
    // @ts-ignore
    date.includes(searchTermDate) &&
    pipe.transform(orders.driverId).includes(searchTermDriverId) &&
    orders.customerName.toLowerCase().includes(searchTermCustomerName.toLowerCase()) &&
    orders.addressCustomer.toLowerCase().includes(searchTermAddressCustomer.toLowerCase()) &&
    orders.addressDelivery.toLowerCase().includes(searchTermDeliveryAddress.toLowerCase()) &&
    orders.userComment.toLowerCase().includes(searchTermUserComment.toLowerCase()) &&
    orders.telephoneCustomer.includes(searchTermCustomerTelephone) &&
    orders.useSale.toString().includes(searchTermIsUseSale)
    && orders.isUseFreeOrder.toLowerCase().includes(searchTermIsFreeOrder.toLowerCase()) &&
    pipe.transform(orders.price).includes(searchTermPrice) &&
    pipe.transform(orders.rating).includes(searchTermRating) &&
    pipe.transform(orders.numberOfKilometers).includes(searchTermNumberOfKilometers) &&
      pipe.transform(orders.userId).includes(searchTermUserId)
  );
}

@Injectable({ providedIn: 'root' })
export class OrdersService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _orders$ = new BehaviorSubject<Order[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private  ORDERS: Order[] = [];
  private _state: State = {
    page: 1,
    pageSize: 4,
    searchTermDate: '',
    searchTermDriverId: '',
    searchTermCustomerName: '',
    searchTermAddressCustomer: '',
    searchTermDeliveryAddress: '',
    searchTermCustomerTelephone: '',
    searchTermPrice: '',
    searchTermRating: '',
    searchTermNumberOfKilometers: '',
    searchTermUserId: '',
    searchTermUserComment: '',
    searchTermIsUseSale:'',
    searchTermIsFreeOrder:'',
    sortColumn: '',
    sortDirection: '',
  };

  constructor(private pipe: DecimalPipe, private service: Service<Order>, private router: Router) {
    this.service.setMap("indicators/orders");
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
              this.service.setMap("indicators/orders");
              this.service.findAll().subscribe(data => {
                this.ORDERS = data;
                console.log("get" );
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.ORDERS = data;
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
        this._orders$.next(result.orders);
        this._total$.next(result.total);
      });

    this._search$.next();
  }

  get orders$() {
    return this._orders$.asObservable();
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
  get searchTermCustomerName() {
    return this._state.searchTermCustomerName;
  }
  get searchTermAddressCustomer() {
    return this._state.searchTermAddressCustomer;
  }
  get searchTermDeliveryAddress() {
    return this._state.searchTermDeliveryAddress;
  }
  get searchTermCustomerTelephone() {
    return this._state.searchTermCustomerTelephone;
  }
  get searchTermPrice() {
    return this._state.searchTermPrice;
  }
  get searchTermRating() {
    return this._state.searchTermRating;
  }
  get searchTermNumberOfKilometers() {
    return this._state.searchTermNumberOfKilometers;
  }

  get searchTermUserId() {
    return this._state.searchTermUserId;
  }

  get searchTermUserComment() {
    return this._state.searchTermUserComment;
  }

  get searchTermIsUseSale() {
    return this._state.searchTermIsUseSale;
  }

  get searchTermIsFreeOrder() {
    return this._state.searchTermIsFreeOrder;
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
  set searchTermCustomerName(searchTermCustomerName: string) {
    this._set({searchTermCustomerName});
  }
  set searchTermAddressCustomer(searchTermAddressCustomer: string) {
    this._set({searchTermAddressCustomer});
  }
  set searchTermDeliveryAddress(searchTermDeliveryAddress: string) {
    this._set({searchTermDeliveryAddress});
  }
  set searchTermCustomerTelephone(searchTermCustomerTelephone: string) {
    this._set({searchTermCustomerTelephone});
  }
  set searchTermPrice(searchTermPrice: string) {
    this._set({searchTermPrice});
  }
  set searchTermRating(searchTermRating: string) {
    this._set({searchTermRating});
  }
  set searchTermNumberOfKilometers(searchTermNumberOfKilometers: string) {
    this._set({searchTermNumberOfKilometers});
  }
  set searchTermUserId(searchTermUserId:string){
    this._set({searchTermUserId})
  }

  set searchTermUserComment(searchTermUserComment:string){
    this._set({searchTermUserComment})
  }

  set searchTermIsUseSale(searchTermIsUseSale:string){
    this._set({searchTermIsUseSale})
  }

  set searchTermIsFreeOrder(searchTermIsFreeOrder:string){
    this._set({searchTermIsFreeOrder})
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
      searchTermDate, searchTermDriverId, searchTermCustomerName, searchTermAddressCustomer,
      searchTermDeliveryAddress, searchTermCustomerTelephone, searchTermPrice, searchTermRating,
      searchTermNumberOfKilometers, searchTermUserId, searchTermUserComment, searchTermIsUseSale,
      searchTermIsFreeOrder} = this._state;

    // 1. sort

    let orders = sort(this.ORDERS, sortColumn, sortDirection);

    // 2. filter
    orders = orders.filter((data) => matches(data, searchTermDate, searchTermDriverId, searchTermCustomerName
      , searchTermAddressCustomer, searchTermDeliveryAddress, searchTermCustomerTelephone, searchTermPrice, searchTermRating
      , searchTermNumberOfKilometers, searchTermUserId, searchTermUserComment, searchTermIsUseSale, searchTermIsFreeOrder,
      this.pipe));
    const total = orders.length;

    // 3. paginate
    orders = orders.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({ orders, total });
  }
}
