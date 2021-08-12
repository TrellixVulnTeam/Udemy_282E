import { Component, OnInit } from '@angular/core';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  // Creado utilizando --flat para no crear el folder forme dentro de clientes
  public  cliente : Cliente = new Cliente();
  public  titulo  : string =" Crear cliente";

  public  errores : string[];

  constructor(private clienteService : ClienteService,
              private router : Router,
              private activatedRoute : ActivatedRoute) { }

  ngOnInit(): void {
    this.cargarCliente();
  }

  private cargarCliente() : void{

    this.activatedRoute.params.subscribe(
      params => {
        let id = params['id']
        if (id){
          this.clienteService.getCliente(id).subscribe(
            (cliente) => this.cliente = cliente
          )
        }
      }
    )

  }

  public create() : void {

    this.clienteService.create(this.cliente).
    subscribe( cliente => {
        this.router.navigate(['/clientes']),
        swal.fire('Nuevo Cliente',`Cliente ${cliente.nombre} creado con éxito`,'success')
      },
      err => {
        this.errores = err.error.errors as string[];
        console.error(" Codigo del error desde el backend :" + err.status);
        console.error(err.error.errors);
      }
    );
  }

  public update(): void{

    this.clienteService.updateCliente(this.cliente).
    subscribe (cliente => {
        this.router.navigate(['/clientes']),
        swal.fire('Cliente Actualizado',`Cliente ${cliente.nombre} ha sido actualizado con éxito`,'success')
      },
      err => {
        this.errores = err.error.errors as string[];
        console.error(" Codigo del error desde el backend :" + err.status);
        console.error(err.error.errors);
      }
    );

  }

}
