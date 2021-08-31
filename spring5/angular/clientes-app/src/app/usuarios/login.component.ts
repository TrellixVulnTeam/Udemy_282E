import { Component, OnInit } from '@angular/core';
import { Usuario } from './usuario';
import swal from 'sweetalert2';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  titulo : string = "Iniciar sesión";
  usuario : Usuario;

  constructor(private authService : AuthService,
              private router : Router) {

    this.usuario = new Usuario();

  }

  ngOnInit(): void {

    if (this.authService.isAuthenticated()){

      swal.fire('Login', `Usuario ${this.authService.usuario.username} esta autenticado `, 'info');
      this.router.navigate(['/clientes']);

    }
  }

  login():void{

    console.log(this.usuario);

    if (this.usuario.username == null || this.usuario.password == null){

      swal.fire('Error Login', ' Username o password no pueden ser nulos', 'error');
      return;

    }

    this.authService.login(this.usuario).subscribe( response => {
      console.log(response);
      //let payLoad = JSON.parse(atob(response.access_token.split('.')[1]));
      //console.log(payLoad);
      this.authService.guardarUsuario(response.access_token);
      this.authService.guardarToken(response.access_token);

      let usuario = this.authService.usuario;


      this.router.navigate(['/clientes']);

      swal.fire('Login', `Bienvenido ${usuario.nombre}`, 'success');

    }, err => {

      if (err.status == 400){

          swal.fire('Error Login', ' Credenciales de acceso incorrectas. ', 'error');

      }

    }
    );

  }

}
