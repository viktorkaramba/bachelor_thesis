import {TestBed} from '@angular/core/testing';

import {Service} from './service.service';

describe('CarService', () => {
  let service: Service<any>;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
