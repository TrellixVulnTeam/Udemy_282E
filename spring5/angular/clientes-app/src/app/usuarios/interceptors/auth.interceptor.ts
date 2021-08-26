import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

import swal from 'sweetalert2';


/** Pass untouched request through to the next request handler. */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService : AuthService,
              private router : Router ){}

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {

      return next.handle(req).pipe(
        catchError ( e=> {

          if (e.status == 401){
            // Implementamos este codigo por si el token ha caducado
            // si el token ha expirado entonces cerramos la session siempre
            // que aca este autenticado.
            if (this.authService.isAuthenticated()){

              // El usuario esta autenticado, por lo que cerramos la sesion por
              // expiracion de la sesion desde el BackEnd
              this.authService.logout();

            }

            this.router.navigate(['/login']);

          }

          if (e.status == 403){

            swal.fire('Acceso denegado', 'No tiene acceso al recurso','warning');
            this.router.navigate(['/clientes']);

          }

          return throwError(e);

        }
      )

    );

  }
}
