import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  modal : boolean = false;
  _notificarUpload : EventEmitter<any>;

  constructor() { }

  public get notificarUpload(): EventEmitter<any>{

     return this._notificarUpload;

  }

  abrirModal(){

    this.modal = true;

  }

  cerrarModal(){

    this.modal = false;

  }

}
