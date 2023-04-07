package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>  {
     List<ItemRequest> getAllByRequesterIdOrderByCreatedDesc(Long userId);

     @Query(value = "SELECT * FROM requests r WHERE r.requester_id != :requester_id ORDER BY created DESC",
             nativeQuery = true)
     List<ItemRequest> getAllNotOwnRequests(@Param("requester_id") long userId);

}
