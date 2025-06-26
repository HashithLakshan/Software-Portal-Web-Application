import { Component, OnInit, TemplateRef, inject } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { LoadingService } from 'src/app/services/loading.service';
import { TimeSlotServiceService } from 'src/app/services/time-slot-service.service';
import { ZoomService } from 'src/app/services/zoom.service';
import { TimeSlots } from 'src/app/shared/timeSlots';
import { Zoom } from 'src/app/shared/zoom';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-super-admin-customer',
  templateUrl: './super-admin-customer.component.html',
  styleUrls: ['./super-admin-customer.component.css']
})
export class SuperAdminCustomerComponent implements OnInit {
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


  fromDate: string = '';
  ToDate: string = '';


  topic: string = '';
  systemProfilesId: string = '';
  customerId2: string = '';
  duration: string = '';
  startTime: string = '';
  systemProfilesId2: string = '';
  customerNumber: string = '';
  customerAddress: string = '';
  customerEmail: string = '';
  customerName: string = '';
  customerType: string = '';
  companyRegNo: string = '';

  TimeSlotId: string = '';
  topicUpdate: string = '';
  durationUpdate: string = '';
  startTimeUpdate: string = '';
  systemProfilesId2Update: string = '';
  customerNumberUpdate: string = '';
  customerAddressUpdate: string = '';
  customerEmailUpdate: string = '';
  customerNameUpdate: string = '';
  customerTypeUpdate: string = '';
  companyRegNoUpdate: string = '';
  zoomPending: Zoom[] = [];
  zoomApprove: Zoom[] = [];
  zoomInActive: Zoom[] = [];
  zoomCompleted: Zoom[] = [];
  // datePending: Zoom[] = [];
  // dateApprove: Zoom[] = [];
  // dateInActive: Zoom[] = [];
  // dateCompleted: Zoom[] = [];
  timeSlotArrey: TimeSlots[] = [];

  DropDownValue: string = '';
  searchInput: string = '';

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
  startTime2: string = '';
  duration2: string = '';



  private modalRef!: NgbModalRef; // Store reference to modal

  today: string = new Date().toISOString().split('T')[0]; // Get today's date in 'YYYY-MM-DD' format



  private modalService = inject(NgbModal);

