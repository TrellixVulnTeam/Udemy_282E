package com.telimay.spring.boot.backend.apirest.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

	private static final String DIRECTORIO_UPLOAD = "uploads";	

	@Override
	public Resource cargar(String nombreArchivo) throws MalformedURLException {

		Path rutaFoto = getPath(nombreArchivo) ;
		
		log.info(nombreArchivo.toString());
			
		Resource recurso = new UrlResource(rutaFoto.toUri());
		
		if (!recurso.exists() && !recurso.isReadable()) {
			
			rutaFoto = Paths.get("src/main/resources/static/images").resolve("sin_foto.jpg").toAbsolutePath();
				
			recurso = new UrlResource(rutaFoto.toUri());
			
		}	
		
		return recurso;		
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		
		// Podemos quitar los espacios con replace 
		// Y creamos un identificado unico con UUID
		//String nombreArchivo = UUID.randomUUID().toString() + "_" +  archivo.getOriginalFilename().replace(" ", "");
		String nombreArchivo = archivo.getOriginalFilename();
					
		//Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
		Path rutaArchivo = getPath(nombreArchivo);
					
		log.info(nombreArchivo.toString());
						
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
		
		
	}

	@Override
	public boolean eliminar(String nombreArchivo) {

		if (nombreArchivo!=null && nombreArchivo.length()>0) {
			
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			
			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				
				archivoFotoAnterior.delete();
				return true;
			}
			
		}
		
		return false;
	}

	@Override
	public Path getPath(String nombreArchivo) {
		
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreArchivo).toAbsolutePath();
		
	}
	
	

}
