import { Component, HostListener , ElementRef, Input} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent {
  isSidebarActive = true;

  constructor(private router:Router){}
 
  dropdowns: { [key: string]: boolean } = {}; // Object to track dropdown states

  toggleDropdown(menu: string, event: Event) {
    event.preventDefault(); // Prevents the page from jumping
    this.dropdowns[menu] = !this.dropdowns[menu]; // Toggle the specific dropdown
  }

  
  toggleSidebar() {
    this.isSidebarActive = !this.isSidebarActive;
  }

  // Toggle individual dropdowns
  onLogout(): void {
    localStorage.clear(); 
    this.router.navigate(['/SuperAdmin-login']);
  }

}
