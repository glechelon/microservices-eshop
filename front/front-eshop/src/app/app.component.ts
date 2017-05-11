import { Component, OnInit } from '@angular/core';
import { AuthGuard } from './guards/auth.gard';
import { Observable } from 'rxjs/Observable';
import { Router, NavigationEnd } from '@angular/router';
import { NotificationsComponent } from './notifications/notifications.component';
import { Message } from './notifications/message';
import { SharedService } from './notifications/shared.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ AuthGuard ]
})
export class AppComponent implements OnInit {
	private logged : boolean;
	private message : Message;
	private notifVisible : boolean;

	constructor(private authService: AuthGuard, private router: Router, private sharedService: SharedService) {
		this.logged = false;
		this.notifVisible = false;
		this.checkIsConnected();
        this.router.events.subscribe( (event)=>{
        	if(event instanceof NavigationEnd){
        		this.checkIsConnected();
        	}
        });
	}

	public ngOnInit() : void {
		this.sharedService.setRootComponent(this);
	}

	private checkIsConnected() : void {
		let activate = this.authService.canActivate();
		let isOsb = activate instanceof Observable;

		if(isOsb){
			(<Observable<boolean>> activate).subscribe(
				resp => {
					this.logged = resp;
				},
				error => {
					this.logged = false;
				}
			)
		}
	}

	public isLogged(){
		return this.logged;
	}

	public disconnect(){
		localStorage.clear();
		this.logged = false;
	}

	public getMessage() : Message {
		return this.message;
	}

	public setMessage(message : string, success : boolean, visible : boolean) : void {
		this.message = new Message(message, success, visible);
		this.notifVisible = visible;
	}

	public notificationVisible() : boolean {
		return this.notifVisible;
	}
}
