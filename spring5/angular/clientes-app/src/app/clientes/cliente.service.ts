import { Injectable } from '@angular/core';
import { formatDate, DatePipe } from '@angular/common';
import localeES from '@angular/common/locales/es';
import { Cliente } from './cliente';
import { Region } from './region';

import { Observable, throwError} from 'rxjs';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { AuthService } from '../usuarios/auth.service';

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
               private router : Router,
               private authService : AuthService){

    this.httpHeaders = new HttpHeaders().set('Content-Type','application/json');

  }

  private isNoAutorizado(e):boolean{

    if (e.status == 401 || e.status == 403){

      this.router.navigate(['/login']);
      return true;
    }

    return false;

  }

  private addAuthorizationHeader():HttpHeaders{

    let token = this.authService.token;

    if (token != null){

        return this.httpHeaders.append('Authorization','Bearer ' + token);

    }

    return this.httpHeaders;

  }

  getRegiones(): Observable<Region[]>{

    return this.httpClient.get<Region[]>(this.urlEndPoint + '/regiones',{headers : this.addAuthorizationHeader()}).pipe(
      catchError(e => {
                        this.isNoAutorizado(e);
                        return throwError(e);
                      }
                )
    );

  }

  // Lo cambiamos porque ahora no receibimos un formato
  // Cliente. Recibimos un formato generico
  //getClientes(): Observable <Cliente[]> {
  getClientes(page:number): Observable <any>{

    //return this.httpClient.get<Cliente>(urlEndPoint);
    // return this.httpClient.get(this.urlEndPoint).pipe( clase 82
    return this.httpClient.get(this.urlEndPoint + '/page/' +  page).pipe(
      tap( (response:any) => {
        //let clientes = response as Cliente[];

        //clientes.forEach( cliente => {
        (response.content as Cliente[]).forEach( cliente => {
          console.log (cliente.nombre);
        })

      }),
      map((response:any) => {

        //let clientes = response as Cliente[]; Clase 82

        //return clientes.map(cliente => { Clase 82
        (response.content as Cliente[]).map(cliente => {

          //registerLocaleData(localeES,'es');

          cliente.nombre = cliente.nombre.toUpperCase();
          //cliente.createAt = formatDate(cliente.createAt,'dd-MM-yyyy', 'en-US');
          let datePipe = new DatePipe('es');
          //cliente.createAt = datePipe.transform(cliente.createAt,"dd/MM/yyyy");
          //cliente.createAt = datePipe.transform(cliente.createAt,'EEEE dd, MMMM yyyy');
          return cliente;

        });
        return response;
      })
    );

  }

  create (cliente : Cliente): Observable<Cliente>{

    //return this.httpClient.post<Cliente>(this.urlEndPoint,cliente, {headers : this.httpHeaders}).pipe(
    return this.httpClient.post<Cliente>(this.urlEndPoint,cliente, {headers : this.addAuthorizationHeader()}).pipe(

      catchError( e=> {

        if (this.isNoAutorizado(e)){

          return throwError(e);

        }

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

    return this.httpClient.get<Cliente>(`${this.urlEndPoint}/${id}`,{headers : this.addAuthorizationHeader()}).pipe(

      catchError(e => {

        if (this.isNoAutorizado(e)){

          return throwError(e);

        }

        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        swal.fire('Error al editar', e.error.mensaje, 'error');
        return throwError(e);

      })


    );

  }

  updateCliente(cliente : Cliente): Observable<Cliente>{

    return this.httpClient.put<Cliente>(`${this.urlEndPoint}/${cliente.id}`, cliente, {headers : this.addAuthorizationHeader()})
    .pipe(

      catchError ( e => {

        if (this.isNoAutorizado(e)){

          return throwError(e);

        }

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

    return this.httpClient.delete<Cliente>(`${this.urlEndPoint}/${id}`,{headers : this.addAuthorizationHeader()}).pipe(
      catchError(e => {
        this.isNoAutorizado(e);
        return throwError(e);
      })
    );

  }

  //subirFoto(archivo: File, id):Observable<Cliente>{
  subirFoto(archivo: File, id):Observable<HttpEvent<{}>>{

    let formData = new FormData(); // Clase JavaScript Nativa buscar en fromdata google.

    // archivo -> mismo nombre que le pusimos en el backend : @RequestParam("archivo")
    formData.append("archivo", archivo);
    formData.append("id", id);

    console.log(archivo.name);
    console.log("id :" + id);

    let httpHeaders = new HttpHeaders();
    let token = this.authService.token;

    if (token!=null){

      httpHeaders = httpHeaders.append('Authorization','Bearer ' + token);

    }


    const req = new HttpRequest('POST',`${this.urlEndPoint}/upload`, formData,
    {
      reportProgress : true,
      headers : httpHeaders
    }
    );

    // El post igual que en BackEdn PortMapping("/clientes/upload")
    return this.httpClient.request(req).pipe(
      catchError ( e => {
        this.isNoAutorizado(e);
        return throwError(e);
      })
    );
    //.pipe(

    //  map((response : any) => response.cliente as Cliente),
    //  catchError(e => {

    //    console.error(e.error.mensaje);
    //    swal.fire('Error al editar', e.error.mensaje,'error');
    //    return throwError(e);
    //  })
    //);

  }



}
