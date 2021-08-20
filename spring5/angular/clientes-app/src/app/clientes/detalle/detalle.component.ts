import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { ActivatedRoute } from '@angular/router';
import { HttpEventType } from '@angular/common/http';
import swal from 'sweetalert2';

@Component({
  selector: 'detalle-cliente',
  templateUrl: './detalle.component.html',
  styleUrls: ['./detalle.component.css']
})
export class DetalleComponent implements OnInit {

  cliente : Cliente;
  titulo : string = "Detalle Cliente";
  fotoSeleccionada : File;
  progreso : number = 0;

  constructor(private clienteService : ClienteService,
              private activatedRoute : ActivatedRoute) { }

  ngOnInit(): void {

    this.activatedRoute.paramMap.subscribe( params => {

      let id : number = +params.get('id');

      if (id){

        this.clienteService.getCliente(id).subscribe(cliente => {
          this.cliente = cliente;
        });
      }
    });
  }

  seleccionarFoto(event){

    this.fotoSeleccionada = event.target.files[0];
    console.log(this.fotoSeleccionada);

    // type no entrega el tipo de archivo
    // indexOf -> busca una coincidencia con image, si la encuentra
    // va a retornar la posicion donde la encontro. Si no entrega
    // valor -1
    // type:"image/jpeg", como ejemplo
    if (this.fotoSeleccionada.type.indexOf('image')<0){

      swal.fire('Error Seleccion','Debe seleccionar un archivo de imagen','error');
      this.fotoSeleccionada = null;

    }

  }

  subirFoto(){

    if (!this.fotoSeleccionada){

      swal.fire('Error Upload','Debe seleccionar una foto','error');

    }else{

      this.clienteService.subirFoto(this.fotoSeleccionada, this.cliente.id)
        .subscribe( event => {

            if (event.type === HttpEventType.UploadProgress){

              this.progreso = Math.round((100 * event.loaded / event.total));
            }

            if (event.type === HttpEventType.Response){

              let response : any = event.body;

              this.cliente = response.cliente as Cliente;

              //swal.fire('La foto se ha subido completamente !!',`La carga ha sido exitosa : ${this.cliente.foto}`, 'success');
              swal.fire('La foto se ha subido completamente !!',response.mensaje, 'success');

            }

          }
        );

      // this.clienteService.subirFoto(this.fotoSeleccionada, this.cliente.id)
      //   .subscribe(cliente => {
      //     this.cliente = cliente;
      //     swal.fire('La foto se ha subido completamente !!',`La carga ha sido exitosa : ${this.cliente.foto}`, 'success');
      //   });

    }

  }

}
