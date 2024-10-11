import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient: any;
  private notificationUrl = '/topic/notifications';

  constructor() { }

  connect() {
    const socket = new SockJS('http://localhost:9191/NOTIFICATION-MAILING-SERVICE/ws-notifications');
    this.stompClient = Stomp.over(socket);
    const _this = this;

    this.stompClient.connect({}, function () {
      _this.stompClient.subscribe(_this.notificationUrl, (message: any) => {
        if (message.body) {            
          console.log(message.body);
        }
      });
    });
  }

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log('Disconnected');
  }
}
