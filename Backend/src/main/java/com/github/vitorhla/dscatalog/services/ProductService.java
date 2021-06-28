package com.github.vitorhla.dscatalog.services;

import java.util.Arrays;
import java.util.List;
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
import com.github.vitorhla.dscatalog.dto.ProductDTO;
import com.github.vitorhla.dscatalog.entities.Category;
import com.github.vitorhla.dscatalog.entities.Product;
import com.github.vitorhla.dscatalog.repositories.CategoryRepository;
import com.github.vitorhla.dscatalog.repositories.ProductRepository;
import com.github.vitorhla.dscatalog.services.exceptions.ControllerNotFoundException;
import com.github.vitorhla.dscatalog.services.exceptions.DatabaseException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable){
		List<Category> categories = (categoryId == 0) ? null :
						Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> page  = productRepository.find(categories,name, pageable);
		productRepository.findProductsWithCategories(page.getContent());
		return page.map(x -> new ProductDTO(x, x.getCategories()));
			
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product>  obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
		return new ProductDTO(entity,entity.getCategories());
		
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity =  new Product();
		copyDtoToEntity(dto,entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		Product entity = productRepository.getOne(id);
		copyDtoToEntity(dto,entity);
		entity= productRepository.save(entity);
		return new ProductDTO(entity);
		}
		
		catch(EntityNotFoundException e) {
			
			throw new ControllerNotFoundException("Id not found" + id);
		}
		
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e ) {
			throw new ControllerNotFoundException("Id not found" + id);
		}
		catch (DataIntegrityViolationException e ) {
			throw new DatabaseException("Integrity violation");
			
		}
		
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
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
