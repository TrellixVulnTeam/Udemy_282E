import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})

export class RoleGuard implements CanActivate {

  constructor (private authService : AuthService,
               private router      : Router ){}

  canActivate(
    next:  ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

      let role = next.data['role'];

      console.log(role);

      if (!this.authService.isAuthenticated()){

        this.router.navigate(['/login']);
        return false;
      }

      if (this.authService.hasRole(role)){

        return true;

      }

      swal.fire('Acceso denegado', 'No tiene acceso al recurso','warning');
      this.router.navigate(['/clientes']);

      return false;
  }

}
