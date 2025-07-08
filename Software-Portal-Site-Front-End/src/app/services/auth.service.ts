import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

   private baseUrl = 'http://localhost:8081/api/auth'; // Adjust to your backend URL

  constructor(private http: HttpClient) {}

  login(payload : any) {
    return this.http.post(`${this.baseUrl}/signin`, payload);
  }

  logout() {
    localStorage.clear(); 
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
