import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data['role']; // ✅ Fix: use 'role' not 'ROLE_ADMIN'
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      this.router.navigate(['/SuperAdmin-login']);
      return false;
    }

    try {
      const decoded: any = jwtDecode(token);
      console.log('Decoded roles:', decoded.roles);
      if (decoded.roles?.includes(expectedRole)) {
        return true;
      } else {
        this.router.navigate(['/software']);
        return false;
      }
    } catch (error) {
      console.error('JWT Decode failed:', error);
      this.router.navigate(['/SuperAdmin-login']);
      return false;
    }
  }
}
