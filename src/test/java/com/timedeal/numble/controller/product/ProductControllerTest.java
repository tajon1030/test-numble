package com.timedeal.numble.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.entity.UserRole;
import com.timedeal.numble.service.ProductService;
import com.timedeal.numble.vo.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @Test
    public void 상품등록() throws Exception {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .name("test")
                .amount(1L)
                .description("test")
                .regularPrice(new Money("1000"))
                .salePrice(new Money("800"))
                .saleStartDateTime(LocalDateTime.of(2023,3,15,0,0))
                .saleEndDateTime(LocalDateTime.of(2023,3,16,0,0))
                .build();

        Product product = Product.fromProductEntity(addProductRequest.toProductEntity());

        when(productService.addProduct(addProductRequest))
                .thenReturn(product);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(addProductRequest))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품등록시_관리자가_아닐경우_에러반환() throws Exception {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .name("test")
                .amount(1L)
                .description("test")
                .regularPrice(new Money("1000"))
                .salePrice(new Money("800"))
                .saleStartDateTime(LocalDateTime.of(2023,3,15,0,0))
                .saleEndDateTime(LocalDateTime.of(2023,3,16,0,0))
                .build();

        Product product = Product.fromProductEntity(addProductRequest.toProductEntity());

        when(productService.addProduct(addProductRequest))
                .thenReturn(product);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.MEMBER);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(addProductRequest))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 상품수정() throws Exception {
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("test")
                .amount(1L)
                .description("test")
                .regularPrice(new Money("1000"))
                .salePrice(new Money("800"))
                .saleStartDateTime(LocalDateTime.of(2023,3,15,0,0))
                .saleEndDateTime(LocalDateTime.of(2023,3,16,0,0))
                .build();
        ModifyProductRequest modifyProductRequest = ModifyProductRequest.builder()
                .name("modify")
                .build();
        Product product = Product.fromProductEntity(
                productEntity.update(modifyProductRequest.getName(),
                        null,null,null,null,null,null));

        when(productService.modifyProduct(productEntity.getId(), modifyProductRequest))
                .thenReturn(product);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(modifyProductRequest))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품수정시_관리자가_아닐경우_에러반환() throws Exception {
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("test")
                .amount(1L)
                .description("test")
                .regularPrice(new Money("1000"))
                .salePrice(new Money("800"))
                .saleStartDateTime(LocalDateTime.of(2023,3,15,0,0))
                .saleEndDateTime(LocalDateTime.of(2023,3,16,0,0))
                .build();
        ModifyProductRequest modifyProductRequest = ModifyProductRequest.builder()
                .name("modify")
                .build();
        Product product = Product.fromProductEntity(
                productEntity.update(modifyProductRequest.getName(),
                        null,null,null,null,null,null));

        when(productService.modifyProduct(productEntity.getId(), modifyProductRequest))
                .thenReturn(product);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.MEMBER);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(modifyProductRequest))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 상품수정시_존재하지않는_Id로_요청할경우_에러반환() throws Exception {
        ModifyProductRequest modifyProductRequest = ModifyProductRequest.builder()
                .name("modify")
                .build();

        when(productService.modifyProduct(1L, modifyProductRequest))
                .thenThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(modifyProductRequest))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 상품삭제() throws Exception {
        doNothing().when(productService).removeProduct(1L);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void 상품삭제시_관리자가_아닐경우_에러반환() throws Exception {
        doNothing().when(productService).removeProduct(1L);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.MEMBER);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 상품삭제시_존재하지않는_Id로_요청할경우_에러반환() throws Exception {
        doThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
                .when(productService).removeProduct(1L);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 상품조회() throws Exception {
        Long productId = 1L;
        Product product =  Product.fromProductEntity(
                ProductEntity.builder()
                        .id(productId)
                        .name("test")
                        .amount(1L)
                        .description("test")
                        .regularPrice(new Money("1000"))
                        .salePrice(new Money("800"))
                        .saleStartDateTime(LocalDateTime.of(2023,3,15,0,0))
                        .saleEndDateTime(LocalDateTime.of(2023,3,16,0,0))
                        .build());

        when(productService.getProduct(productId))
                .thenReturn(product);

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(product)));
    }

    @Test
    public void 상품조회시_존재하지않는_Id로_조회할경우_에러반환() throws Exception {
        when(productService.getProduct(1L))
                .thenThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 상품목록조회() throws Exception {
        when(productService.getProducts(any()))
                .thenReturn(Page.empty());

        MockHttpSession mockHttpSession = new MockHttpSession();
        User user = new User("loginId","userName", UserRole.ADMIN);
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(get("/api/products")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").exists());
    }
}