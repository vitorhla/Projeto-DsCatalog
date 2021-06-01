package com.github.vitorhla.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.vitorhla.dscatalog.dto.CategoryDTO;
import com.github.vitorhla.dscatalog.dto.UserDTO;
import com.github.vitorhla.dscatalog.entities.Category;
import com.github.vitorhla.dscatalog.entities.User;
import com.github.vitorhla.dscatalog.repositories.CategoryRepository;
import com.github.vitorhla.dscatalog.repositories.UserRepository;
import com.github.vitorhla.dscatalog.services.exceptions.ControllerNotFoundException;
import com.github.vitorhla.dscatalog.services.exceptions.DatabaseException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
		Page<User> list  = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
			
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User>  obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
		return new UserDTO(entity,entity.getCategories());
		
	}

	@Transactional
	public UserDTO insert(UserDTO dto) {
		User entity =  new User();
		copyDtoToEntity(dto,entity);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
		User entity = repository.getOne(id);
		copyDtoToEntity(dto,entity);
		entity= repository.save(entity);
		return new UserDTO(entity);
		}
		
		catch(EntityNotFoundException e) {
			
			throw new ControllerNotFoundException("Id not found" + id);
		}
		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e ) {
			throw new ControllerNotFoundException("Id not found" + id);
		}
		catch (DataIntegrityViolationException e ) {
			throw new DatabaseException("Integrity violation");
			
		}
		
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}
	
	
	
	
	
}
