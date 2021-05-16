package com.github.vitorhla.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.vitorhla.dscatalog.dto.CategoryDTO;
import com.github.vitorhla.dscatalog.entities.Category;
import com.github.vitorhla.dscatalog.repositories.CategoryRepository;
import com.github.vitorhla.dscatalog.services.exceptions.ControllerNotFoundException;
import com.github.vitorhla.dscatalog.services.exceptions.DatabaseException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list  = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
			
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category>  obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
		
		
		
		
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity =  new Category();
		entity.setName(dto.getName());
		entity = categoryRepository.save(entity);
		
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = categoryRepository.getOne(id);
		entity.setName(dto.getName());
		entity= categoryRepository.save(entity);
		return new CategoryDTO(entity);
		}
		
		catch(EntityNotFoundException e) {
			
			throw new ControllerNotFoundException("Id not found" + id);
		}
		
	}

	public void delete(Long id) {
		try {
		categoryRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e ) {
			throw new ControllerNotFoundException("Id not found" + id);
		}
		catch (DataIntegrityViolationException e ) {
			throw new DatabaseException("Integrity violation");
			
		}
		
	}
	
	
	
	
	
	
	
}
