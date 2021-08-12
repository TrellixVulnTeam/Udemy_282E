package com.telimay.spring.boot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.bind.annotation.RestController;

import com.telimay.spring.boot.backend.apirest.models.entity.Cliente;
import com.telimay.spring.boot.backend.apirest.models.service.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		
		return clienteService.findAll();
		
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
	

}
