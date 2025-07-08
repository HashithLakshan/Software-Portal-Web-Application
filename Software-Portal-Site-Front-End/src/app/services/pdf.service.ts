import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  private apiUrl = 'http://localhost:8081/api/pdf'; // Change this to your backend URL

  constructor(private http: HttpClient) {}

  uploadPdf(file: File, systemProfileId: string, perchaseId: string,requestStatus: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('systemProfileId', systemProfileId);
    formData.append('perchaseId', perchaseId);
    formData.append('requestStatus', requestStatus);
  



    return this.http.post<any>(`${this.apiUrl}/upload`, formData);
  }
   

  getPaymentReciptUpdateStatus(id: string, actionDo: string,): Observable<any> {
    const url = `${this.apiUrl}/updateStatus?id=${id}&actionDo=${actionDo}`;
    return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
  }
  getZoomFilterStatus(commonStatus: string, requestStatus: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/filtered?commonStatus=${commonStatus}&requestStatus=${requestStatus}&page=${page}&size=${size}`);
  }
  
  downloadPdf(id: number) {
    return this.http.get(`${this.apiUrl}/download/${id}`, {
      responseType: 'blob', 
      observe: 'response' 
    });
  }
   
  getPerchaseIdSearch( commonStatus: string, requestStatus: string,perchaseId: string,): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailPerchaseId?commonStatus=${commonStatus}&requestStatus=${requestStatus}&perchaseId=${perchaseId}`);
  }

  getIdSearch( commonStatus: string, requestStatus: string,id: string,): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailId?commonStatus=${commonStatus}&requestStatus=${requestStatus}&id=${id}`);
  }

  getZoomFilterDate(fromDate:string,toDate: string,commonStatus: string, requestStatus: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailsAllDateFilter?fromDate=${fromDate}&toDate=${toDate}&commonStatus=${commonStatus}&requestStatus=${requestStatus}&page=${page}&size=${size}`);
  }
}
