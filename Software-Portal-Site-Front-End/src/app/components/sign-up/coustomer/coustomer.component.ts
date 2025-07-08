import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from 'src/app/services/customer.service';
import { LoadingService } from 'src/app/services/loading.service';
import { TimeSlotServiceService } from 'src/app/services/time-slot-service.service';
import { ZoomService } from 'src/app/services/zoom.service';
import { Customer } from 'src/app/shared/customer';
import { TimeSlots } from 'src/app/shared/timeSlots';
import { Zoom } from 'src/app/shared/zoom';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-coustomer',
  templateUrl: './coustomer.component.html',
  styleUrls: ['./coustomer.component.css']
})
export class CoustomerComponent {
  durationOptions: number[] = [15, 30, 45, 60, 90, 120, 180, 240, 300, 360];
  duration1: number = 30; // Default selection
  
  customer: Customer = {
    customerId: '',
    customerAddress: '',
    customerNumber: '',
    customerEmail: '',
    customerName: '',
    companyRegNo: '',
    customerType: '',
    commonStatus: 'ACTIVE',
    systemProfileDto: {
      systemProfilesId: ''
    }

  }

  zoom: Zoom = {
    sheduleTopic: '',
    startDate: '',
    startTime: '',
    zoomLink: '',
    meetingPassword: '',
    meeting_Duration: '',
    requestStatus: '',
    commonStatus: '',
   
    systemProfileDto: {
      systemProfilesId: ''
    }
  }
  topic: string = '';
  systemProfilesId: string = '';
  TimeSlot: string = '';
  StartDate: string = '';
  email1: string = '';
  timeSlotArrey : TimeSlots [] = [];

  constructor(private router: Router, private customerService: CustomerService, private zoomService: ZoomService, private route: ActivatedRoute,private timeSlotService : TimeSlotServiceService
    ,private loadingService: LoadingService
  ) { 
    this.getAllTimeSlots("ACTIVE");
  }



  onSubmit(): void {
    // Validate required fields


    function containsAtSymbol(input: string): boolean {
      return input.includes('@');
    }

    // Validate password confirmation


   
    const x = containsAtSymbol(this.email1);
if(this.email1 != ''){
    if (x === true) {
      this.customer.customerEmail = this.email1;
      console.log(this.customer);

      console.log(this.customer);
      const systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');

      if(this.TimeSlot != ''){
      if (systemProfilesId) {
        this.customer.systemProfileDto = this.customer.systemProfileDto || {}
        this.customer.systemProfileDto.systemProfilesId = systemProfilesId;
              let startDate = this.StartDate;
              let topic = this.topic;
              let timeSlotId = this.TimeSlot;
              // this.loadingService.show(); 

              this.zoomService.pendingScheduleMeeting(timeSlotId, systemProfilesId, startDate,topic,String(this.customer.customerNumber),String(this.customer.customerAddress),String(this.customer.customerEmail),String(this.customer.customerName),String(this.customer.customerType),String(this.customer.companyRegNo)).subscribe(
                (response) => {
                  if (response.status == true) {
                    // this.loadingService.hide(); 
                    Swal.fire('', response.commonMessage, 'success')
                  } else {
                    // this.loadingService.hide(); 
                    if(response.errorMessages.length){
                    for (let i = 0; i < response.errorMessages.length; i++) {
                      Swal.fire('', response.errorMessages[i], 'info');

                    }
                  }else{
                    this.loadingService.hide(); // Hide loader after success
                    Swal.fire('', response.commonMessage, 'info');

                  }
                  }
                }

              
        )
      } else {
        if (!systemProfilesId) console.warn('systemProfilesId is missing from the route.');

      }

    }else{
      this.loadingService.hide(); // Hide loader after success
      Swal.fire('', 'You are not selected in TimeSlot', 'info');

    }
    } else {
      this.loadingService.hide(); // Hide loader after success
      Swal.fire('', 'Invalid Email', 'error');

    }


  }else{
    this.loadingService.hide(); // Hide loader after success
    Swal.fire('', 'Email field is Empty', 'error');
  }


  }

getAllTimeSlots(commonStatus : any){
  this.timeSlotService.getAllTimeSlots(commonStatus).subscribe(
    (response)=>{
      console.log(response.payload)
      this.timeSlotArrey = response.payload[0];
    }
  )
}

}