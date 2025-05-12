package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.service.SearService;
import com.chikacow.kohimana.util.enums.TableStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SearService seatService;

    /**
     * Require paging
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    /**
     * unused
     * Case sensitive not required
     * @param tableNo
     * @return
     */
    //@GetMapping("/{tableNo}")
    public ResponseEntity<Seat> getSeatByTableNo(@PathVariable String tableNo) {
        return ResponseEntity.ok(seatService.getSeatByTableNo(tableNo));
    }

    /**
     * status can be AVAILABLE, OCCUPIED, RESERVED, CLEANING
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Seat>> getSeatsByStatus(@PathVariable TableStatus status) {
        return ResponseEntity.ok(seatService.getSeatsByStatus(status));
    }

    /**
     *  {
     *     "tableNo": "Z10",
     *     "description": "Bàn số 1 khu vực A",
     *     "status": "AVAILABLE"
     *   }
     * @param seat
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        Seat createdSeat = seatService.createSeat(seat);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(createdSeat.getId())
//                .toUri();
//        return ResponseEntity.created(location).body(createdSeat);

        return ResponseEntity.ok(createdSeat);
    }


    /**
     *  {
     *     "tableNo": "Z11",
     *     "description": "Bàn số 1 khu vực A",
     *     "status": "AVAILABLE"
     *   }
     * @param id
     * @param seatDetails
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id, @RequestBody Seat seatDetails) {
        return ResponseEntity.ok(seatService.updateSeat(id, seatDetails));
    }

    /**
     * http://localhost:9091/api/v1/admin/seats/1/status?status=CLEANING
     * @param id
     * @param status
     * @return
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Seat> updateSeatStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        return ResponseEntity.ok(seatService.updateSeatStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createBatch")
    public ResponseEntity<List<Seat>> createBatchSeat(@RequestBody List<Seat> seat) {
        List<Seat> createdSeat = seatService.createBatchSeat(seat);

        return ResponseEntity.ok(createdSeat);
    }
}