  constructor(private zoomService: ZoomService, private messageService: MessageService, private timeSlotService: TimeSlotServiceService,private loadingService : LoadingService) {
    this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
    this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
    this.getAllFilterZoomStatusInactive("INACTIVE", "");
    this.getAllTimeSlots("ACTIVE");
    this.getAllFilterZoomStatusCompleted("ACTIVE", "COMPLETED");
    // this.reminder();


  }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
  onPageChange(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page = page;
      this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
    } else {
      this.page = page;
      this.dateFilterApprove("ACTIVE", "APPROVED");
    }
  }

  onPageChange2(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page1 = page;
      this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
    } else {
      this.page1 = page;
      this.dateFilterPending("ACTIVE", "PENDING");
    }
  }


  onPageChange3(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page2 = page;
      this.getAllFilterZoomStatusInactive("INACTIVE", "");
    } else {
      this.page2 = page;
      this.dateFilterInactive("INACTIVE", "");
    }
  }
  onPageChange4(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page3 = page;
      this.getAllFilterZoomStatusCompleted("ACTIVE", "COMPLETED");
    } else {
      this.page3 = page;
      this.dateFilterComplete("ACTIVE", "COMPLETED");
    }
  }

  openLg(content: TemplateRef<any>) {
    this.modalRef = this.modalService.open(content, { size: 'lg' });
  }


  joinMeeting(zoomLink: any) {
    window.open(zoomLink, '_blank');
  }
  
  isTodaColurRow(startDate: any): boolean {
    if (!startDate) return false;
    
    const today = new Date().toISOString().split('T')[0];
    return startDate.split('T')[0] === today;
  }
  

  // reminder() {
  //   this.zoomService.reminderMeetings().subscribe(
  //     (response) => {

  //       if (response.status == true) {
  //         this.messageService.add({
  //           severity: 'warning',
  //           summary: 'Reminder Meeting',
  //           detail: response.commonMessage,
  //           life: 0,
  //           sticky: true
  //         });
  //       }
  //     }
  //   )
  // }

  getAllFilterZoomStatusCompleted(commonStatus: "ACTIVE", requestStatus: "COMPLETED") {
    this.zoomService.getZoomFilterStatus(commonStatus, requestStatus, this.page3 - 1, this.pageSize3).subscribe(
      (response) => {
        this.zoomCompleted = response.payload[0];
        console.log(this.zoomApprove)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize3 = pagination.totalItems; // Total number of items
          this.pageSize3 = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize3, "Page Size:", this.pageSize3);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterZoomStatusApprove(commonStatus: "ACTIVE", requestStatus: "APPROVED") {
    this.zoomService.getZoomFilterStatus(commonStatus, requestStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.zoomApprove = response.payload[0];
        console.log(this.zoomApprove);
        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize3, "Page Size:", this.pageSize3);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching meetings:", error);
      }
    );
  }

  // Method to check if the meeting is scheduled for today
  isToday(meetingDate: any): boolean {
    if (!meetingDate) return false;
    return meetingDate.split('T')[0] === this.today;
  }

  getAllFilterZoomStatusPending(commonStatus: "ACTIVE", requestStatus: "PENDING") {
    this.zoomService.getZoomFilterStatus(commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.zoomPending = response.payload[0];

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

  getAllFilterZoomStatusInactive(commonStatus: "INACTIVE", requestStatus: "") {
    this.zoomService.getZoomFilterStatus(commonStatus, requestStatus, this.page2 - 1, this.pageSize2).subscribe(
      (response) => {
        this.zoomInActive = response.payload[0];

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize2 = pagination.totalItems; // Total number of items
          this.pageSize2 = 5; // Number of items per page (should match backend size)

        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );


  }

  SheduleMeeting(perchaseId: any) {

    function containsAtSymbol(input: string): boolean {
      return input.includes('@');
    }

    let x = containsAtSymbol(this.customerEmail);

    let startTime = this.startTime;
    let topic = this.topic;
    let duration = this.duration;
    let systemProfilesId = this.systemProfilesId2;
    let customerAddress = this.customerAddress;
    let customerName = this.customerName;
    let customerType = this.customerType;
    let companyRegNo = this.companyRegNo;
    let customerNumber = this.customerNumber;
    let zoomTimeSlotId = this.TimeSlotId;
    if (x === true) {
      let customerEmail = this.customerEmail;
      this.loadingService.show();
      this.zoomService.scheduleMeeting(perchaseId, zoomTimeSlotId, topic, startTime, duration, systemProfilesId, 'Super Admin', customerNumber, customerAddress, customerEmail, customerName, customerType, companyRegNo).subscribe(
        (response) => {
          if (response.status == true) {
            this.loadingService.hide();

            console.log(response)
            Swal.fire('', response.commonMessage, 'success')
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
            this.modalRef.close(); // ✅ Close modal after getting data
            this.clearData;
          } else {
            this.loadingService.hide();

            if (response.errorMessages.length) {
              for (let i = 0; i < response.errorMessages.length; i++) {
                // Swal.fire('', response.errorMessages[i], 'info');
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[i] });

              }
            } else {
              this.loadingService.hide();

              // Swal.fire('', response.commonMessage, 'info');

              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
          }
        }
      );
    } else {
      this.messageService.add({ severity: 'info', summary: 'info', detail: "This email is not valid" });
    }
  }


  clearData() {
    console.log('hello');
    this.startTime = '';
    this.topic = '';
    this.duration = '';
    this.systemProfilesId2 = '';
    this.customerAddress = '';
    this.customerEmail = '';
    this.customerName = '';
    this.customerType = '';
    this.companyRegNo = '';
    this.customerNumber = '';
    this.DropDownValue = '';
    this.searchInput = '';
    this.fromDate = '';
    this.ToDate = '';
    this.page = 1;
    this.page1 = 1;
    this.page2 = 1;
    this.page3 = 1;


  }

  confirmDialogDeleteDatabase(changeStatus: any, perchaseId: any, meetingId: any, rollName: any) {

    Swal.fire({
      title: 'Are you sure?',
      text: 'You want to permenet delete this record',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {

        this.approveAndDelete(changeStatus, perchaseId, meetingId, rollName);

      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Swal.fire(
        //   'Cancelled',
        //   'Your employee record is safe',
        //   'info'
        // );
        this.messageService.add({ severity: 'info', summary: 'info', detail: "Your Meeting record is safe" });


      }
    });

  }

  confirmApprove(changeStatus: any, perchaseId: any, meetingId: any, rollName: any) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You want to Approve this meeting',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#28A745',
      cancelButtonColor: 'btn-secondary',
      confirmButtonText: 'Yes, approve it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {

        this.approveAndDelete(changeStatus, perchaseId, meetingId, rollName);

      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Swal.fire(
        //   'Cancelled',
        //   'Your employee record is safe',
        //   'info'
        // );
        this.messageService.add({ severity: 'info', summary: 'info', detail: "You not Approve this Meeting Request" });


      }
    });

  }

  confirmComplete(changeStatus: any, perchaseId: any, meetingId: any, rollName: any) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You Complete meeting',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#28A745',
      cancelButtonColor: 'btn-secondary',
      confirmButtonText: 'Yes, I complete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {

        this.approveAndDelete(changeStatus, perchaseId, meetingId, rollName);

      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Swal.fire(
        //   'Cancelled',
        //   'Your employee record is safe',
        //   'info'
        // );
        this.messageService.add({ severity: 'info', summary: 'info', detail: "Not move to Complete table" });


      }
    });

  }


  confirmRejected(changeStatus: any, perchaseId: any, meetingId: any, rollName: any) {
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

          this.approveAndDelete(changeStatus, perchaseId, meetingId, rollName);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Your employee record is safe" });


        }
      });
    } else {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to Reject this Approve meeting',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#FFC107',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, reject it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          changeStatus = "RejectedApprove"
          this.approveAndDelete(changeStatus, perchaseId, meetingId, rollName);

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





  approveAndDelete(changeStatus: any, perchaseId: any, meetingId: any, rollName: any) {
    if (changeStatus == "Approved") {

      const dateObj = new Date(this.startTime2);

      // Extract Date (YYYY-MM-DD)
      const datePart = dateObj.toISOString().split("T")[0];
      if (datePart == this.zoomDataShow.startDate) {
        this.zoomDataShow.startTime = this.startTime2;
        this.zoomDataShow.meeting_Duration = this.duration2;
        this.loadingService.show();
        this.zoomService.scheduleMeetingApproving(perchaseId, String(this.zoomDataShow.zoomTimeSlotsDto?.zoomTimeSlotId), String(this.zoomDataShow.sheduleTopic), String(this.zoomDataShow.startTime), String(this.zoomDataShow.meeting_Duration), String(this.zoomDataShow.systemProfileDto?.systemProfilesId), rollName, String(this.zoomDataShow.customerNumber), String(this.zoomDataShow.customerAddress), String(this.zoomDataShow.customerEmail), String(this.zoomDataShow.customerName), String(this.zoomDataShow.customerType), String(this.zoomDataShow.companyRegNo)).subscribe(
          (response) => {
            if (response.status == true) {
              this.loadingService.hide();

              this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
              this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
              this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
              this.modalRef.close(); // ✅ Close modal after getting data

            } else {
              this.loadingService.hide();

              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }

          }
        );
      } else {
        this.messageService.add({ severity: 'info', summary: 'info', detail: "Miss Match Dates" });
        this.startTime2 = '';

      }
    }

    if (changeStatus == "Completed") {
      this.zoomService.changeStatus(changeStatus, perchaseId).subscribe(
        (response) => {
          console.log("hello");

          console.log(response);

          if (response.status === true) {
            this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
            this.getAllFilterZoomStatusCompleted("ACTIVE", "COMPLETED");
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.modalRef.close(); // ✅ Close modal after getting data

          }
        }
      )
    }


    if (changeStatus == "Rejected") {
      this.zoomService.changeStatus(changeStatus, perchaseId).subscribe(
        (response) => {
          console.log(response);

          if (response.status === true) {
            this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
            this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
            this.getAllFilterZoomStatusInactive("INACTIVE", "");
            this.getAllFilterZoomStatusCompleted("ACTIVE", "COMPLETED");
            this.modalRef.close(); // ✅ Close modal after getting data

            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });

          }
        }
      )
    }


    if (changeStatus == "RejectedApprove") {
      changeStatus = "Rejected";
      this.zoomService.changeStatus(changeStatus, perchaseId).subscribe(
        (response) => {
          console.log(response);

          if (response.status === true) {
            this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
            this.getAllFilterZoomStatusInactive("INACTIVE", "");
            this.modalRef.close(); // ✅ Close modal after getting data

            console.log(meetingId)
            this.loadingService.show();

            this.zoomService.deleteZoomMeeting(meetingId).subscribe(
              (response) => {
                if (response.status === true) {
                  this.loadingService.hide();

                  this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
                  this.modalRef.close(); // ✅ Close modal after getting data

                } else {
                  this.loadingService.hide();

                  this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
                }
              }
            );
          } else {
            this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
          }
        }
      );


    }

    if (changeStatus == "DeleteDatabase") {
      this.zoomService.changeStatus(changeStatus, perchaseId).subscribe(
        (response) => {
          console.log(response);

          if (response.status === true) {
            this.getAllFilterZoomStatusInactive("INACTIVE", "");
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.modalRef.close(); // ✅ Close modal after getting data

          }
        }
      )

    }


  }

  onViewBtn(zoomData: Zoom, index: number) {
    this.zoomDataShow = zoomData;
    console.log(this.zoomDataShow)
  }

  openXl(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'xl' });
  }
  getAllTimeSlots(commonStatus: any) {
    this.timeSlotService.getAllTimeSlots(commonStatus).subscribe(
      (response) => {
        console.log(response.payload)
        this.timeSlotArrey = response.payload[0];
      }
    )
  }

  checkIfNumber(input: string): boolean {
    return /^\d+$/.test(input); // Returns true if input contains only digits (0-9)
  }

  search(commonStatus: any, requestStatus: any) {

    let x = this.checkIfNumber(String(this.searchInput));
    if(this.searchInput === null || this.searchInput === ''){
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Search fieeld are empty' });

    }else{
    // if (x === true) {

      switch (this.DropDownValue) {
        case "perchaseId":
          console.log(commonStatus)
          console.log(requestStatus)
          console.log(this.searchInput)
    if (x === true) {


          this.zoomService.getPerchaseIdSearch(this.searchInput, commonStatus, requestStatus).subscribe(
            (response) => {
              if (response.status == true) {
                if (commonStatus === "INACTIVE") {
                  this.zoomInActive = response.payload;
                } else {
                  if (commonStatus === "ACTIVE" && requestStatus === "APPROVED") {
                    this.zoomApprove = response.payload;
                  }
                  if (commonStatus === "ACTIVE" && requestStatus === "COMPLETED") {
                    this.zoomCompleted = response.payload;
                  }
                  if (commonStatus === "ACTIVE" && requestStatus === "PENDING") {
                    this.zoomPending = response.payload;
                  }
                }
              } else {
                this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
                // Swal.fire('',response.commonMessage,'info');
              }
            }
          );
            } else {
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

    }
          break;
        case "ZoomId":
          console.log(requestStatus)
          console.log(commonStatus)
          if (x === true) {

          this.zoomService.getZoomIdSearch(this.searchInput, commonStatus, requestStatus).subscribe(
            (response) => {
              if (response.status == true) {
                if (commonStatus === "INACTIVE") {
                  this.zoomInActive = response.payload;
                } else {
                  if (commonStatus === "ACTIVE" && requestStatus === "APPROVED") {
                    this.zoomApprove = response.payload;
                    console.log(response.payload)
                  }
                  if (commonStatus === "ACTIVE" && requestStatus === "COMPLETED") {
                    this.zoomCompleted = response.payload;
                  }
                  if (commonStatus === "ACTIVE" && requestStatus === "PENDING") {
                    this.zoomPending = response.payload;
                  }
                }
              } else {
                this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
                // Swal.fire('',response.commonMessage,'info');
              }
            }
          );
             } else {
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

    }
          break;
        default:
          this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
        // Swal.fire ('','Select the search type','info')
      }
    // } else {
    //   this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

    // }
    }
  }

  onTabClick(event: any) {
    this.clearData();
    this.getAllFilterZoomStatusApprove("ACTIVE", "APPROVED");
    this.getAllFilterZoomStatusPending("ACTIVE", "PENDING");
    this.getAllFilterZoomStatusInactive("INACTIVE", "");
    this.getAllFilterZoomStatusCompleted("ACTIVE", "COMPLETED");
  }

  dateFilterApprove(commonStatus: string, requestStatus: string) {
    this.zoomService.getZoomFilterDate(this.fromDate, this.ToDate, commonStatus, requestStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        if (response.status == true) {
          this.zoomApprove = response.payload[0];
          console.log(this.zoomApprove)

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize = pagination.totalItems; // Total number of items
            this.pageSize = 5; // Number of items per page (should match backend size)
          this.messageService.add({ severity: 'success', summary: 'success', detail: "date filetrd" });

            console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }

      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    )
  }


  dateFilterPending(commonStatus: string, requestStatus: string) {
    this.zoomService.getZoomFilterDate(this.fromDate, this.ToDate, commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        if (response.status == true) {
          this.zoomPending = response.payload[0];
          console.log(this.zoomApprove)

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize1 = pagination.totalItems; // Total number of items
            this.pageSize1 = 5; // Number of items per page (should match backend size)

            console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }


      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    )


  }

  dateFilterInactive(commonStatus: string, requestStatus: string) {
    this.zoomService.getZoomFilterDate(this.fromDate, this.ToDate, commonStatus, requestStatus, this.page2 - 1, this.pageSize2).subscribe(
      (response) => {
        if (response.status == true) {
          this.zoomInActive = response.payload[0];
          // console.log('hy')

          // console.log(this.zoomInActive)

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize2 = pagination.totalItems; // Total number of items
            this.pageSize2 = 5; // Number of items per page (should match backend size)

            console.log("Total Items:", this.collectionSize2, "Page Size:", this.pageSize2);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }

      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    )
  }



  dateFilterComplete(commonStatus: string, requestStatus: string) {
    this.zoomService.getZoomFilterDate(this.fromDate, this.ToDate, commonStatus, requestStatus, this.page3 - 1, this.pageSize3).subscribe(
      (response) => {

        if (response.status == true) {
          this.zoomCompleted = response.payload[0];
          console.log(this.zoomApprove)

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize3 = pagination.totalItems; // Total number of items
            this.pageSize3 = 5; // Number of items per page (should match backend size)

            console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }

      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    )
  }


}

function moment() {
  throw new Error('Function not implemented.');
}
