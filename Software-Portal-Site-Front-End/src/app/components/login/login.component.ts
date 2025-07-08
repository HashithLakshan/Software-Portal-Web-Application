import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { AuthServiceService } from 'src/app/services/auth.service';
import { LoadingService } from 'src/app/services/loading.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';
import { JwtDecodes } from './JwtDecode';

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
		private modalService: NgbModal,private loadingService : LoadingService,private messageService: MessageService,private authService: AuthServiceService, private _Activatedroute: ActivatedRoute
,private jwtDecode : JwtDecodes) {
  // config.backdrop = 'static';
  // config.keyboard = false;
    this.myForm = new FormGroup({
      userName: new FormControl(''),
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
      userName: this.myForm.value.userName,
      password: this.myForm.value.password,
    };
    console.log(loginData);
      this.authService.login(loginData).subscribe(

        (response) => {
              console.log(response);
          this.saveToSession(response);
          if(this.jwtDecode.isAdmin() === true){
           this.router.navigateByUrl('/admin');

          }
          else{
            this.router.navigateByUrl('/software/home');
          }
      },
      (error) => {
        Swal.fire('Oops', 'Username or Password Incorrect!', 'error');
      }
    );
    
    // function containsAtSymbol(input: string): boolean {
    //   return input.includes('@');
    // }
    
  
  
}




  saveToSession(data: any){
    localStorage.setItem('role', data.roles);
    localStorage.setItem('jwtToken', data.token);
    localStorage.setItem('userName', data.username);
    localStorage.setItem('email', data.email);
    // localStorage.setItem('role', data.user.role[0].roleName);
    localStorage.setItem('userID', data.id);
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

function jwtDecode(token: string): any {
  throw new Error('Function not implemented.');
}
