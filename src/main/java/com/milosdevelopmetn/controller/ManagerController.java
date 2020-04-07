package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.Star;
import com.milosdevelopmetn.model.*;
import com.milosdevelopmetn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Controller
public class ManagerController extends BaseMethodsController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @RequestMapping("/manager/manager/{hotelId}/room/add")
    private String formAddRoom(@PathVariable Long hotelId, ModelMap modelMap){
        List<RoomType> roomTypes = roomTypeService.findAll();
        Hotel hotel = hotelService.findById(hotelId);

        modelMap.addAttribute("roomTypes", roomTypes);
        modelMap.addAttribute("returnedHotel", hotel);
        modelMap.addAttribute("room", new Room());
        return "manager/room/add";
    }

    @RequestMapping("/manager/manager/{hotelId}/room/delete")
    private String formDeleteRoom(@PathVariable Long hotelId, ModelMap modelMap){
        Hotel hotel = hotelService.findById(hotelId);
        List<Room> hotelRooms = hotel.getRooms();

        modelMap.addAttribute("rooms", hotelRooms);
        return "manager/room/delete";
    }

    @RequestMapping("/manager/manager/{hotelId}/room/update")
    private String formUpdateRoom(@PathVariable Long hotelId, ModelMap modelMap){
        Hotel hotel = hotelService.findById(hotelId);
        List<Room> hotelRooms = hotel.getRooms();
        List<RoomType> roomTypes = roomTypeService.findAll();

        modelMap.addAttribute("rooms", hotelRooms);
        modelMap.addAttribute("roomTypes", roomTypes);
        modelMap.addAttribute("room", new Room());
        return "manager/room/update";
    }

    @RequestMapping(value = "/manager/manager/{hotelId}/hotel/update")
    private String formUpdateHotel(@PathVariable Long hotelId, ModelMap modelMap){
        Hotel hotel = hotelService.findById(hotelId);

        modelMap.addAttribute("action", String.format("/manager/manager/%s/hotel/update/", hotel.getId()));
        modelMap.addAttribute("h1", "Izmeni podatke hotela");
        modelMap.addAttribute("submit", "Izmeni");
        modelMap.addAttribute("hotel", hotel);
        modelMap.addAttribute("stars", Star.values());
        return "manager/hotel/update";
    }

    @RequestMapping(value = "/manager/manager/{id}/hotel/update/", method = RequestMethod.POST)
    private String updateHotelManager(@Valid Hotel hotel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.hotel", bindingResult);
            redirectAttributes.addFlashAttribute("hotel", hotel);
            return "redirect:/manager/manager/"+hotel.getId()+"/hotel/update/";
        }
        hotelService.save(hotel, hotel.getFile());
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno azuriran hotel!", FlashMessage.Status.SUCCESS));
        return "redirect:/manager/index";
    }

    @RequestMapping(value = "/manager/manager/room/add", method = RequestMethod.POST)
    private String addRoomManager(@Valid Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request){
        String passedId = request.getParameter("hotelId");
        Hotel hotel = hotelService.findById(Long.parseLong(passedId));
        room.setHotel(hotel);
        return addNewRoomBase(room, bindingResult, redirectAttributes, "redirect:/manager/index");
    }

    @RequestMapping(value = "/manager/manager/room/update", method = RequestMethod.POST)
    private String updateRoomManager(@RequestParam("roomId") Long roomId,
                                @RequestParam("updateRoomType") Long updateRoomType,
                                @RequestParam("updateRoomStatus") boolean updateRoomStatus,
                                RedirectAttributes redirectAttributes){
        return updateRoomBase(roomId, updateRoomType, updateRoomStatus, redirectAttributes, "redirect:/manager/index");
    }

    @RequestMapping(value = "manager/manager/room/{roomId}/delete")
    private String deleteRoomManager(@PathVariable Long roomId, RedirectAttributes redirectAttributes){
        try{
            Room room = roomService.findById(roomId);
            Reservation reservation = room.getReservation();
            if (reservation != null){
                reservationService.delete(reservation);
            }
            roomService.delete(room);

            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno obrisana soba", FlashMessage.Status.SUCCESS));
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Doslo je do greske prilikom brisanja sobe", FlashMessage.Status.FAILURE));
        }
        //        return deleteRoomBase(request, redirectAttributes, "");
        return "redirect:/manager/index";
    }
}
