import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { LoadingService } from 'src/app/services/loading.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  myForm: FormGroup;
email : string = '';
  passwordFieldType: string = 'password';
  modalRef: NgbModalRef | null = null;

  
  // Toggle the password field visibility
  togglePasswordVisibility() {
    this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
  }
  constructor(private userService: UserService, private router: Router,config: NgbModalConfig,
		private modalService: NgbModal,private loadingService : LoadingService,private messageService: MessageService,
) {
  // config.backdrop = 'static';
  // config.keyboard = false;
    this.myForm = new FormGroup({
      email: new FormControl(''),
      password: new FormControl(''),
    });
  }
  open(content: any) {
    this.modalRef = this.modalService.open(content);
  }
  closeModal() {
    if (this.modalRef) {
      this.modalRef.close();
      this.modalRef = null; // Reset the reference
    }
  }  
  onClick(): void {

    const loginData = {
      email: this.myForm.value.email,
      password: this.myForm.value.password,
    };
    const x = containsAtSymbol(this.myForm.value.email);






    if (x === true) {

      
      this.userService.getUserUserNameAndUserPassword(loginData).subscribe(

        (response) => {
          console.log(response)

         if (response.status == true){

          const user = response.payload[0];
          console.log(user.userId)
       
            // Store session data
            sessionStorage.setItem('userId', user.userId);
            sessionStorage.setItem('userName', user.userName);
            sessionStorage.setItem('email', user.email);
    
            // Navigate based on user role
            switch (user.rollDto.rollName) {
              // case 'Employee':
              //   this.router.navigate(['/admin-dash']);
              //   break;
              case 'SuperAdmin':
                this.router.navigate(['/admin'], { replaceUrl: true }).then(() => {
                  history.pushState(null, '', location.href);
                  window.onpopstate = () => {
                    history.pushState(null, '', location.href);
                    // Optional: show alert or redirect
                  };
                });
                Swal.fire('', 'Login Success', 'success');

                break;
              default:
                Swal.fire('', 'Unknown role. Please contact support.', 'error');
                return;
            }
        
         }else if(response.status == false){
          console.log(response.commonMessage)
          Swal.fire('',response.commonMessage, 'error');

         }
         
          // Check if response contains the payload
        
        },
        (error) => {
          console.error('Backend Error:', error);
          Swal.fire('Error', 'There was an issue fetching the user data.', 'error');
        }
      );
    } else {
      Swal.fire('Invalid Email', 'Please enter a valid email address.', 'info');
    }
    
    function containsAtSymbol(input: string): boolean {
      return input.includes('@');
    }
    
}


emailsendOtp(){
  console.log(this.email);
  this.loadingService.show();
  this.userService.forgetPasswordEmailSend(String(this.email)).subscribe(
    (response)=>{
      if(response.status == true){
        this.loadingService.hide();
        this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
        this.closeModal();
        const email: string = String(this.email); // Ensure it's a string
        const url = `/recover-password?email=${encodeURIComponent(email)}`;
        window.open(url, '_blank'); // Opens in a new tab
        

      }else{
        this.loadingService.hide();
        this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

      }
    }
  );

  
}
}