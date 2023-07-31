package com.springapp.prod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapp.prod.dto.ProductRequest;
import com.springapp.prod.dto.ProductResponse;
import com.springapp.prod.model.Product;
import com.springapp.prod.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@Test
	public void testCreateProduct_Success() {

		ProductRequest prodReq = ProductRequest.builder()
				.name("Tosiba")
				.description("This is tosiba")
				.price(BigDecimal.valueOf(12000))
				.build();

	}

	@Test
	public void testGetAllProducts() throws Exception {
		// Mock the ProductService behavior
		List<Product> products = new ArrayList<>();
		products.add(Product.builder()
				.name("Tosiba1")
				.description("This is tosiba1")
				.price(BigDecimal.valueOf(12000))
				.build());
		products.add(Product.builder()
				.name("Tosiba2")
				.description("This is tosiba2")
				.price(BigDecimal.valueOf(13000))
				.build());
		when(productService.getAllProducts()).thenReturn(products.stream().map(product -> mapToProductResponse(product)).toList());

		// Perform the HTTP GET request
		ResultActions result = mockMvc.perform(get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the response
		result.andExpect(status().isOk());
	}



	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
}
