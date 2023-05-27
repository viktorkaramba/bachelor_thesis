import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AngularFireDatabase} from "@angular/fire/compat/database";
import {Loader} from "@googlemaps/js-api-loader";
import {NETISHYN_BOUNDS, styles} from "../mapstyles";
import {DriverResumeRequestService} from "../service/driver-resume-request.service";
import {MilitaryBonusesRequestService} from "../service/military-bonuses-request.service";
import {Event, NavigationEnd, NavigationStart, Router} from '@angular/router';
import {DriverInfo} from "../model/driverInfo";


@Component({
  selector: 'app-manager',
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css']
})
export class ManagerComponent implements OnInit {
  @ViewChild('map') mapDiv: ElementRef | undefined;
  private driversList: DriverInfo[] | undefined;
  private map: google.maps.Map | undefined;
  private driverMarkerMap: Map<number, google.maps.Marker>;
  title: string;
  showContent = true;
  public notificationCount = 0;

  constructor(private db: AngularFireDatabase, private driverResumeService: DriverResumeRequestService,
              private militaryBonusesRequestService: MilitaryBonusesRequestService, private router: Router) {
    this.title = 'EnergyTaxi';
    this.router.events.subscribe((event: Event) => {

      if (event instanceof NavigationEnd) {
        // Hide progress spinner or progress bar
        this.showContent = this.router.url === '/manager';
        console.log(this.router.url);
      }

      if (event instanceof NavigationStart) {
        // Hide progress spinner or progress bar
        this.showContent = this.router.url === '/manager';
        console.log(this.router.url);
      }
    });
    this.driverMarkerMap = new Map<number, google.maps.Marker>();
    this.getDrivers().then(r => {
      this.driversList = r as DriverInfo[];
      for(let driver of this.driversList) {
        if (driver.status === 'ONLINE' || driver.status === 'IN_ORDER'){
          this.addMarker(driver);
        }
      }
    });
    this.driverListener();
  }


   getDrivers(){
    return new Promise((resolve, reject)=>{
      this.db.list('drivers-info').valueChanges().subscribe(data=>{
        console.log(data);
        resolve(data);
      })
    });
  }

  driverListener(){
    this.db.list('drivers-info').valueChanges().subscribe(changes => {
      let drivers = changes as unknown as DriverInfo[];
      for(let driver of drivers) {
        if (driver.status === 'ONLINE' || driver.status === 'IN_ORDER'){
          if(this.checkIsMarkerExist(driver)){
            this.addMarker(driver);
          }
        }
        else {
          this.deleteMarker(driver);
        }
      }
    });
  }

  checkIsMarkerExist(driver: DriverInfo | any){
    let isExist: boolean = false;
    for(let entry of this.driverMarkerMap.entries()){
      if(entry[0] == driver.driverId){
        isExist = true;
        const location = {
          lat: driver.latitude,
          lng: driver.longitude
        };
        if(location != entry[1].getPosition()){
          entry[1].setVisible(false);
          this.driverMarkerMap.delete(entry[0]);
          return true;
        }
      }
    }
    if(!isExist){
      return true;
    }
    return false;
  }

  addMarker(driver: DriverInfo | any){
    let iconType: string = '';
    if(driver.status === 'ONLINE'){
      iconType = '../resources/img/default_car.svg';
    }else{
      iconType = '../resources/img/in_order_car.svg';
    }
    const location = {
      lat: driver.latitude,
      lng: driver.longitude
    };
    let fullName = driver.driverFirstName + " " + driver.driverSurName;
    const marker = new google.maps.Marker({
      position: location,
      map:this.map,
      title: fullName,
      icon: {
        url: iconType,
        labelOrigin: new google.maps.Point(12, -5)
      },
      label: {
        text: fullName,
        color: '#ffffff',
        fontSize: '12px'
      }
    });
    this.driverMarkerMap.set(driver.driverId, marker);
  }

  deleteMarker(driver: DriverInfo | any): void {
    for(let entry of this.driverMarkerMap.entries()){
      if(entry[0] == driver.driverId){
        entry[1].setVisible(false);
        this.driverMarkerMap.delete(entry[0]);
      }
    }
  }

  ngOnInit(): void {
    this.driverResumeService.getDriverResumeInfo().subscribe(data => {
      this.notificationCount = data.length;
      this.militaryBonusesRequestService.getMilitaryBonusesInfo().subscribe(data => {
        this.notificationCount += data.length;
      });
    });
    let loader = new Loader({
      apiKey:'AIzaSyA-OPsE3b6OZgPme5XDIzl_o1xV64aRWaw'
    });

    loader.load().then(()=>{
      const location = {
        lat: 50.33421127146544,
        lng: 26.650803556849187
      };
      this.map = new google.maps.Map(this.mapDiv?.nativeElement, {
        center: location,
        zoom: 15,
        styles: styles,
        minZoom:10,
        mapTypeControl:false,
        disableDefaultUI: true,
        restriction:{
          latLngBounds: NETISHYN_BOUNDS,
          strictBounds: false,
        }
      })
    });
  }

}


