import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { AuthServiceService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router,private authService : AuthServiceService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //  Get token from localStorage
    const token = localStorage.getItem('jwtToken');
    console.log("Token in interceptor:", token);

    //  Clone request and attach token
    let clonedReq = req;
    if (token) {
      clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      console.log("Authorization header set with token:", clonedReq);
    }

    //  Handle request with error handling
    return next.handle(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        //  Catch token expired error from backend
        if (error.status === 401) {
          const errorMsg = "Unorthenticated request. Please login again.";

          Swal.fire({
            icon: 'error',
            title: 'Session Expired',
            text: errorMsg,
            confirmButtonText: 'Login Again'
          }).then(() => {
            this.authService.logout();
            this.router.navigate(['/SuperAdmin-login']);    //  Redirect to login
          });
        }

        return throwError(() => error);
      })
    );
  }
}
