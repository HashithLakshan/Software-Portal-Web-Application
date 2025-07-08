import { Component, inject } from '@angular/core';
import { Route, Router } from '@angular/router';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { CategoryService } from 'src/app/services/category.service';
import { LoadingService } from 'src/app/services/loading.service';
import { TimeSlotServiceService } from 'src/app/services/time-slot-service.service';
import { UserService } from 'src/app/services/user.service';
import { ZoomService } from 'src/app/services/zoom.service';
import { Category } from 'src/app/shared/category';
import { TimeSlots } from 'src/app/shared/timeSlots';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-web-site-maneging',
  templateUrl: './web-site-maneging.component.html',
  styleUrls: ['./web-site-maneging.component.css']
})
export class WebSiteManegingComponent {

  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 10;

  collectionSize1: number = 0;
  page1: number = 1;
  pageSize1: number = 10;

  categoryShow: Category = {
    categoryId: '',
    categoryName: '',
    saveDate: '',
    categoryDiscription: '',
    commonStatus: 'ACTIVE'

  }
  category: Category = {
    categoryId: '',
    categoryName: '',
    saveDate: '',
    categoryDiscription: '',
    commonStatus: 'ACTIVE'

  }
  timeSlot: TimeSlots = {
    zoomTimeSlotId: '',
    slotOpenTime: '',
    slotCloseTime: '',
    commonStatus: 'ACTIVE'
  }
  timeSlotShow: TimeSlots = {
    zoomTimeSlotId: '',
    slotOpenTime: '',
    slotCloseTime: '',
    commonStatus: ''
  }
  private modalService = inject(NgbModal);
  categoryActive: Category[] = [];
  timeSlotActive: TimeSlots[] = [];
  dropDownValue: String = '';
  searchValue: String = '';
  email: String = '';
  code: String = '';






  constructor(private categoryService: CategoryService, config: NgbModalConfig, private timeSlotService: TimeSlotServiceService, private messageService: MessageService, private userService: UserService, private loadingService: LoadingService, private router: Router,private zoomService :ZoomService) {
    this.getAllFilterActiveCategory('ACTIVE');
    this.getAllFilterActiveTimeSlot('ACTIVE');

  }

  openSecondModal(content: any) {
    this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }
  openSecondModal1(content2: any) {
    this.modalService.open(content2, { size: 'md' }).result.then(() => { }, () => { });
  }
  onPageChange(page: number) {
    this.page = page;
    this.getAllFilterActiveCategory('ACTIVE');
  }

  onPageChange2(page: number) {
    this.page1 = page;
    this.getAllFilterActiveCategory('ACTIVE');
  }

