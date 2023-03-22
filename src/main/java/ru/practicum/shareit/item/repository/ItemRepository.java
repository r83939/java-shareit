package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

   @Query(value = "SELECT i.user_id FROM items i where i.id = :id",
           nativeQuery = true)
   long findUserIdById(@Param("id") Long id);

   @Query(value = "SELECT * FROM items i " +
           "WHERE i.available = :available " +
           "AND (LOWER(i.name) like LOWER(concat('%', concat(:text, '%'))) OR LOWER(i.description) like LOWER(concat('%', concat(:text, '%')))) ",
           nativeQuery = true)
   List<Item> search(@Param("text") String text,
                     @Param("available") boolean available);


   List<Item> findAllByOwner(@Param("id") Long id);



}
