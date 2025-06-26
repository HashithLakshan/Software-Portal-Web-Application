import { NumberFormatStyle } from '@angular/common';
import { Component, inject, TemplateRef } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { LoadingService } from 'src/app/services/loading.service';
import { SystemIssueMessagesService } from 'src/app/services/system-issue-messages.service';
import { SystemIssuesAnswerMessageService } from 'src/app/services/system-issues-answer-message.service';
import { SystemIssuesAnswerMessages } from 'src/app/shared/systemIssueMessageAnswer';
import { SystemMessageIssues } from 'src/app/shared/systemMessage';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-super-admin-system-issues',
  templateUrl: './super-admin-system-issues.component.html',
  styleUrls: ['./super-admin-system-issues.component.css']
})
export class SuperAdminSystemIssuesComponent {
  [x: string]: any;
  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 5;

  collectionSize1: number = 0;
  page1: number = 1;
  pageSize1: number = 5;

  collectionSize2: number = 0;
  page2: number = 1;
  pageSize2: number = 5;

  fromDate: string = '';
  ToDate: string = '';
  searchInput: string = '';
  DropDownValue: string = '';
  subject: string = '';
  body: string = '';
  private modalRef!: NgbModalRef; // Store reference to modal



  messageYes: SystemMessageIssues[] = [];
  messageNo: SystemMessageIssues[] = [];
  messageDelete: SystemMessageIssues[] = [];

  messageShow: SystemMessageIssues = {

    issueId: '',
    subject: '',
    body: '',
    receivedDate: '',
    receivedTime: '',
    commonStatus: '',
    replyMessageStatus: '',
    zoomDto: {
      perchaseId: '',
      customerEmail: '',
      systemProfileDto: {
        systemProfilesId: '',
        systemProfilesName: '',


      }
    }
  }

  answerMessage: SystemIssuesAnswerMessages = {
    answerId: '',
    answerSubject: '',
    answerBody: '',
    sendDate: '',
    sendTime: '',
    commonStatus: '',
    systemIssueMessagesDto: {
      issueId: '',
      subject: '',
      body: '',
      receivedDate: '',
      receivedTime: '',
      commonStatus: '',
      replyMessageStatus: '',
      zoomDto: {
        perchaseId: '',
        customerEmail: ''
      }

    }
  }
  answer: SystemIssuesAnswerMessages[] = []


  private modalService = inject(NgbModal);

  constructor(private messageService: MessageService, private issueMessageService: SystemIssueMessagesService, private messageAnswerService: SystemIssuesAnswerMessageService,private loadingService : LoadingService) {
    this.getAllFilterMessagesStatusYes('ACTIVE', 'YES');
    this.getAllFilterMessagesStatusNo('ACTIVE', 'NO');
    this.getAllFilterMessagesStatusInactive('INACTIVE', '');


  }


  openSecondModal(content2: any) {
    this.modalService.open(content2, { size: 'md' }).result.then(() => { }, () => { });
  }

  openLg(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }

