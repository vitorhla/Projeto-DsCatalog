package com.github.vitorhla.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.github.vitorhla.dscatalog.entities.Product;
import com.github.vitorhla.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long nonExistingId;
	private long countTotalProjects;
	
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProjects = 25L;
	}
	
	
	
	
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProjects+1, product.getId());
		
	}
	
	
	@Test
	public void deletShouldDeleteObjectWhenIdExists() {

		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void deletShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistingId);
			
		});
	}
	
	
	
	
	
}
