import { TestBed } from '@angular/core/testing';

import { DriverResumeRequestService } from './driver-resume-request.service';

describe('DriverResumeRequestService', () => {
  let service: DriverResumeRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverResumeRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