  onPageChange(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page = page;
      this.getAllFilterMessagesStatusYes('ACTIVE', 'YES');
    } else {
      this.page = page;
      this.dateFilterYes('ACTIVE', 'YES');
    }
  }
  onPageChange1(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page1 = page;
      this.getAllFilterMessagesStatusNo('ACTIVE', 'NO');
    } else {
      this.page1 = page;
      this.dateFilterNo('ACTIVE', 'NO');
    }
  }
  onPageChange2(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page2 = page;
      this.getAllFilterMessagesStatusInactive('INACTIVE', '');
    } else {
      this.page2 = page;
      this.dateFilterdelete('INCTIVE', '');
    }
  }


  getMessageAnswerId(issueId: any) {
    console.log(issueId)
    this.messageAnswerService.getByAnswers(issueId).subscribe(
      (response) => {
        console.log(response)
        this.answerMessage = response.payload[0];
        console.log(this.answerMessage)

      }
    )
  }


  getAllFilterMessagesStatusYes(commonStatus: "ACTIVE", replyMessageStatus: "YES") {
    this.issueMessageService.getAllMessages(commonStatus, replyMessageStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.messageYes = response.payload[0];
        console.log(response.payload)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching message:", error);
      }
    );


  }

  getAllFilterMessagesStatusNo(commonStatus: "ACTIVE", replyMessageStatus: "NO") {
    this.issueMessageService.getAllMessages(commonStatus, replyMessageStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.messageNo = response.payload[0];
        console.log(response.payload)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize1, "Page Size:", this.pageSize1);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching message:", error);
      }
    );


  }

  getAllFilterMessagesStatusInactive(commonStatus: "INACTIVE", replyMessageStatus: "") {
    this.issueMessageService.getAllMessages(commonStatus, replyMessageStatus, this.page2 - 1, this.pageSize2).subscribe(
      (response) => {
        this.messageDelete = response.payload[0];
        console.log(response.payload)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize2 = pagination.totalItems; // Total number of items
          this.pageSize2 = 5; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize2, "Page Size:", this.pageSize2);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching message:", error);
      }
    );


  }

  dateFilterYes(commonStatus: any, replyMessageStatus: any) {
    this.issueMessageService.getMessageFilterDate(this.fromDate, this.ToDate, commonStatus, replyMessageStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {

        if (response.status == true) {
          this.messageYes = response.payload[0];

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize = pagination.totalItems; // Total number of items
            this.pageSize = 5; // Number of items per page (should match backend size)

            console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }

      },
      (error) => {
        console.error("Error fetching message:", error);
      }
    )
  }

  dateFilterNo(commonStatus: any, replyMessageStatus: any) {
    this.issueMessageService.getMessageFilterDate(this.fromDate, this.ToDate, commonStatus, replyMessageStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {

        if (response.status == true) {
          this.messageNo = response.payload[0];

          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];

            this.collectionSize1 = pagination.totalItems; // Total number of items
            this.pageSize1 = 5; // Number of items per page (should match backend size)

            console.log("Total Items:", this.collectionSize1, "Page Size:", this.pageSize1);
          } else {
            console.warn("Pagination details not found in response");
          }
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }

      },
      (error) => {
        console.error("Error fetching message:", error);
      }
    )
  }

  dateFilterdelete(commonStatus: any, replyMessageStatus: any) {
    this.issueMessageService.getMessageFilterDate(this.fromDate, this.ToDate, commonStatus, replyMessageStatus, this.page2 - 1, this.pageSize2).subscribe(
      (response) => {

        if (response.status == true) {
          this.messageDelete = response.payload[0];

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
        console.error("Error fetching messages:", error);
      }
    )
  }

  updateStatus(status: any, issueId: any) {
    if (status == 'YES') {
      this.loadingService.show();
      this.issueMessageService.updateStatus(status, issueId, this.subject, this.body).subscribe(
        (response) => {
          if (response.status == true) {
            this.loadingService.hide();
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterMessagesStatusYes('ACTIVE', 'YES');
            this.getAllFilterMessagesStatusNo('ACTIVE', 'NO');
            this.getAllFilterMessagesStatusInactive('INACTIVE', '');
            this.modalRef.close(); // ✅ Close modal after getting data

          } else {
            if(response.errorMessages){
              this.loadingService.hide();
              for(let i = 0;i<response.errorMessages.length;i++){
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[i] });
              }
            }else{
              this.loadingService.hide();
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
            }
           

          }
        }
      );

    }

    else {
      this.issueMessageService.updateStatus(status, issueId, '', '').subscribe(
        (response) => {
          if (response.status == true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterMessagesStatusYes('ACTIVE', 'YES');
            this.getAllFilterMessagesStatusNo('ACTIVE', 'NO');
            this.getAllFilterMessagesStatusInactive('INACTIVE', '');
            this.modalRef.close(); // ✅ Close modal after getting data

          } else {
            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

          }
        }
      );
    }
  }


  confirmMessages(status: any, issueId: any) {
    console.log(status)
    console.log(issueId)
    if (status == 'YES') {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to send this message',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: 'btn-success ',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I send it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.updateStatus(status, issueId);
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Message not send" });


        }
      });
    }

    else if (status == 'DELETE') {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want delete  this Message',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#DC3545',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I want it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.updateStatus(status, issueId);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "this messge not deleted" });


        }
      });

    } else if (status == 'ACTIVE') {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want Recover this message',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: 'btn-success ',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I want it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.updateStatus(status, issueId);
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "Message not send" });


        }
      });
    }
    else {
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want delete permenet this record',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#FFC107',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I want it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {

          this.updateStatus(status, issueId);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Swal.fire(
          //   'Cancelled',
          //   'Your employee record is safe',
          //   'info'
          // );
          this.messageService.add({ severity: 'info', summary: 'info', detail: "this message not deleted perment" });


        }
      });
    }
  }


  openXl(content: TemplateRef<any>) {
    this.modalRef = this.modalService.open(content, { size: 'xl' });
  }

  onViewBtn(messageData: SystemMessageIssues, index: number) {
    this.messageShow = messageData;
  }
  checkIfNumber(input: string): boolean {
    return /^\d+$/.test(input); // Returns true if input contains only digits (0-9)
  }
  search(status: any) {
    let x = this.checkIfNumber(this.searchInput);

    if(this.searchInput === null || this.searchInput === ''){
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Search fieeld are empty' });

    }else{
    // if (x != false) {
      switch (this.DropDownValue) {
        case 'perchaseId':
              if (x != false) {

          this.issueMessageService.getMessageFilterPerchaseId(this.searchInput,status ).subscribe(
            (response) => {
              if(response.status == true){
              this.messageShow = response.payload[0]
              this.getMessageAnswerId(this.messageShow.issueId);
                  this.messageService.add({ severity: 'success', summary: 'success', detail: 'Successfully filtered' });

    
              }else{
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

              }
            }
          )
              }else{
      this.messageService.add({ severity: 'info', summary: 'info', detail: "Provide number only" });

    }
          break;
          case 'issueId':
            if (x != false) {

            this.issueMessageService.getMessageFilterIssueId(this.searchInput, status).subscribe(
              (response) => {
                if(response.status == true){
                this.messageShow = response.payload[0]
                this.getMessageAnswerId(this.messageShow.issueId);
                }else{
                  this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
  
                }
              }
            )
          }else{
            this.messageService.add({ severity: 'info', summary: 'info', detail: "Provide number only" });
      
          }
            break;
            default:
              this.messageService.add({ severity: 'info', summary: 'info', detail: "You are not select Serach type" });

      }
    // }else{
    //   this.messageService.add({ severity: 'info', summary: 'info', detail: "Provide number only" });

    // }
    }
  }

  clearData(){
    this.DropDownValue = '';
    this.searchInput = '';
    this.ToDate = '';
    this.fromDate = '';
  }
  onTabClick(event: any) {
    this.clearData();
    this.getAllFilterMessagesStatusYes('ACTIVE', 'YES');
    this.getAllFilterMessagesStatusNo('ACTIVE', 'NO');
    this.getAllFilterMessagesStatusInactive('INACTIVE', '');
    this.subject = '';
    this.body = '';
   }
}

