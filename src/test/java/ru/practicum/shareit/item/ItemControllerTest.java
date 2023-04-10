package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceImpl itemServiceImpl;
    @Test
    void getItem() {
    }

    @Test
    void getAllItem() {
    }

    @Test
    void createItem() {
    }

    @Test
    @SneakyThrows
    void updateItem() {
        Long itemId = 0L;
        ItemRequestDto itemToUpdate = new ItemRequestDto();
        itemToUpdate.setAvailable(null);

       mockMvc.perform(put("/items/{id}", itemId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isMethodNotAllowed());

        verify(itemServiceImpl, never()).updateItem(itemId, itemToUpdate);
    }

    @Test
    void deleteItem() {
    }

    @Test
    void searchItems() {
    }

    @Test
    void addComment() {
    }
}