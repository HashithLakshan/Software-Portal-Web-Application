import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemIssuesAnswerMessageService {

  constructor(private http: HttpClient) { }
  private apiUrl = 'http://localhost:8081/api/answerMessages';

  getByAnswers(issueId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getInformation/${issueId}`);
  }

}
