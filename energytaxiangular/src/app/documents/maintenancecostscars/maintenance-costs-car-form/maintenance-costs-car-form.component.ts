import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../service/service.service";
import {MaintenanceCostsCars} from "../../../model/maintenancecostscars";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-maintenance-costs-car-form',
  templateUrl: './maintenance-costs-car-form.component.html',
  styleUrls: ['./maintenance-costs-car-form.component.css']
})
export class MaintenanceCostsCarFormComponent implements OnInit {

  maintenanceCostsCars: MaintenanceCostsCars;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Service<MaintenanceCostsCars>) {
    this.service.setMap("documents/maintenance-costs-cars");
    this.maintenanceCostsCars = new MaintenanceCostsCars();
  }

  onSubmit() {
    this.service.save(this.maintenanceCostsCars).pipe(
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
              this.service.save(this.maintenanceCostsCars).subscribe(() => this.gotoMaintenanceCostsCarsList());
            }
          });
        }
        return of([]);
      })
    ).subscribe(() => this.gotoMaintenanceCostsCarsList());
  }

  gotoMaintenanceCostsCarsList() {
    this.router.navigate(['manager/maintenance-costs-car/maintenance-costs-car/maintenance-costs-cars']);
  }

  ngOnInit(): void {
  }
}
