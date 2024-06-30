package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.StaffRoomRequest;
import com.binhnd.pmsbe.dto.response.StaffRoomResponse;
import com.binhnd.pmsbe.dto.response.UpdateStaffInRoom;
import com.binhnd.pmsbe.entity.StaffRoom;
import com.binhnd.pmsbe.services.staff_room.StaffRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.binhnd.pmsbe.common.constants.RequestAction.STAFF_ROOM;
import static com.binhnd.pmsbe.common.constants.RequestAction.StaffRoom.FIND_STAFF_IN_ROOM;
import static com.binhnd.pmsbe.common.constants.RequestAction.StaffRoom.UPDATE_STAFF_ROOM;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + STAFF_ROOM)
public class StaffRoomController {

    private final Logger L = LoggerFactory.getLogger(StaffRoomController.class);

    private final StaffRoomService staffRoomService;

    public StaffRoomController(StaffRoomService staffRoomService) {
        this.staffRoomService = staffRoomService;
    }

    @PostMapping(value = UPDATE_STAFF_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStaffInRoom (@RequestParam Long id, @RequestBody StaffRoomRequest request) {
        L.info("[POST] {}: update staff in room", PMSConstants.PREFIX_URL + "/staff-room/update");
        staffRoomService.updateStaffInRoom(id, request);
        return ResponseEntity.ok().body("\"Update staff in room successfully!\"");
    }

    @GetMapping(value = FIND_STAFF_IN_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffRoomResponse> showStaffInRoom (@RequestParam Long id) {
        L.info("[GET] {}: find all staff in room", PMSConstants.PREFIX_URL + "/staff-room/find-staff-in-room?id=" + id);
        return ResponseEntity.ok().body(staffRoomService.showStaffInRoom(id));
    }

    @GetMapping(value = "/find-all-staff-in-room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateStaffInRoom> findAllStaffInRoom(@RequestParam Long id) {
        L.info("[GET] {}: find all staff in room", PMSConstants.PREFIX_URL + "/staff-room/find-all-staff-in-room?id=" + id);
        return ResponseEntity.ok().body(staffRoomService.findAllStaffInRoom(id));
    }
}
