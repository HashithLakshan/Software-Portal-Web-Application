import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeService } from 'src/app/services/employee.service';
import { ImageService } from 'src/app/services/image.service';
import { UserService } from 'src/app/services/user.service';
import { Employee } from 'src/app/shared/employee';
import { User } from 'src/app/shared/user';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent {


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
      requestStatus: 'PENDING'

  }

  confirmPassword: string = '';
  email1: string = '';
  proImg: File | null = null;
  imageUrl: string | ArrayBuffer | null = null;
  previewUrl: string | ArrayBuffer | null = null; // Stores the preview URL


  constructor(private router: Router, private userService: UserService, private employeeService: EmployeeService, private imageService: ImageService) { }



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

        console.log(this.employee)
        if (!this.proImg) {
          Swal.fire('', 'Uplod Your profile photo', 'info');
          return;
        }
      
        const payload = {
          image: this.proImg,
          employeeId: this.employee.employeeId
        }
       

              this.employeeService.saveUser(this.employee).subscribe(
                (response) => {
                  if (response.status === true) {
                    if (payload) {
                      this.imageService.uploadImgs(payload).subscribe(
                        (response) => {
                          console.log('Upload success:', response);
                          Swal.fire('', 'We contact you soon !!', 'success');
                          this.router.navigate(['/software']);
                        },
                        (err) => {
                          console.error('Error uploading file:', err);
                          Swal.fire('', 'please wait for Admin Approvel', 'success');
                          this.router.navigate(['/software']);
                        }
                      );
                    }
                  }else{
                    if(response.errorMessages){
                   for (let i = 0; i < response.errorMessages.length; i++) {
                                   Swal.fire('', response.errorMessages[i], 'info');
                   
                       }
                      }
                      Swal.fire('',response.commonMessage,'info')
                  }
                }
              )


        
         
     

    } else {
      Swal.fire('', 'Invalid Email', 'error');

    }
  
  
  }else{
    Swal.fire('', 'Email field is empty', 'info');
  }
  }

  onFileSelected(event: any): void {
    this.proImg = event.target.files[0]; // Save the file for further processing (e.g., upload)
    
    if (event.target.files && event.target.files[0]) { // Check if a file is selected
      const file = event.target.files[0];
      const reader = new FileReader();
  
      // Read the file as a data URL
      reader.onload = () => {
        this.previewUrl = reader.result; // Set the preview URL
      };
  
      reader.readAsDataURL(file); // Trigger the file read
    }
  }
  
  removeFile(): void {
    this.proImg = null; // Clear the file reference
    this.previewUrl = null; // Clear the preview URL
    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = ''; // Reset the input field
    }
  }
}