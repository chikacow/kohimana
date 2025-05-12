package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.util.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * from tbl_order where seat_id = :seat_id", nativeQuery = true)
    public List<Order> findBySeatId(@Param("seat_id") Long seatId);

    /**
     * problem with enum be treated as string
     * @param statuses
     * @return
     */
    @Query(value = "SELECT * FROM tbl_order WHERE status IN (:statuses)", nativeQuery = true)
    public List<Order> findByOrderStatus(@Param("statuses") List<OrderStatus> statuses);


    List<Order> findByStatusIn(List<OrderStatus> statuses);

}
