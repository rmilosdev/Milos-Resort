package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Room;
import com.milosdevelopmetn.model.RoomType;
import com.milosdevelopmetn.service.RoomService;
import com.milosdevelopmetn.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public class BaseMethodsController {

    public BaseMethodsController(){}

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    protected String deleteRoomBase(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                    String redirectPath){
        try{
            Room room = roomService.findById(Long.parseLong(request.getParameter("roomId")));

            roomService.delete(room);

            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno obrisana soba.", FlashMessage.Status.SUCCESS));
            return redirectPath;
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(ex.toString(), FlashMessage.Status.FAILURE));
            return redirectPath;
        }
    }

    protected String addNewRoomBase(@Valid Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes, String redirectPath){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.room", bindingResult);
            redirectAttributes.addFlashAttribute("room", room);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Greska prilikom dodavanja nove sobe, proverite  da li ste popunili sva polja!", FlashMessage.Status.FAILURE));

            return redirectPath;
        }
        roomService.save(room);
        Hotel hotel = room.getHotel();
        hotel.getRooms().add(room);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno dodata soba!", FlashMessage.Status.SUCCESS));
        return redirectPath;
    }

    protected String updateRoomBase(Long roomId, Long updateTypeId, boolean updateRoomStatus, RedirectAttributes redirectAttributes, String redirectPath){
        Room room = roomService.findById(roomId);
        RoomType roomType = roomTypeService.findById(updateTypeId);

        room.setRoomType(roomType);
        room.setFree(updateRoomStatus);

        roomService.save(room);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno azurirana soba!", FlashMessage.Status.SUCCESS));
        return redirectPath;
    }
}