  getAllFilterActiveCategory(commonStatus: "ACTIVE") {
    this.categoryService.getAllCategoriesActive(commonStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.categoryActive = response.payload[0];
        console.log(this.categoryActive)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 10; // Number of items per page (should match backend size)

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

  getAllFilterActiveTimeSlot(commonStatus: "ACTIVE") {
    this.timeSlotService.getAllTimeSlotActive(commonStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.timeSlotActive = response.payload[0];
        console.log(this.timeSlotActive)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 10; // Number of items per page (should match backend size)

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
  saveCategory() {
    console.log(this.category);
    if (this.categoryShow.categoryId != '') {
      this.categoryService.saveCategory(this.categoryShow).subscribe(
        (response) => {
          if (response.status == true) {
            console.log(response);
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterActiveCategory('ACTIVE');

          } else {
            console.log(response);

            if (response.errorMessages) {
              for (let i = 0; response.errorMessages.length > i; i++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[i] });

              }
            } else {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
          }
        }
      )

    } else {
      this.categoryService.saveCategory(this.category).subscribe(
        (response) => {
          if (response.status == true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterActiveCategory('ACTIVE');

          } else {
            console.log(response);

            if (response.errorMessages) {
              for (let i = 0; response.errorMessages.length > i; i++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[i] });

              }
            } else {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
          }
        }
      )
    }
  }

  saveTimeSlot() {
    this.timeSlotService.saveTimeSlot(this.timeSlot).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.getAllFilterActiveTimeSlot('ACTIVE');

        } else {
          console.log(response);
          if (response.errorMessages) {
            for (let i = 0; response.errorMessages.length > i; i++) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[i] });

            }
          } else {
            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

          }
        }
      }
    )
  }

  search() {
    switch (this.dropDownValue) {
      case 'categoryName':
        this.categoryService.searchCategoryName(String(this.searchValue)).subscribe(
          (response) => {
            if (response.status == true) {
              this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });

            } else {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

            }
          }
        );
        break;
      default:

        this.messageService.add({ severity: 'info', summary: 'info', detail: "choose a search type" });

    }
  }

  clearData() {
    this.category.categoryDiscription = '';
    this.category.categoryId = '';
    this.category.categoryName = '';
    this.timeSlot.zoomTimeSlotId = '';
    this.timeSlot.slotOpenTime = '';
    this.timeSlot.slotCloseTime = '';
    this.dropDownValue = '';
    this.searchValue = '';
  }



  deleteCategory(categoryId: any, commonStatus: any) {
    this.categoryService.statusUpdate(String(categoryId), commonStatus).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.getAllFilterActiveCategory('ACTIVE');
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    )
  }

  deleteTimeSlot(zoomTimeSlotId: any, commonStatus: any) {
    this.timeSlotService.statusUpdate(String(zoomTimeSlotId), commonStatus).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.getAllFilterActiveTimeSlot('ACTIVE');
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    )
  }

  confirmetionDelete(id: any, commonStatus: any, action: any) {
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
        if (action == 'category') {
          this.deleteCategory(id, commonStatus);
        } else {
          this.deleteTimeSlot(id, commonStatus);
        }


      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Swal.fire(
        //   'Cancelled',
        //   'Your employee record is safe',
        //   'info'
        // );
        this.messageService.add({ severity: 'info', summary: 'info', detail: "Safe your record" });


      }
    });

  }
  onViewBtn(categoryData: Category, index: number) {
    this.categoryShow = categoryData;
    console.log(categoryData)
  }

  onViewBtn2(slotTimeData: TimeSlots, index: number) {
    this.timeSlotShow = slotTimeData;
  }

  clearData2() {
    this.categoryShow.categoryDiscription = '';
    this.categoryShow.categoryId = '';
    this.categoryShow.categoryName = '';


  }

  sendZoomCode(){
    this.loadingService.show();
    console.log(this.code)
    this.zoomService.zoomCallBack(this.code).subscribe(
      (response)=>{
        if(response.status == true){
          this.loadingService.hide();
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
        }else{
          this.loadingService.hide();
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
        }
      },  (error) => {
        this.loadingService.hide();
        
        let errorMessage = 'Failed to connect with Zoom';
        
        // Handle different types of errors
        if (error.error?.commonMessage) {
          errorMessage = error.error.commonMessage;
        } else if (error.status === 400) {
          errorMessage = 'Invalid authorization code. Please try again.';
        } else if (error.status === 401) {
          errorMessage = 'Authentication failed. Please check your credentials.';
        } else if (error.status === 0) {
          errorMessage = 'Network error. Please check your internet connection.';
        }
  
        this.messageService.add({ severity: 'error', summary: 'error', detail: errorMessage });

        
        // Log the full error for debugging
        console.error('Zoom connection error:', error);
      
    });
  }

  emailsendOtp() {
    console.log(this.email);
    this.loadingService.show();
    this.userService.forgetPasswordEmailSend(this.email).subscribe(
      (response) => {
        if (response.status == true) {
          this.loadingService.hide();
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          const email: string = String(this.email); // Ensure it's a string
          const url = `recover-password?email=${encodeURIComponent(email)}`;
          window.open(url, '_blank'); // Opens in a new tab



        } else {
          this.loadingService.hide();
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    );


  }


}
