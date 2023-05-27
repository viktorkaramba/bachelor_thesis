import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Report} from "../model/report";
import {LoginResponse} from "../model/login-info";
import {RegisterResponse} from "../model/register-response";

@Injectable()
export class Service<T> {

  public url: string;
  constructor(private http: HttpClient) {
    this.url="";
  }

  public auth(object: T){
    return this.http.post<LoginResponse>(this.url, object);
  }

  public logout(){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    let options={
      headers:httpHeaders
    };
    return this.http.get<RegisterResponse>(this.url, options);
  }

  public setMap(map:string){
    this.url="http://localhost:8081/api/v1/"+map;
  }

  public findAll(){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    return this.http.get<T[]>(this.url, options);
  }
  public save(object: T) {
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    return this.http.post<T>(this.url, object, options);
  }
  public deleteById(id:number){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    let deleteUrl = this.url + "/" + id;
    return this.http.delete<number>(deleteUrl, options);
  }

  public update(object: T){
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    return this.http.put<T>(this.url, object, options);
  }

  public getReport(report: Report): Observable<T[]>{
    let httpHeaders = new HttpHeaders()
      .set('Content-type','application/Json')
      .set('Authorization','Bearer ' + localStorage.getItem("accessToken"));
    let options={
      headers:httpHeaders
    };
    return this.http.post<T[]>(this.url, report, options);
  }

  public refreshToken(){
    this.setMap("auth/refresh-token")
    console.log(localStorage.getItem("refreshToken"));
    let httpHeaders = new HttpHeaders()
      .set('Content-type', 'application/Json')
      .set('Authorization', 'Bearer ' + localStorage.getItem("refreshToken"));
    let options={
      headers:httpHeaders
    };
    console.log("options: " + options.headers.get("Authorization"));
    return this.http.post<RegisterResponse>(this.url,null, {headers: httpHeaders});
  }
}
