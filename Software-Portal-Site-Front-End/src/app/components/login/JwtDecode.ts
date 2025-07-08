import { jwtDecode } from 'jwt-decode';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtDecodes {
  isAdmin(): boolean {
    const token = localStorage.getItem('jwtToken');
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      console.log(decoded.roles?.[0] + ' is the role');
      return decoded.roles?.includes('ROLE_ADMIN');
    } catch (error) {
      console.error('Invalid token', error);
      return false;
    }
  }
}
