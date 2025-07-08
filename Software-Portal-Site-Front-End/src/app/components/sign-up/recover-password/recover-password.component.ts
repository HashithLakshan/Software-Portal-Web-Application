import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoadingService } from 'src/app/services/loading.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-recover-password',
  templateUrl: './recover-password.component.html',
  styleUrls: ['./recover-password.component.css']
})
export class RecoverPasswordComponent {
  email: String = '';
  sendCode: String = '';
  password: String = '';

  constructor(private userService : UserService,private loadingService :LoadingService, private messageService: MessageService,private route: ActivatedRoute){}


  recoverPassword(){
    this.loadingService.show();
    this.route.queryParams.subscribe(params => {
      this.email = params['email'];  // Get the email from query params
    });
    console.log(this.email)
    console.log(this.sendCode)
    console.log(this.password)

    this.userService.recoverPassword(this.sendCode,this.email,this.password).subscribe(
      (response)=>{
        if(response.status == true){
          this.loadingService.hide();
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          window.close();

  
        }else{
          this.loadingService.hide();
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
  
        }
      }
    );
    
  }
}
