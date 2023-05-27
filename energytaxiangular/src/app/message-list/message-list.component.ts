import {Component, OnInit} from '@angular/core';
import {MilitaryBonuses} from "../model/militarybonuses";
import {Driver} from "../model/driver";
import {DriverResumeRequestService} from "../service/driver-resume-request.service";
import {MilitaryBonusesRequestService} from "../service/military-bonuses-request.service";
import {AngularFireDatabase} from "@angular/fire/compat/database";

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit{
  public _militaryBonuses: MilitaryBonuses[] = [];
  public _driversResume: Driver[] = [];
  public photoUrls: {[key: string]: string} = {};
  _bonusStates: {[key: string]: boolean} = {};
  _resumeStates: {[key: string]: boolean} = {};
  _recommendedCarID: {[key: number]: number} = {};
  _recommendedSalary: {[key: number]: number} = {};

  constructor(private driverResumeService: DriverResumeRequestService,
              private militaryBonusesRequestService: MilitaryBonusesRequestService) {
    this.militaryBonusesRequestService.getMilitaryBonusesInfo().subscribe(date=>{
      this._militaryBonuses = date;
      this._militaryBonuses.forEach(bonus => {
        this._bonusStates[bonus.userId] = false;
        const imageString = 'data:image/jpeg;base64,' + bonus.documentPhotoByteArray;
        const byteCharacters = atob(imageString.split(',')[1]);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const imageBlob = new Blob([byteArray], { type: 'image/jpeg' });
        this.photoUrls[bonus.userId] = URL.createObjectURL(imageBlob);
      });
    });
    this.driverResumeService.getDriverResumeInfo().subscribe(date=>{
      this._driversResume = date;
      this._driversResume.forEach(resume => {
        this._resumeStates[resume.driverId] = false;
        this._recommendedCarID[resume.driverId] = resume.carId;
        this._recommendedSalary[resume.driverId] = resume.salary;
      });
    });
  }

  toggleBonus(bonus: MilitaryBonuses) {
    this._bonusStates[bonus.userId] = !this._bonusStates[bonus.userId];
  }

  isBonusOpen(bonus: MilitaryBonuses): boolean {
    return this._bonusStates[bonus.userId];
  }

  toggleResume(driver: Driver) {
    this._resumeStates[driver.driverId] = !this._resumeStates[driver.driverId];
  }

  isResumeOpen(driver: Driver): boolean {
    return this._resumeStates[driver.driverId];
  }

  setBonusAnswer(bonus: MilitaryBonuses, answer: string) {
    this.militaryBonusesRequestService.setAnswer(bonus, answer);
  }

  setResumeAnswer(resume: Driver, answer: string, carId: number) {
    this.driverResumeService.setAnswer(resume, answer, carId);
  }

  getPhoto(userId: number) {
    return this.photoUrls[userId];
  }

  getCarID(driverId: number) {
    return this._recommendedCarID[driverId];
  }

  getSalary(driverId: number) {
    return this._recommendedSalary[driverId];
  }
  ngOnInit(): void {

  }
}
