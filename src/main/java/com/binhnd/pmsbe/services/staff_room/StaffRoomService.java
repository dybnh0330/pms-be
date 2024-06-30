package com.binhnd.pmsbe.services.staff_room;

import com.binhnd.pmsbe.dto.request.StaffRoomRequest;
import com.binhnd.pmsbe.dto.response.StaffRoomResponse;
import com.binhnd.pmsbe.dto.response.UpdateStaffInRoom;
import com.binhnd.pmsbe.entity.StaffRoom;

public interface StaffRoomService {

    void updateStaffInRoom(Long id, StaffRoomRequest request);

    StaffRoomResponse showStaffInRoom(Long roomId);

    UpdateStaffInRoom findAllStaffInRoom(Long roomId);

}
