import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-super-admin-top',
  templateUrl: './super-admin-top.component.html',
  styleUrls: ['./super-admin-top.component.css']
})
export class SuperAdminTopComponent {
constructor(private router:Router){}

  onLogout(): void {
    localStorage.removeItem('authToken'); 
    sessionStorage.clear(); 
    this.router.navigate(['/login']);
  }
}
