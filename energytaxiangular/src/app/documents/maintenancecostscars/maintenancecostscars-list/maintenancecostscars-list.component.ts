import {Component, OnInit} from '@angular/core';
import {Service} from "../../../service/service.service";
import {MaintenanceCostsCars} from "../../../model/maintenancecostscars";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-maintenancecostscars-list',
  templateUrl: './maintenancecostscars-list.component.html',
  styleUrls: ['./maintenancecostscars-list.component.css']
})
export class MaintenanceCostsCarsListComponent implements OnInit {

  maintenanceCostsCars: MaintenanceCostsCars[] | undefined;
  isEdit: boolean[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<MaintenanceCostsCars>) {
    this.service.setMap("documents/maintenance-costs-cars");
  }

  ngOnInit() {
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
              this.service.setMap("documents/maintenance-costs-cars");
              this.service.findAll().subscribe(data => {
                this.maintenanceCostsCars = data;
                this.isEdit?.fill(false, 0, this.maintenanceCostsCars?.length)
                console.log("get");
                console.log(data);
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => {
      this.maintenanceCostsCars = data;
      this.isEdit?.fill(false, 0, this.maintenanceCostsCars?.length)
      console.log("get");
      console.log(data);
    });
  }


  setIsEdit(value: boolean, index: number){
    this.isEdit[index] = value;
  }

  delete(id:number){
    console.log(id);
    this.service.deleteById(id).pipe(
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
              this.service.setMap("documents/maintenance-costs-cars");
              this.service.deleteById(id).subscribe(data => {
                location.reload();
              });
            }
          });
        }
        return of([]);
      })
    ).subscribe(data => { location.reload();});
  }

  update(maintenanceCostsCar: MaintenanceCostsCars, index: number){
    this.service.update(maintenanceCostsCar).pipe(
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
              this.service.setMap("documents/maintenance-costs-cars");
              this.service.update(maintenanceCostsCar).subscribe(() => this.gotoList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoList());
    this.setIsEdit(false, index);
  }

  gotoList() {
    this.router.navigate(['manager/maintenance-costs-car/maintenance-costs-car/maintenance-costs-cars']);
  }
}
