import { Injectable } from '@angular/core';
import { Cliente } from './cliente';
import { Observable} from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private urlEndPoint : string = 'http://localhost:8080/api/clientes';
  private httpHeaders : HttpHeaders = new HttpHeaders();

  //constructor(private httpClient : HttpClient,
  //            private httpHeaders : HttpHeaders ) {

  //  this.httpHeaders =  new HttpHeaders().set('content-type','application/json');

  //}

  constructor (private httpClient : HttpClient){

    this.httpHeaders = new HttpHeaders().set('Content-Type','application/json');

  }

  getClientes(): Observable <Cliente[]> {

    //return this.httpClient.get<Cliente>(urlEndPoint);
    return this.httpClient.get(this.urlEndPoint).pipe(
      map(response => response as Cliente[])
    );

  }

  create (cliente : Cliente): Observable<Cliente>{

    return this.httpClient.post<Cliente>(this.urlEndPoint,cliente, {headers : this.httpHeaders});

  }

  getCliente(id:number): Observable<Cliente>{

    return this.httpClient.get<Cliente>(`${this.urlEndPoint}/${id}`);

  }

  updateCliente(cliente : Cliente): Observable<Cliente>{

    return this.httpClient.put<Cliente>(`${this.urlEndPoint}/${cliente.id}`, cliente, {headers : this.httpHeaders});

  }

  deleteCliente(id:number):Observable<Cliente>{

    return this.httpClient.delete<Cliente>(`${this.urlEndPoint}/${id}`,{headers : this.httpHeaders});

  }

}
