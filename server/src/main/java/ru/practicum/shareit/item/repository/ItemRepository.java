package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
                     @Param("available") boolean available,
                     PageRequest pageRequest);

   @Query(value = "SELECT * FROM items i where i.user_id = :id ORDER BY i.id ASC ",
           nativeQuery = true)
   List<Item> findAllByOwner1(@Param("id") Long id, PageRequest pageRequest);

   @Query("SELECT i FROM Item i " +
           "WHERE i.owner.id = :userId " +
           "ORDER BY i.id")
   List<Item> findAllByOwnerId(Long userId, PageRequest pageRequest);

   List<Item> findAllByRequestId(Long requestId);

   List<Item> findAllByRequestIdIn(List<Long> requestIds);
}
