import { Injectable, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { ZoomService } from './zoom.service';

@Injectable({
  providedIn: 'root' // This makes the service globally available
})
export class ReminderService implements OnDestroy {
  private reminderSubscription!: Subscription;

  constructor(
    private zoomService: ZoomService,
    private messageService: MessageService
  ) { }

  startReminder(): void {
    if (!this.reminderSubscription) {
      this.reminderSubscription = interval(300000).subscribe(() => { // Every 5 mins
        this.reminder();
      });
    }
  }

  private reminder(): void {
    this.zoomService.reminderMeetings().subscribe(response => {
      if (response.status === true) {
        this.messageService.add({
          severity: 'warning',
          summary: 'Reminder Meeting',
          detail: response.commonMessage,
          life: 0, // Stays until manually closed
          sticky: true
        });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.reminderSubscription) {
      this.reminderSubscription.unsubscribe();
    }
  }
}
