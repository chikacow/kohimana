package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.service.SeatService;
import com.chikacow.kohimana.util.enums.TableStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/seats")
@RequiredArgsConstructor
@Validated
@Slf4j(topic = "SEAT-CONTROLLER")
public class SeatController {

    private final SeatService seatService;

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
    public ResponseData<?> getSeatById(@PathVariable Long id) {
        var res = seatService.getSeatInfo(id);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();

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
    public ResponseData<?> getSeatsByStatus(@PathVariable String status) {
        var res = seatService.getSeatsByStatus(status);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();

    }

    /**
     *  {
     *     "tableNo": "Z10",
     *     "description": "Bàn số 1 khu vực A",
     *     "status": "AVAILABLE"
     *   }
     * @param requestDTO
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseData<?> createSeat(@Valid @RequestBody Seat.SeatRequestDTO requestDTO) {
        Seat.SeatResponseDTO res = seatService.createSeat(requestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseData<?> updateSeat(@PathVariable Long id, @Valid @RequestBody Seat.SeatRequestDTO seatDetails) {
        var res = seatService.updateSeat(id, seatDetails);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();

    }

    /**
     * http://localhost:9091/api/v1/admin/seats/1/status?status=CLEANING
     * @param id
     * @param status
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Seat> updateSeatStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        return ResponseEntity.ok(seatService.updateSeatStatus(id, status));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/createBatch")
    public ResponseData<?> createBatchSeat(@Valid @RequestBody List<Seat.SeatRequestDTO> requestDTOS) {
        List<Seat.SeatResponseDTO> res = seatService.createBatchSeat(requestDTOS);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }
}
