import { Component, OnInit, OnDestroy } from '@angular/core';

import {style, state, animate, transition, trigger} from '@angular/core';

@Component({
  selector: 'sucessfulladd-component',
  templateUrl: './sucessfulladd.component.html',
  animations: [
  trigger('fadeInOut', [
	    transition(':enter', [
	      style({opacity:0}),
	      animate(500, style({opacity:1})) 
	    ]),
	    transition(':leave', [
	      animate(500, style({opacity:0})) 
	    ])
  	])
  ]
})

export class SuccessfullAddComponent {
	private show : boolean = false;
	constructor(){
		console.log('construct')
	}

	setVisible(visible : boolean){
		this.show = visible;
	}

	close(){
		this.show = false;
	}
}