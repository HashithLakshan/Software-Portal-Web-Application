import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/shared/user';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.css']
})
export class SuperAdminComponent {

  user: User = {
    commonStatus: 'ACTIVE',
    requestStatus : 'APPROVED',
    userId: '25`',
    userName: '',
    email: '',
    rollDto: {
      rollId: '1',
      rollName: 'ADMIN'
    },
    password: ''
  };
  confirmPassword: string = '';
  email1: string = '';

  constructor(private router: Router, private userService: UserService) { }



  onSubmit(): void {
    // Validate required fields


    function containsAtSymbol(input: string): boolean {
      return input.includes('@');
    }

    // Validate password confirmation


    function generateRandomId(): string {
      return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit random ID
    }
    this.user.userId = generateRandomId();

    const x = containsAtSymbol(this.email1);
    console.log(x);

    if (x === true) {
      this.user.email = this.email1;
      console.log(this.user);
      if (this.user.password == this.confirmPassword) {



        this.userService.registerAdmin(this.user).subscribe(
          (response: any) => { // Ensure correct response type
            console.log(response);
            if (response.message_status === 'Success') {
              this.router.navigate(['SuperAdmin-login']);
              Swal.fire('', 'Successfully registered as Super Admin', 'success');
            } else {
              Swal.fire('', response.commonMessage, 'error');
            }
          },
          (error) => {
            Swal.fire('', 'A backend error occurred. Please try again later.', 'error');
          }
        );
      } else {

        Swal.fire('', 'Passwords do not match', 'error');

      }

    } else {
      Swal.fire('', 'Invalid Email', 'error');

    }
  }
}