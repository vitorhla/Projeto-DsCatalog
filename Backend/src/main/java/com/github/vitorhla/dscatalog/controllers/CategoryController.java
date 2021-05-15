package com.github.vitorhla.dscatalog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.vitorhla.dscatalog.dto.CategoryDTO;
import com.github.vitorhla.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value= "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list =  categoryService.findAll();	
		return ResponseEntity.ok().body(list);
		
	}
	
	@GetMapping(value =  "/{id}")
	public ResponseEntity<CategoryDTO>findById(@PathVariable Long id){
		CategoryDTO dto =  categoryService.findById(id);	
		return ResponseEntity.ok().body(dto);
		
	}
	
	

}
