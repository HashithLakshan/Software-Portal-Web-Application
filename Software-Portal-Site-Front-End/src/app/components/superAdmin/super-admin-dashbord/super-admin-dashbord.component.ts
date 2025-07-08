import { Component, inject, OnInit, TemplateRef } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { PdfService } from 'src/app/services/pdf.service';
import { ReminderService } from 'src/app/services/reminder.service';
import { systemprofiles } from 'src/app/services/system-profiles.service';
import { ZoomService } from 'src/app/services/zoom.service';
import { PayemntResit } from 'src/app/shared/payementResit';
import { SystemProfile } from 'src/app/shared/systemProfile';
import { Zoom } from 'src/app/shared/zoom';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-super-admin-dashbord',
  templateUrl: './super-admin-dashbord.component.html',
  styleUrls: ['./super-admin-dashbord.component.css']
})
export class SuperAdminDashbordComponent implements OnInit{
  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 5;

  collectionSize1: number = 0;
  page1: number = 1;
  pageSize1: number = 5;

  collectionSize2: number = 0;
  page2: number = 1;
  pageSize2: number = 5;

  collectionSize3: number = 0;
  page3: number = 1;
  pageSize3: number = 5;

  private modalRef!: NgbModalRef; // Store reference to modal



  countOfSystems: number = 0;
  countOfPendingMeetings: number = 0;
  countOfPaymentResit: number = 0;
  countOfZoomToday: number = 0;
  systemProfileApprove: SystemProfile[] = [];
  zoomPending: Zoom[] = [];
  payementResitApprove: PayemntResit[] = [];
  zoomToday: Zoom[] = [];


  zoomDataShow: Zoom = {
    perchaseId: '',
    sheduleTopic: '',
    startDate: '',
    startTime: '',
    zoomLink: '',
    meetingPassword: '',
    meeting_Duration: '',
    requestStatus: '',
    commonStatus: '',
    customerAddress: '',
    customerNumber: '',
    customerEmail: '',
    customerName: '',
    customerType: '',
    companyRegNo: '',
    zmeetingId: '',
    systemProfileDto: {
      systemProfilesId: '',
      systemProfilesName: '',
      systemProfilesDiscription: '',
      commonStatus: '',
      requestStatus: '',
      systemProfilesPrice: '',
      employeeDto: {
        employeeId: '',
      }
    },
    zoomTimeSlotsDto: {
      zoomTimeSlotId: '',
      slotOpenTime: '',
      slotCloseTime: '',
      commonStatus: '',
    }

  }
  payementResitshow: PayemntResit = {
    id:'',
    saveDate:'',
    requestStatus:'',
commonStatus:'',
systemProfileDto:{
    systemProfilesId:'',
    systemProfilesName:'',
    systemProfilesDiscription:'',
    commonStatus:'',
    requestStatus:'',
    systemProfilesPrice:'',
},
zoomDto:{
    perchaseId:'',
    sheduleTopic:'',
    startDate:'',
    startTime:'',
    zoomLink:'',
    meetingPassword:'',
    meeting_Duration:'',
    requestStatus:'',
    customerAddress:'',
    customerNumber:'',
    customerName:'',
    customerType:'',
    companyRegNo:'',
    zmeetingId:'',
}
  }

  private modalService = inject(NgbModal);

  constructor(private reminderService :ReminderService,private systemProfileService: systemprofiles, private messageService: MessageService, private zoomService: ZoomService, private pdfService: PdfService) {
    this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
    this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
    this.getCountsZoomTodat("today","APPROVED","ACTIVE");
    this.getCountsZoomPendingCount("pending","PENDING","ACTIVE");
    this.getCountsPaymentPendingCount("payment","PENDING","ACTIVE");
    this.getAllSystemCount("APPROVED", "ACTIVE");
    this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
    this.getAllFilterZoomToday("APPROVED","ACTIVE");


  }

  ngOnInit(): void {
    this.reminderService.startReminder();
    console.log( this.reminderService.startReminder())
  }

  openLg(content: TemplateRef<any>) {
    this.modalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }
  onPageChange(page: number) {

    this.page2 = page;
    this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
    this.getCountsPaymentPendingCount("payment","PENDING","ACTIVE");

  }
  onPageChange1(page: number) {

    this.page1 = page;
    this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
    this.getCountsZoomPendingCount("pending","PENDING","ACTIVE");

  }
  onPageChange2(page: number) {

    this.page = page;
    this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");

  }
  onPageChange3(page: number) {

    this.page3 = page;
    this.getAllFilterZoomToday("APPROVED","ACTIVE");
    this.getCountsZoomTodat("today","APPROVED","ACTIVE");

  }
  onViewBtn(zoomData: Zoom, index: number) {
    this.zoomDataShow = zoomData;
    console.log(this.zoomDataShow)
  }

  onViewBtn2(zoomData: Zoom, index: number) {
    this.zoomDataShow = zoomData;
    console.log(this.zoomDataShow)
  }

     onViewBtn3(resitData: PayemntResit, index: number) {
         this.payementResitshow = resitData;
         console.log(this.payementResitshow)
      }

      

  getCountsZoomTodat(action: any,commonStatus : any,requestStatus : any) {
    this.zoomService.getDashboard(action,commonStatus,requestStatus).subscribe(
      (response) => {
        this.countOfZoomToday = response.payload[0].length;
      }
    )
  }

