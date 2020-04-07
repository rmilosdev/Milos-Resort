package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.model.*;
import com.milosdevelopmetn.service.*;
import javassist.compiler.Parser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Controller
public class RoomController extends BaseMethodsController{

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin/roomtype/add", method = RequestMethod.POST)
    private String addNewType(@Valid RoomType roomtype, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roomType", bindingResult);
            redirectAttributes.addFlashAttribute("roomtype", roomtype);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Greska prilikom dodavanja novog tipa, proverite da li ste popunili sva polja!", FlashMessage.Status.FAILURE));

            return "redirect:/admin/rooms";
        }
        roomTypeService.save(roomtype);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno dodat tip sobe!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/rooms";
    }

    @RequestMapping(value = "/admin/room/add", method = RequestMethod.POST)
    private String addNewRoom(@Valid Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        return addNewRoomBase(room, bindingResult, redirectAttributes, "redirect:/admin/rooms");
    }

    @RequestMapping(value = "/admin/roomtype/update/types", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    private RoomType oneType(Long id){
        RoomType roomType = roomTypeService.findById(id);
        return roomType;
    }

    @RequestMapping(value = "/admin/room/showrooms", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    private List<Room> roomlist(String name){
        Hotel hotel = hotelService.findByName(name);
        List<Room> roomList = hotel.getRooms();
        return roomList;
    }

    @RequestMapping(value = "/admin/room/findRoom", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    private Room findRoom(Long roomId){
        Room room = roomService.findById(roomId);
        return room;
    }

    @RequestMapping(value = "/admin/room/update", method = RequestMethod.POST)
    private String updateRoom(@RequestParam("roomId") Long roomId,
                               @RequestParam("updateroomType") Long updateTypeId,
                               @RequestParam("updateroomFree") boolean updateRoomStatus,
                               RedirectAttributes redirectAttributes)
    {
        return updateRoomBase(roomId, updateTypeId, updateRoomStatus, redirectAttributes, "redirect:/admin/rooms");
    }

    @RequestMapping("/admin/room/delete")
    private String deleteRoom(HttpServletRequest request, RedirectAttributes redirectAttributes){
        return deleteRoomBase(request, redirectAttributes, "redirect:/admin/rooms");
    }

    //Preuzimamo ID selektovane sobe i prosledjujemo ga na kontroler za azuriranje podataka
//    @RequestMapping(value = "admin/room/getRoomId", method = RequestMethod.POST)
//    public @ResponseBody String getRoomId(@RequestBody Long roomId, RedirectAttributes redirectAttributes){
//        Room room = roomService.findById(roomId);
//
//        redirectAttributes.addFlashAttribute("selectedRoom", room);
//
//        return "redirect: admin/room/update";
//    }

//    @RequestMapping("admin/room/update")
//    private String updateSelectedRoom(@ModelAttribute("selectedRoom") final Room room ){
//        return null;
//    }

    @RequestMapping(value = "/admin/roomtype/update", method = RequestMethod.POST)
    private String updateType(@Valid RoomType roomType, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Greska prilikom azuriranja 'Tipa sobe', proverite da li ste popunili sva polja i pokusajte opet", FlashMessage.Status.FAILURE));
            return "redirect:/admin/rooms";
        }

        roomTypeService.save(roomType);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno azuriran 'Tip sobe'!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/rooms";
    }

//    @RequestMapping(value = "/admin/room/update", method = RequestMethod.POST)
//    private String updateRoom(JSONObject roomHotelName, Room room, RedirectAttributes redirectAttributes, HttpServletRequest request, ModelMap modelMap){
//
//        if(!modelMap.containsAttribute("room")){
//            modelMap.addAttribute("room", new Room());
//        }
//
//        RoomType roomType = roomTypeService.findById( Long.parseLong(request.getParameter("updateroomType")));
//        Hotel hotel = hotelService.findByName(roomHotelName.toString());
//
//        room.setFree(Boolean.parseBoolean(request.getParameter("updateroomFree")));
//        room.setId(Long.parseLong(request.getParameter("updateRoomId")));
//        room.setHotel(hotel);
//        room.setRoomType(roomType);
//
//        roomService.save(room);
//        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno azurirana soba!", FlashMessage.Status.SUCCESS));
//        return "redirect:/admin/rooms";
//    }

    @RequestMapping(value = "/admin/roomtype/delete", method = RequestMethod.POST)
    private String deleteType(HttpServletRequest request, RedirectAttributes redirectAttributes){
        Long roomTypeId = Long.parseLong(request.getParameter("typeForDelete"));
        RoomType roomType = roomTypeService.findById(roomTypeId);
        try{
            List<Room> rooms = roomService.findAll();
            for (Room room: rooms) {
                if(room.getRoomType().equals(roomType)){
                    if(room.getReservation() != null){
                        reservationService.delete(room.getReservation());
                    }
                    roomService.delete(room);
                }
            }
            roomTypeService.delete(roomType);
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Greska! Izaberite postojeci tip sobe za brisanje.", FlashMessage.Status.FAILURE));
            return "redirect:/admin/rooms";
        }

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno obrisan tip sobe.", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/rooms";
    }

    //Dodavanje rezervacije na sobe
    @RequestMapping(value = "hotel/{hotelName}/more/{roomId}/reserve", method = RequestMethod.POST)
    private String addReservation(@Valid Reservation reservation, @RequestParam("room_id") Long roomId, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes, HttpServletRequest request, Principal principal){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reservation", bindingResult);
            redirectAttributes.addFlashAttribute("reservation", reservation);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Greska prilikom dodavanja rezervacije, proverite  da li ste popunili sva polja!", FlashMessage.Status.FAILURE));
        }

        Room room = roomService.findById(new Long(roomId));
        User user = userService.findByUsername(principal.getName());

        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setArrived(false);
        reservation.setReservationDate(LocalDateTime.now());

        reservationService.save(reservation);

        Long reservationId = reservation.getId();

        roomService.addReservation(room, reservationId);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno rezervisana soba!", FlashMessage.Status.SUCCESS));

        //Redirect to previous page
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
