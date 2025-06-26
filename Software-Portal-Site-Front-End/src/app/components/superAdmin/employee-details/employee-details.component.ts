import { Component, inject, TemplateRef } from '@angular/core';
import { Router } from '@angular/router';
import {  NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeService } from 'src/app/services/employee.service';
import { ImageService } from 'src/app/services/image.service';
import { UserService } from 'src/app/services/user.service';
import { Employee } from 'src/app/shared/employee';
import { User } from 'src/app/shared/user';
import Swal from 'sweetalert2';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent {
  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 5;

  collectionSize1: number = 0;
  page1: number = 1;
  pageSize1: number = 5;

  collectionSize2: number = 0;
  page2: number = 1;
  pageSize2: number = 5;

	private modalService = inject(NgbModal);
   user: User = {
      commonStatus: 'ACTIVE',
      userId: '',
      userName: '',
      email: '',
      rollDto: {
        rollId: '2'
  
      },
      password: ''
    }
    employee: Employee = {
      employeeId: '',
      employeeNumber: '',
      companyRgNo:'',
      companyName: '',
      employeeNIC: '',
      commonStatus: 'ACTIVE',
      companyEmail:'',
      requestStatus: 'APPROVED'
     
    }
    employeeInformation: Employee = {
      employeeId: '',
      employeeNumber: '',
      companyRgNo:'',
      companyName: '',
      employeeNIC: '',
      commonStatus: '',
      companyEmail:'',
      requestStatus: '',
     
    }
 
     
    searchInputApprove :String = '';
    searchInputPending :String = '';
    searchInputInactive :String = '';

    dropDown : String = '';
    
    employeeActive : Employee [] = [];
    employeeApprove : Employee [] = [];
    employeeInActive : Employee [] = [];
    confirmPassword: string = '';
    confirmPassword1: string = '';
    email1: string = '';
    proImg: File | null = null;
    proImgUpdate: File | null = null;
    imageUrl: string | ArrayBuffer | null = null;
    previewUrl: string | ArrayBuffer | null = null; 
    previewUrlback: string | ArrayBuffer | null = null; 

  
  
    constructor(private router: Router,private messageService: MessageService,private confirmationService: ConfirmationService
      , private userService: UserService, private employeeService: EmployeeService, private imageService: ImageService) { 
        this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
        this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING");
        this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");

    }
  
  
  
    onSubmit(): void {
      // Validate required fields
  
  
      function containsAtSymbol(input: string): boolean {
        return input.includes('@');
      }
  
      // Validate password confirmation
  
  
      function generateRandomId(): string {
        return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit random ID
      }
      this.employee.employeeId = generateRandomId();
      const x = containsAtSymbol(this.email1);
      if(this.email1 != null && this.email1 != null){
      if (x === true) {
        this.employee.companyEmail = this.email1;
        console.log(this.user);
        if (this.user.password == this.confirmPassword) {
  
          console.log(this.employee)
  
        
          const payload = {
            image: this.proImg,
            employeeId: this.employee.employeeId
          }
         console.log(this.employee)
                if (!this.proImg) {
                  // Swal.fire('', 'Uplod Your profile photo', 'info');
                  this.messageService.add({ severity: 'info', summary: 'info', detail: 'Uplod Your profile photo' });

                  return;
                }
                this.employeeService.saveUserAdmin( this.employee).subscribe(
                  (response) => {
                    if (response.status === true) {
                      if (payload) {
                        this.imageService.uploadImgs(payload).subscribe(
                          (response) => {
                            console.log('Upload success:', response);
                            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Employee regiter successfully' });

                            this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
                            // Swal.fire('', 'please wait for Admin Approvel', 'success');
                           this.employee. employeeNumber= '';
                           this.employee.companyRgNo='';
                           this.employee. companyName= '';
                           this.employee. employeeNIC= '';
                           this.email1 = '';
                           this.user.password = '';
                           this.confirmPassword = '';
                          this.removeFile();
                          },
                          (err) => {
                            console.error('Error uploading file:', err);
                                                    }
                        );
                      }
                    }else{
                      if(response.errorMessages){
                     for (let i = 0; i < response.errorMessages.length; i++) {
                                    //  Swal.fire('', response.errorMessages[i], 'info');
                                    this.messageService.add({ severity: 'info', summary: 'Info', detail: response.errorMessages[i] });

                     
                         }
                        }
                        this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

                    }
                  }
                )
  
  
              
           
        } else {
  
          this.messageService.add({ severity: 'error', summary: 'error', detail:'Password does not match ' });
  
        }
  
      } else {
        // Swal.fire('', 'Invalid Email', 'error');
        this.messageService.add({ severity: 'error', summary: 'error', detail:'Invalid Email ' });

      }
    
    
    }else{
      // Swal.fire('', 'Email field is empty', 'info');
      this.messageService.add({ severity: 'info', summary: 'info', detail:'Email field is empty' });

    }
    }
  
    onFileSelected(event: any): void {
      this.proImg = event.target.files[0];
      
      if (event.target.files && event.target.files[0]) { 
        const file = event.target.files[0];
        const reader = new FileReader();
    
        // Read the file as a data URL
        reader.onload = () => {
          this.previewUrl = reader.result; 
        };
    
        reader.readAsDataURL(file); // Trigger the file read
      }
    }
    
    removeFile(): void {
      this.proImg = null; 
      this.previewUrl = null; 
      const fileInput = document.getElementById('file') as HTMLInputElement;
      if (fileInput) {
        fileInput.value = ''; 
      }
    }




  openLg(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }
  
  getAllFilterEmployeeStatuspending(commonStatus:"ACTIVE", requestStatus : "PENDING"){
  
    this.employeeService.getEmployeesFilterStatus(commonStatus, requestStatus, this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.employeeActive = response.payload[0]; 
        console.log(this.employeeActive)
        
        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 5; // Number of items per page (should match backend size)

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

  getAllFilterEmployeeStatusActive(commonStatus:"ACTIVE", requestStatus : "APPROVED"){
 
    this.employeeService.getEmployeesFilterStatus(commonStatus, requestStatus, this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.employeeApprove = response.payload[0]; 
        console.log(this.employeeApprove)
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
   getAllFilterEmployeeStatusInactive(commonStatus:"INACTIVE", requestStatus : ""){
 
     this.employeeService.getEmployeesFilterStatus(commonStatus, requestStatus, this.page2 - 1, this.pageSize2).subscribe(
       (response)=>{
         console.log(response);
        this.employeeInActive = response.payload[0];
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

   onPageChange(page: number) {
    this.page = page;
    this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
  }

  onPageChange2(page: number) {
    this.page1 = page;
    this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING"); 
  }

  onPageChange3(page: number) {
    this.page2 = page;
    this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");
  }

  clearData(){

    this.employee. employeeNumber= '';
                           this.employee.companyRgNo='';
                           this.employee. companyName= '';
                           this.employee. employeeNIC= '';
                           this.email1 = '';
                           this.user.password = '';
                           this.confirmPassword = '';
                           this.user.userName = '';
                          this.removeFile();
                          this.dropDown = '';
                          this.searchInputApprove  = '';
                          this.searchInputPending  = '';
                          this.searchInputInactive  = '';
                          this.page = 1;
                          this.page1 = 1;
                          this.page2 = 1;
     
  
    

  }

searchApprove(){
  let commonStatus = "ACTIVE";
  let requestStatus = "APPROVED";
  switch(this.dropDown){
case "id" :
  this.employeeService.getEmployeesFilterId(commonStatus,requestStatus,this.searchInputApprove).subscribe(
(response)=>{
  if(response.status == true){
  this.employeeApprove = response.payload;
      this.messageService.add({ severity: 'success', summary: 'success', detail: "Employee successfully filtered" });

  console.log(this.employeeApprove)
  }else{
    this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
    // Swal.fire('',response.commonMessage,'info');
  }
}
  );
break;
case "name" :
  this.employeeService.getEmployeesFilterCompany(commonStatus,requestStatus,this.searchInputApprove).subscribe(
    (response)=>{
      if(response.status == true){
      this.employeeApprove = response.payload;
       this.messageService.add({ severity: 'success', summary: 'success', detail: "Employee successfully filtered" });

      console.log(this.employeeApprove)
      }else{
        this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
        // Swal.fire('',response.commonMessage,'info');
      }
    }
  )
  break;
  default:
    this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
// Swal.fire ('','Select the search type','info')
  }
  }
  searchPending(){
    let commonStatus = "ACTIVE";
    let requestStatus = "PENDING";
    switch(this.dropDown){
  case "id" :
    this.employeeService.getEmployeesFilterId(commonStatus,requestStatus,this.searchInputPending).subscribe(
  (response)=>{
    if(response.status == true){
    this.employeeActive = response.payload;
    console.log(this.employeeApprove)
    }else{
      this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
      // Swal.fire('',response.commonMessage,'info');
    }
  }
    );
  break;
  case "name" :
    this.employeeService.getEmployeesFilterCompany(commonStatus,requestStatus,this.searchInputPending).subscribe(
      (response)=>{
        if(response.status == true){
        this.employeeActive = response.payload;
        console.log(this.employeeApprove)
        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
          // Swal.fire('',response.commonMessage,'info');
        }
      }
    )
    break;
    default:
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
  // Swal.fire ('','Select the search type','info')
    }
  }

  searchInactive(){
    let commonStatus = "INACTIVE";
    let requestStatus = "";
    switch(this.dropDown){
  case "id" :
    console.log(this.searchInputInactive)
    console.log(requestStatus)
    console.log(commonStatus)
    this.employeeService.getEmployeesFilterId(commonStatus,requestStatus,this.searchInputInactive).subscribe(
  (response)=>{
    if(response.status == true){
    this.employeeInActive = response.payload;
    console.log(this.employeeApprove)
    }else{
      this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
      // Swal.fire('',response.commonMessage,'info');
    }
  }
    );
  break;
  case "name" :
    console.log(this.searchInputInactive)
    console.log(requestStatus)
    console.log(commonStatus)

    this.employeeService.getEmployeesFilterCompany(commonStatus,requestStatus,this.searchInputInactive).subscribe(
      (response)=>{
        if(response.status == true){
        this.employeeInActive = response.payload;
        console.log(this.employeeApprove)
        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
          // Swal.fire('',response.commonMessage,'info');
        }
      }
    )
    break;
    default:
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
  // Swal.fire ('','Select the search type','info')
    }
  }

  onDeleteTableItem(employeeId : any){
    let commonStatus = "INACTIVE";
    let requestStatus = "";
    this.employeeService.getEmployeesUpdateStatus(String(employeeId),requestStatus,commonStatus).subscribe(
      (response)=>{
        if(response.status === true){
          console.log(response);
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
          this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING");
          this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");
          console.log(this.employeeApprove)
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
      )
  }

  confirmDelete(employeeId: any) {
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
        this.onPermenetDeleteTableItem(employeeId);
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


  
  onPermenetDeleteTableItem(employeeId : any){
    let commonStatus = "DELETED";
    let requestStatus = "";
    this.employeeService.getEmployeesUpdateStatus(String(employeeId),requestStatus,commonStatus).subscribe(
      (response)=>{
        if(response.status === true){
          // Swal.fire ('',response.commonMessage,'success')
    
          this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
      )
  }

  Approve(employeeId : any){
    let commonStatus = "";
    let requestStatus = "APPROVED";
    this.employeeService.getEmployeesUpdateStatus(String(employeeId),requestStatus,commonStatus).subscribe(
      (response)=>{
        if(response.status === true){
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
          this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING");
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
      )
  }
  ActiveEmployee(employeeId : any){
    let commonStatus = "ACTIVE";
    let requestStatus = "";
    this.employeeService.getEmployeesUpdateStatus(String(employeeId),requestStatus,commonStatus).subscribe(
      (response)=>{
        if(response.status === true){
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
          this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING");
          this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        }else{
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
      )
  }
  openXl(content: TemplateRef<any>) {
		this.modalService.open(content, { size: 'xl' });
	}
  onViewBtn(employeeData: Employee, index: number){
    this.employeeInformation.companyEmail = employeeData.companyEmail
    this.employeeInformation.employeeId = employeeData.employeeId
    this.employeeInformation.requestStatus = employeeData.requestStatus;
    this.employeeInformation.companyName = employeeData.companyName;
    this.employeeInformation.companyRgNo = employeeData.companyRgNo;
    this.employeeInformation.employeeId = employeeData.employeeId;
    this.employeeInformation.employeeNIC = employeeData.employeeNIC;
    this.employeeInformation.employeeNumber = employeeData.employeeNumber;
    this.employeeInformation.commonStatus = employeeData.commonStatus;

  }


backendPhoto(userId : any){
  this.imageService.getUserImage(String(userId)).subscribe(
    (blob: Blob) => {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrlback = reader.result as string; // Convert to Data URL
      };
      reader.readAsDataURL(blob);
    },
    (error) => console.error('Error fetching image:', error)
  );
}
updateEmployee(){
  function containsAtSymbol(input: string): boolean {
    return input.includes('@');
  }




  const i = containsAtSymbol(String(this.employeeInformation.companyEmail));
 
  if(i === true){
             console.log(this.employeeInformation)

        this.employeeService.saveUser( this.employeeInformation).subscribe(
          (response) => {
            if (response.status === true) {
           
              this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage})

              

            }else{
             for (let i = 0; i < response.errorMessages.length; i++) {
                            
                            this.messageService.add({ severity: 'info', summary: 'Info', detail: response.errorMessages[i] });

             
                 }
                 this.messageService.add({ severity: 'info', summary: 'Info', detail: response.commonMessage });

            }
          }
        );
      

     
 
  }
  else{
    this.messageService.add({ severity: 'error', summary: 'error', detail:'email is not valid structure' });

  }
}

  updatePhoto(){
    const payload = {
      image: this.proImgUpdate,
      userId: this.employeeInformation.employeeId
    }
    if (payload.image != null) {
                
      this.imageService.uploadImgs(payload).subscribe(
        (response) => {
if(response.status === true){
  this.messageService.add({ severity: 'success', summary: 'Success', detail:  response.commonMessage })
}else{

  this.messageService.add({ severity: 'info', summary: 'info', detail:  response.commonMessage })

}
       
        
        },
        (err) => {
          console.error('Error uploading file:', err);
                                  }
      );
    }else{
      this.messageService.add({ severity: 'info', summary: 'info', detail: "you are not Update profile picture" })

    }
  }


onFileSelectedUpdate(event: any): void {
  // Check if a file is selected
  if (event.target.files && event.target.files.length > 0) {
    this.proImgUpdate = event.target.files[0];

    const file = event.target.files[0];
    const reader = new FileReader();

    // Read the file as a data URL
    reader.onload = () => {
      this.previewUrlback = reader.result;
    };

    reader.readAsDataURL(file); // Trigger the file read
  } else {
    // No file selected or user canceled selection
    this.proImgUpdate = null;
    this.previewUrlback = null; // Clear the preview
  }
}
onTabClick(event: any) {
  this.clearData();
  this.getAllFilterEmployeeStatusActive("ACTIVE",  "APPROVED"); 
  this.getAllFilterEmployeeStatuspending("ACTIVE",  "PENDING");
  this.getAllFilterEmployeeStatusInactive("INACTIVE",  "");
}
}
