  getCountsZoomPendingCount(action: any,commonStatus : any,requestStatus : any) {
    this.zoomService.getDashboard(action,commonStatus,requestStatus).subscribe(
      (response) => {
        this.countOfPendingMeetings = response.payload[0].length;
      }
    )
  }

  getCountsPaymentPendingCount(action: any,commonStatus : any,requestStatus : any) {
    this.zoomService.getDashboard(action,commonStatus,requestStatus).subscribe(
      (response) => {
        this.countOfPaymentResit = response.payload[0].length;
      }
    )
  }

  getAllSystemCount(requestStatus: any, commonStatus: any) {
    this.systemProfileService.getAllSystemProfies(requestStatus, commonStatus).subscribe(
      (response) => {
        this.countOfSystems = response.payload[0].length;
      }
    )
  }


  getAllFilterZoomStatusPending(commonStatus: "ACTIVE", requestStatus: "PENDING") {
    this.zoomService.getZoomFilterStatus(commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.zoomPending = response.payload[0];
        console.log(response.payload.length)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 5; // Number of items per page (should match backend size)

        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterZoomToday(requestStatus :any,commonStatus : any) {
    this.zoomService.getZoomFilterToday( requestStatus,commonStatus,this.page3 - 1, this.pageSize3).subscribe(
      (response) => {
        this.zoomToday = response.payload[0];
        console.log(response.payload.length)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize3 = pagination.totalItems; // Total number of items
          this.pageSize3 = 5; // Number of items per page (should match backend size)

        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterPayementResitStatusApproved(commonStatus: "ACTIVE", requestStatus: "PENDING") {
    this.pdfService.getZoomFilterStatus(commonStatus, requestStatus, this.page2 - 1, this.pageSize2).subscribe(
      (response) => {
        this.payementResitApprove = response.payload[0];
        console.log(response.payload)
        // this.countOfPaymentResit = response.payload.length;
        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize2 = pagination.totalItems; // Total number of items
          this.pageSize2 = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterSystemProfileApprove(commonStatus: String, requestStatus: String) {
    this.systemProfileService.getSystemProfileFilterStatus(String(commonStatus), String(requestStatus), this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.systemProfileApprove = response.payload[0];
        console.log(this.systemProfileApprove)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 10; // Number of items per page (should match backend size)

        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );
    
  }

messagePopUp(changeStatus : any,perchaseId : any,zoomLink : any){
 if (changeStatus === "Rejected") {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to Reject this Pending meeting',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#FFC107',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, reject it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.deleteMeetingToday(changeStatus, perchaseId);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Your employee record is safe" });


        }
      });
    } 
    if(changeStatus === "today") {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to Reject this Today meeting',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#FFC107',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, reject it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          changeStatus = 'Rejected'
          this.deleteMeetingToday(changeStatus, perchaseId);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Your employee record is safe" });


        }
      });
    }
    if (changeStatus === "join") {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to Login this meeting',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: 'btn-primary',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, want it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.joinMeeting(zoomLink);
           
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Your employee record is safe" });


        }
      });
    } 
}


  deleteMeetingToday(changeStatus : any,perchaseId : any){
this.zoomService.changeStatus(changeStatus,perchaseId).subscribe(
  (response) => {
    if (response.status == true) {
      this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
      this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
      this.getAllFilterZoomToday("APPROVED","ACTIVE");

      this.getCountsZoomPendingCount("pending","PENDING","ACTIVE");
      this.getCountsZoomTodat("today","APPROVED","ACTIVE");
      this.modalRef.close(); 

    } else {
      this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

    }

  }
)
  }

  joinMeeting(zoomLink: any) {
    window.open(zoomLink, '_blank');
  }

  downloadFile(id: any) {
    this.pdfService.downloadPdf(id).subscribe(response => {
      const blob = new Blob([response.body!], { type: 'application/pdf' });

      // Extract filename from Content-Disposition header
      const contentDisposition = response.headers.get('Content-Disposition');
      let filename = 'download.pdf'; // Default name
      if (contentDisposition) {
        const matches = contentDisposition.match(/filename="(.+)"/);
        if (matches && matches.length > 1) {
          filename = matches[1];
        }
      }

      // Create an anchor element to trigger download
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = filename;
      link.click();

      // Cleanup
      window.URL.revokeObjectURL(link.href);
    });
  }
  
 confirmComplete(id: any, actionDo: any){
    switch(actionDo){

      case  'Inactive':
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to delete this record',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: ' #DC3545',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I delete it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          
          this.updateStatus(id, actionDo );
         
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Not move to Complete table" });
  
  
        }
      });
    break;
    case  'Completed':
      Swal.fire({
        title: 'Are you sure?',
        text: 'You Complete business',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#FFC107',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I complete it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          
          this.updateStatus(id, actionDo );
         
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Not move to Complete table" });
  
  
        }
      });
    break;
    }
  }

  updateStatus(id: any, actionDo: string) {
    console.log(id)
    this.pdfService.getPaymentReciptUpdateStatus(id, actionDo).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
          this.getCountsPaymentPendingCount("payment","PENDING","ACTIVE");

          this.modalRef.close(); 

        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    )

  }
}
