package com.telimay.spring.boot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.telimay.spring.boot.backend.apirest.models.entity.Cliente;
import com.telimay.spring.boot.backend.apirest.models.entity.Region;
import com.telimay.spring.boot.backend.apirest.models.service.IClienteService;
import com.telimay.spring.boot.backend.apirest.models.service.IUploadFileService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	IClienteService clienteService;
	
	@Autowired
	IUploadFileService uploadFileService;
	
	private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);
	
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		
		return clienteService.findAll();
		
	}
	
	// Implementacion de paginacion
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page){
		
		Pageable pageable = PageRequest.of(page,4);
		return clienteService.findAll(pageable);
		
		// Otra forma
		//return clienteService.findAll(PageRequest.of(page, 4));
		
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			Cliente clienteResult = clienteService.findById(id);
					
			
			if (clienteResult==null) {
				
				response.put("mensaje", "Id cliente no encontrado");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				
			}
			
			return new ResponseEntity<Cliente>(clienteResult, HttpStatus.OK);
			
			
		}catch(DataAccessException e) {
			
			response.put("mensaje", "No se pudo obtener el cliente");
			response.put("error", e.getMessage().concat(": ")
					              .concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
//	public Cliente show(@PathVariable Long id ) {
//		
//		return clienteService.findById(id);
//	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?>create(@Valid @RequestBody Cliente cliente, BindingResult result){
		
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			
//			List<String> errors = new ArrayList<>();
//			
//			for (FieldError error : result.getFieldErrors()) {
//				
//				errors.add(error.getField() + ", " + error.getDefaultMessage());
//				
//			}
			
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getField() + ", " + err.getDefaultMessage()
					).collect(Collectors.toList());
			
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			Cliente clienteResult = clienteService.save(cliente);
			
			return new ResponseEntity<Cliente>(clienteResult, HttpStatus.CREATED);
			
		}catch(DataAccessException e) {
			
			response.put("mensaje", "No se pudo crear el cliente");
			response.put("error", e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cliente create(@RequestBody Cliente cliente) {
//		
//		return clienteService.save(cliente);
//		
//	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getField() + ", " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
			
		}
		
		Cliente clienteActual	= null;
		Cliente clienteUpdate	= null;
		
		try {
			
			clienteActual = clienteService.findById(id);
			
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setRegion(cliente.getRegion());
			
			clienteUpdate = clienteService.save(clienteActual);
						
		}catch(DataAccessException e) {
			
			response.put("mensaje", "No se pudo actualizar cliente");
			response.put("error", e.getMessage().concat(": ")
								.concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		

		response.put("mensaje", "Cliente actualizado !!!");
		response.put("objeto", clienteUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
		
	}
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
//		
//		Cliente clienteActual = clienteService.findById(id);
//		
//		clienteActual.setApellido(cliente.getApellido());
//		clienteActual.setEmail(cliente.getEmail());
//		clienteActual.setNombre(cliente.getNombre());
//		
//		return clienteService.save(clienteActual);
//		
//		
//	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			// este codigo proviene del upload de foto y lo colocamos
			// para eliminar la foto y no dejar la foto huerfana
			
			Cliente cliente = clienteService.findById(id);
			
			String nombreFotoAnterior = cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
//			if (nombreFotoAnterior!=null && nombreFotoAnterior.length()>0) {
//				
//				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
//				
//				File archivoFotoAnterior = rutaFotoAnterior.toFile();
//				
//				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
//					
//					archivoFotoAnterior.delete();
//				}
//				
//			}
			
			
			clienteService.delete(id);
			
		}catch(DataAccessException e) {
			
			response.put("mensaje", "No se pudo eliminar el cliente");
			response.put("error",e.getMessage().concat(": ")
					.concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje", "Cliente eliminado !!!");
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
		
	}
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void delete(@PathVariable Long id) {
//		
//		clienteService.delete(id);
//	}
	
	// Clase 95, subir archivos ( fotos )
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		if (!archivo.isEmpty()) {
			
			String nombreArchivo = null;
			
			try {
				
				nombreArchivo = uploadFileService.copiar(archivo);
				
				
			} catch (IOException e) {

				response.put("mensaje", "Error al subir la imagen ");
				//response.put("error",e.getMessage().concat(": ")
				//		.concat(e.getCause().getMessage()));
				
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				
			}
			
			// En la siguiente parte de código eliminamos la foto existente asociado
			// al cliente.
			
			String nombreFotoAnterior = cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
//			if (nombreFotoAnterior!=null && nombreFotoAnterior.length()>0) {
//				
//				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
//				
//				File archivoFotoAnterior = rutaFotoAnterior.toFile();
//				
//				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
//					
//					archivoFotoAnterior.delete();
//				}
//				
//			}
			
			cliente.setFoto(nombreArchivo);
			
			clienteService.save(cliente);
			
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen : " + nombreArchivo );
			
			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
		}

		response.put("mensaje", "Error al subir imagen " );
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}") // :.+ indica que viene con punto y una extension
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Resource recurso = null;
		
//		Path rutaFoto = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
//		Resource recurso = null;
//		
//		log.info(nombreFoto.toString());
//		
//		try {
//			
//			recurso = new UrlResource(rutaFoto.toUri());
//			
//		} catch (MalformedURLException e) {
//			
//			e.printStackTrace();
//		}
//		
//		if (!recurso.exists() && !recurso.isReadable()) {
//			
//			
//			
//			//throw new RuntimeException("Error, no se pudo cargar la imagen " + nombreFoto);
//			
//			rutaFoto = Paths.get("src/main/resources/static/images").resolve("sin_foto.jpg").toAbsolutePath();
//			
//			try {
//				
//				recurso = new UrlResource(rutaFoto.toUri());
//				
//			} catch (MalformedURLException e) {
//				
//				e.printStackTrace();
//			}
//			
//			log.error("Error, no se pudo cargar la imagen " + nombreFoto);
//			
//		}
		
	
		try {
			
			recurso = uploadFileService.cargar(nombreFoto);
			
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
		// agregamos una cabecera para que el recurso lo forcemos
		// para que pueda descargarse
		HttpHeaders cabecera = new HttpHeaders();
		
		//cabecera.add("Content-Disposition", nombreFoto)
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
		
	}
	
	@GetMapping("/clientes/regiones")
	public List<Region> listarRegiones(){
		
		return clienteService.findAllRegiones();
		
	}
}
