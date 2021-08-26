import { Injectable } from '@angular/core';
import { Usuario } from './usuario';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _usuario : Usuario;
  private _token   : string;


  constructor(private httpClient : HttpClient) { }

  public get usuario() : Usuario{

    if (this._usuario != null){

      return this._usuario;

    }

    if (sessionStorage.getItem('usuario')!=null){

      this._usuario = JSON.parse(sessionStorage.getItem('usuario')) as Usuario;

      return this._usuario;

    }

    return new Usuario(); // Un usuario vacio

  }

  public get token() : string {

    if (this._token != null){

      return this._token;

    }

    if (sessionStorage.getItem('token')!=null){

      this._token = sessionStorage.getItem('token');
      return this._token;

    }

    return null;

  }

  login(usuario : Usuario):Observable<any>{

    const urlEndPoint   = 'http://localhost:8080/oauth/token';
    const credenciales  = btoa('angularapp' + ':' + '12345');
    const httpHeaders   = new HttpHeaders({'Content-Type':'application/x-www-form-urlencoded',
                                           'Authorization' : 'Basic ' + credenciales});
    let params = new URLSearchParams();

    params.set('grant_type','password');
    params.set('username',usuario.username);
    params.set('password',usuario.password);

    console.log (params.toString());

    return this.httpClient.post<any>(urlEndPoint, params.toString(), {headers : httpHeaders});

  }

  guardarUsuario(accessToken : string):void{

    let payLoad = this.obtenerDatosToken(accessToken);

    this._usuario = new Usuario();

    this._usuario.nombre    = payLoad.nombre;
    this._usuario.apellido  = payLoad.apellido;
    this._usuario.email     = payLoad.email;
    this._usuario.username  = payLoad.user_name;
    this._usuario.roles     = payLoad.authorities;

    sessionStorage.setItem('usuario', JSON.stringify(this._usuario));

  }

  guardarToken(accessToken : string):void{

    this._token = accessToken;

    sessionStorage.setItem('token', this._token);


  }

  obtenerDatosToken(accessToken : string):any{

    if (accessToken!=null){

      return JSON.parse(atob(accessToken.split('.')[1]));
    }
    return null;

  }

  isAuthenticated():boolean {

    let payLoad = this.obtenerDatosToken(this.token);

    if (payLoad != null && payLoad.user_name && payLoad.user_name.length > 0){

        return true;

    }

    return false;

  }

  logout():void{

    this._usuario = null;
    this._token   = null;
    sessionStorage.clear();

    // Otra forma
    // sessionStorage.removeItem('usuario');
    // sessionStorage.removeItem('token');

  }

  hasRole(role : string) : boolean {

    // El include permite buscar si el rol de parametro esta en los roles del usuario
    if (this.usuario.roles.includes(role)){
      return true;
    }

    return false;
    
  }
}
