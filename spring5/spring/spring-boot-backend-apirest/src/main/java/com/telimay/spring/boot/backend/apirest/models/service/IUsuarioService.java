package com.telimay.spring.boot.backend.apirest.models.service;

import com.telimay.spring.boot.backend.apirest.models.entity.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername(String username);
	
}
