import { Injectable } from '@angular/core';
import { formatDate, DatePipe } from '@angular/common';
import localeES from '@angular/common/locales/es';
import { Cliente } from './cliente';
import { Observable, throwError} from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import swal from 'sweetalert2';
import { Router } from '@angular/router';

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

  constructor (private httpClient : HttpClient,
               private router : Router ){

    this.httpHeaders = new HttpHeaders().set('Content-Type','application/json');

  }

  getClientes(): Observable <Cliente[]> {

    //return this.httpClient.get<Cliente>(urlEndPoint);
    return this.httpClient.get(this.urlEndPoint).pipe(
      tap( response => {
        let clientes = response as Cliente[];

        clientes.forEach( cliente => {
          console.log (cliente.nombre);
        })
        
      }),
      map(response => {
        let clientes = response as Cliente[];
        return clientes.map(cliente => {

          //registerLocaleData(localeES,'es');

          cliente.nombre = cliente.nombre.toUpperCase();
          //cliente.createAt = formatDate(cliente.createAt,'dd-MM-yyyy', 'en-US');
          let datePipe = new DatePipe('es');
          //cliente.createAt = datePipe.transform(cliente.createAt,"dd/MM/yyyy");
          //cliente.createAt = datePipe.transform(cliente.createAt,'EEEE dd, MMMM yyyy');
          return cliente;
        });
      }
      )
    );

  }

  create (cliente : Cliente): Observable<Cliente>{

    return this.httpClient.post<Cliente>(this.urlEndPoint,cliente, {headers : this.httpHeaders}).pipe(

      catchError( e=> {

        // los errores provenientes del backend deben ser
        // manejados de forma distinta
        if (e.status == 400){

          return throwError(e);

        }
        console.log (e.error.mensaje);
        swal.fire('Error al crear cliente', e.error.mensaje, 'error');
        return throwError(e);
      })
    );

  }

  getCliente(id:number): Observable<Cliente>{

    return this.httpClient.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(

      catchError(e => {

        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        swal.fire('Error al editar', e.error.mensaje, 'error');
        return throwError(e);

      })


    );

  }

  updateCliente(cliente : Cliente): Observable<Cliente>{

    return this.httpClient.put<Cliente>(`${this.urlEndPoint}/${cliente.id}`, cliente, {headers : this.httpHeaders})
    .pipe(

      catchError ( e => {

        if (e.status==400){

          return throwError(e);
        }

        console.error(e.error.mensaje);
        swal.fire('Error al editar', e.error.mensaje,'error');
        return throwError(e);
      })
    );

  }

  deleteCliente(id:number):Observable<Cliente>{

    return this.httpClient.delete<Cliente>(`${this.urlEndPoint}/${id}`,{headers : this.httpHeaders});

  }

}
