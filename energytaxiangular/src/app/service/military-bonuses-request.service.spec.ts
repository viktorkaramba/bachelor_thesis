import { TestBed } from '@angular/core/testing';

import { MilitaryBonusesRequestService } from './military-bonuses-request.service';

describe('MilitaryBonusesRequestService', () => {
  let service: MilitaryBonusesRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MilitaryBonusesRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
