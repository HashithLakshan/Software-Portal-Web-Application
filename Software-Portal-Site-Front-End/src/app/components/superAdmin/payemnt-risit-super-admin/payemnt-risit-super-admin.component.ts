import { Component, inject, TemplateRef } from '@angular/core';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { PdfService } from 'src/app/services/pdf.service';
import { PayemntResit } from 'src/app/shared/payementResit';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-payemnt-risit-super-admin',
  templateUrl: './payemnt-risit-super-admin.component.html',
  styleUrls: ['./payemnt-risit-super-admin.component.css']
})
export class PayemntRisitSuperAdminComponent {
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
  perchaseId: string = '';
  selectedFile: File | null = null;
  systemProfileId: string = '';
  searchInput: string = '';
  DropDownValue: string = '';

    private modalService = inject(NgbModal);

  payementResitApprove: PayemntResit[] = [];
  payementResitCompleted: PayemntResit[] = [];
  payementResitInactive: PayemntResit[] = [];
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


  constructor(private pdfService: PdfService, private messageService: MessageService,config: NgbModalConfig) {
    this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
    this.getAllFilterPayementResitStatusComplete("ACTIVE", "COMPLETED");
    this.getAllFilterPayementResitStatusInactive("INACTIVE", "");


  }

  onPageChange(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page = page;
      this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
    } else {
      this.page = page;
      this.dateFilterApprove("ACTIVE", "PENDING");
    }
  }

  onPageChange2(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page1 = page;
      this.getAllFilterPayementResitStatusComplete("ACTIVE", "COMPLETED");
    } else {
      this.page1 = page;
      this.dateFilterComplete("ACTIVE", "COMPLETED");
    }
  }

  onPageChange3(page: number) {
    if (this.fromDate == '' || this.ToDate == '') {
      this.page2 = page;
      this.getAllFilterPayementResitStatusInactive("INACTIVE", "");
    } else {
      this.page2 = page;
      this.dateFilterInactive("INACTIVE", "");
    }
  }
  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
  getAllFilterPayementResitStatusApproved(commonStatus: "ACTIVE", requestStatus: "PENDING") {
    this.pdfService.getZoomFilterStatus(commonStatus, requestStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.payementResitApprove = response.payload[0];
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
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterPayementResitStatusComplete(commonStatus: "ACTIVE", requestStatus: "COMPLETED") {
    this.pdfService.getZoomFilterStatus(commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.payementResitCompleted = response.payload[0];
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
        console.error("Error fetching employees:", error);
      }
    );


  }

  getAllFilterPayementResitStatusInactive(commonStatus: "INACTIVE", requestStatus: "") {
    this.pdfService.getZoomFilterStatus(commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.payementResitInactive = response.payload[0];
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
        console.error("Error fetching employees:", error);
      }
    );


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
  heckIfNumber(input: string): boolean {
    return /^\d+$/.test(input); // Returns true if input contains only digits (0-9)
  }
  onUploadPdf() {

    let x = this.heckIfNumber(this.perchaseId);
    let y = this.heckIfNumber(this.systemProfileId);



    if (!this.perchaseId) {
      this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please enter a perchase ID' });

      // Swal.fire('Warning', 'Please enter a Customer ID', 'warning');
      return;
    }
    if (!this.systemProfileId) {
      this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please enter a System Profile ID' });

      // Swal.fire('Warning', 'Please enter a Customer ID', 'warning');
      return;
    }
    if (x == false || y == false) {
      this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please use for numbers' });

      // Swal.fire('Warning', 'Please select a file', 'warning');
      return; // Stop execution if no file is selected
    }
    // Ensure selectedFile is not null
    if (!this.selectedFile) {
      this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please select a file' });

      // Swal.fire('Warning', 'Please select a file', 'warning');
      return; // Stop execution if no file is selected
    }
    console.log(this.perchaseId)
    this.pdfService.uploadPdf(this.selectedFile, this.systemProfileId, this.perchaseId, 'COMPLETED').subscribe(
      (response) => {
        if (response.status === true) {
          // Swal.fire('', response.commonMessage, "success");
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });

          this.selectedFile = null;
          this.perchaseId = '';
          const fileInput = document.getElementById("fileInput") as HTMLInputElement;
          if (fileInput) {
            fileInput.value = '';
          }

        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

          // Swal.fire('', response.commonMessage, "info");
        }
      },
      (error) => {
        this.messageService.add({ severity: 'Error', summary: 'Error', detail: 'File upload failed' });

        // Swal.fire('Error', 'File upload failed', 'error');
      }
    );
  }







  updateStatus(id: any, actionDo: string) {
    console.log(id)
    this.pdfService.getPaymentReciptUpdateStatus(id, actionDo).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
          this.getAllFilterPayementResitStatusComplete("ACTIVE", "COMPLETED");
          this.getAllFilterPayementResitStatusInactive("INACTIVE", "");
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    )

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
    case  'deleteDatabase':
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
    case  'Active':
      Swal.fire({
        title: 'Are you sure?',
        text: 'You want to recover this record',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#28A745',
        cancelButtonColor: 'btn-secondary',
        confirmButtonText: 'Yes, I want it !',
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

   checkIfNumber(input: string): boolean {
    return /^\d+$/.test(input); // Returns true if input contains only digits (0-9)
  }
   search(commonStatus : any, requestStatus : any){

    let x = this.checkIfNumber(String(this.searchInput));
    if(this.searchInput === null || this.searchInput === ''){
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Search fieeld are empty' });

    }else{
  // if(x === true){
  
        switch(this.DropDownValue){
      case "perchaseId" :
          if(x === true){

      console.log(commonStatus)
      console.log(requestStatus)
      console.log(this.searchInput)
  
  
        this.pdfService.getPerchaseIdSearch(commonStatus,requestStatus,this.searchInput).subscribe(
      (response)=>{
        if(response.status == true){
          if(commonStatus === "INACTIVE"){
            this.payementResitInactive = response.payload;
          }else{
            if(commonStatus === "ACTIVE" && requestStatus === "PENDING"){
            this.payementResitApprove = response.payload;
            }
            if(commonStatus === "ACTIVE" && requestStatus === "COMPLETED"){
              this.payementResitCompleted = response.payload;
            }
          
          }
        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
          // Swal.fire('',response.commonMessage,'info');
        }
      }
        );
          }else{
        this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });
  
      }
      break;
      case "id" :
        console.log(requestStatus)
        console.log(commonStatus)
        if(x === true){

        this.pdfService.getIdSearch(commonStatus,requestStatus,this.searchInput).subscribe(
          (response)=>{
            if(response.status == true){
              if(commonStatus === "INACTIVE"){
                this.payementResitInactive = response.payload;
              }else{
                if(commonStatus === "ACTIVE" && requestStatus === "PENDING"){
                this.payementResitApprove = response.payload;
                console.log( response.payload)
                }
                if(commonStatus === "ACTIVE" && requestStatus === "COMPLETED"){
                  this.payementResitCompleted = response.payload;
                          this.messageService.add({ severity: 'success', summary: 'success', detail: 'Payment Resit filtered' });

                }
              
              }
            }else{
              this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
              // Swal.fire('',response.commonMessage,'info');
            }
          }
            );
              }else{
        this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });
  
      }
        break;
        default:
          this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
      // Swal.fire ('','Select the search type','info')
        }
      // }else{
      //   this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });
  
      // }
    }
      }

      dateFilterApprove(commonStatus:string,requestStatus:string){
        this.pdfService.getZoomFilterDate(this.fromDate,this.ToDate,commonStatus,requestStatus ,this.page - 1, this.pageSize).subscribe(
          (response) => {
            if(response.status == true){
              this.payementResitApprove = response.payload[0];
              console.log(this.payementResitApprove)
      
              if (response.pages && response.pages.length > 0) {
                const pagination = response.pages[0];
      
                this.collectionSize = pagination.totalItems; // Total number of items
                this.pageSize = 5; // Number of items per page (should match backend size)
      
                console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
              } else {
                console.warn("Pagination details not found in response");
              }
            }else{
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
         
          },
          (error) => {
            console.error("Error fetching employees:", error);
          }
        )
      }


    

      dateFilterInactive(commonStatus:string,requestStatus:string){
        this.pdfService.getZoomFilterDate(this.fromDate,this.ToDate,commonStatus,requestStatus ,this.page2 - 1, this.pageSize2).subscribe(
          (response) => {
            if(response.status == true){
              this.payementResitInactive = response.payload[0];
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
            }else{
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
           
          },
          (error) => {
            console.error("Error fetching employees:", error);
          }
        )
}



dateFilterComplete(commonStatus:string,requestStatus:string){
  this.pdfService.getZoomFilterDate(this.fromDate,this.ToDate,commonStatus,requestStatus ,this.page1 - 1, this.pageSize1).subscribe(
    (response) => {

      if(response.status == true){
        this.payementResitCompleted = response.payload[0];
        console.log(this.payementResitCompleted)
  
        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];
  
          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 5; // Number of items per page (should match backend size)
  
          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      }else{
        this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

      }
     
    },
    (error) => {
      console.error("Error fetching employees:", error);
    }
  )
}
clearData(){
 this.fromDate = '';
 this.ToDate = '';
 this.perchaseId = ''; 
  this.selectedFile  = null;
  this.systemProfileId = '';
  this.searchInput = ''; 
  this.DropDownValue = '';
}

onTabClick(event: any) {
  this.clearData();
  this.getAllFilterPayementResitStatusApproved("ACTIVE", "PENDING");
  this.getAllFilterPayementResitStatusComplete("ACTIVE", "COMPLETED");
  this.getAllFilterPayementResitStatusInactive("INACTIVE", "");

 }
 openSecondModal(content2: any) {
  this.modalService.open(content2, { size: 'md' }).result.then(() => { }, () => { });
}

 openLg(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }

   onViewBtn(resitData: PayemntResit, index: number) {
       this.payementResitshow = resitData;
       console.log(this.payementResitshow)
    }
}
