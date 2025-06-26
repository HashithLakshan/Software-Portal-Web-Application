import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemIssueMessagesService {

  constructor(private http: HttpClient) { }
       private apiUrl = 'http://localhost:8081/api/issuesMessages';

 saver(payload: any): Observable<any> {
      return this.http.post<any>(`${this.apiUrl}/save`, payload);
    }

    getAllMessages(commonStatus:any,replyMessageStatus:any, page: number, size: number):Observable<any>{
      return this.http.get<any>(`${this.apiUrl}/filtered?commonStatus=${commonStatus}&replyMessageStatus=${replyMessageStatus}&page=${page}&size=${size}`);
    }

    getMessageFilterDate(fromDate:string,toDate: string,commonStatus: string, replyMessageStatus: string, page: number, size: number): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/getDetailsAllDateFilter?fromDate=${fromDate}&toDate=${toDate}&commonStatus=${commonStatus}&replyMessageStatus=${replyMessageStatus}&page=${page}&size=${size}`);
    }

    getMessageFilterPerchaseId(perchaseId:string,status: string): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/getPerchaseIdWithDetails?perchaseId=${perchaseId}&status=${status}`);
    }
    getMessageFilterIssueId(issueId:string,status: string): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/issueIdDetails?issueId=${issueId}&status=${status}`);
    }

    updateStatus(status:any,issueId : any,subject : any,body : any): Observable<any> {
      const url = `${this.apiUrl}/updateStatus?status=${status}&issueId=${issueId}&subject=${subject}&body=${body}`;
      return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
    }
}

