package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.HotelValidator;
import com.milosdevelopmetn.Star;
import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.service.HotelService;
import com.milosdevelopmetn.service.ReservationService;
import com.milosdevelopmetn.service.RoomService;
import com.milosdevelopmetn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@SuppressWarnings("Duplicates")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @InitBinder("hotel")
    protected void initBinder(WebDataBinder binder){
        binder.setValidator(new HotelValidator());
    }


    @RequestMapping(value = "/admin/hotels/form", method = RequestMethod.GET)
    public String addHotelForm(ModelMap modelMap){
        if(!modelMap.containsAttribute("hotel")){
            modelMap.addAttribute("hotel", new Hotel());
        }
        List<User> freeManagers = new ArrayList<>();
        List<User> users =  userService.findAll();

        for(User user : users){
            if(user.getRole().getId() == 2){
                if(user.getHotel() == null){
                    freeManagers.add(user);
                }
            }
        }

        modelMap.addAttribute("managers", freeManagers);
        modelMap.addAttribute("action", "/admin/hotels/add");
        modelMap.addAttribute("h1", "Dodaj nov hotel");
        modelMap.addAttribute("submit", "Dodaj");
        modelMap.addAttribute("stars", Star.values());
        return "admin/hotels/form";
    }

    @RequestMapping(value = "/admin/hotels/add", method = RequestMethod.POST)
    public String addHotel(@Valid Hotel hotel, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.hotel", bindingResult);
            redirectAttributes.addFlashAttribute("hotel", hotel);
            return "redirect:/admin/hotels/form";
        }

        hotelService.save(hotel, hotel.getFile());

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno dodat hotel!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/hotels";
    }

    @RequestMapping("/admin/hotel/{id}/delete/")
    public String deleteHotel(@PathVariable Long id){
        Hotel hotel = hotelService.findById(id);

        List<Room> rooms = hotel.getRooms();

        for (Room room : rooms){
           if(room.isFree()){
               Reservation reservation = room.getReservation();
               reservationService.delete(reservation);

               room.setReservation(null);
           }

           roomService.delete(room);

        }

        hotelService.delete(hotel);

        return "redirect:/admin/hotels/list";
    }

    @RequestMapping("/admin/hotel/{id}/update/")
    public String updateHotelForm(@PathVariable Long id, ModelMap modelMap){

        Hotel hotel = hotelService.findById(id);

        List<User> freeManagers = new ArrayList<>();
        List<User> users =  userService.findAll();

        for(User user : users){
            if(user.getRole().getId() == 2){
                if(user.getHotel() == null){
                    freeManagers.add(user);
                }
            }
        }

        modelMap.addAttribute("managers", freeManagers);
        modelMap.addAttribute("action", String.format("/admin/hotel/%s/update/",id));
        modelMap.addAttribute("h1", "Azuriraj hotel");
        modelMap.addAttribute("submit", "Azuriraj");
        modelMap.addAttribute("hotel", hotel);
        modelMap.addAttribute("stars", Star.values());
        return "admin/hotels/form";
    }

    @RequestMapping(value = "/admin/hotel/{id}/update/", method = RequestMethod.POST)
    public String updateHotel(@Valid Hotel hotel, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.hotel", bindingResult);
            redirectAttributes.addFlashAttribute("hotel", hotel);
            return "redirect:/admin/hotels/form";
        }

        hotelService.save(hotel, hotel.getFile());
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno azuriran hotel!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/hotels/list";
    }

    @RequestMapping(value = "/admin/hotels/list")
    public String hotelList(ModelMap modelMap){
        List<Hotel> hotels = hotelService.findAll();
        modelMap.addAttribute("hotels", hotels);
        return "admin/hotels/list";
    }


    @RequestMapping(value = "hotel/{hotelName}/more")
    public String hotelInfo(@PathVariable String hotelName, ModelMap modelMap){

        Hotel hotel = hotelService.findByName(hotelName);
        if(!modelMap.containsAttribute("reservation")){
            modelMap.addAttribute("reservation", new Reservation());
        }
        List<Room> roomsForReturn =  hotel.getRooms();
        List<Room> roomList = new ArrayList<>();
        for(Room room : roomsForReturn){
            if(!room.isFree()){
                roomList.add(room);
            }
        }
//        Pageable firstPageWithTenElements = PageRequest.of(page, 3, Sort.by("roomType").ascending());




        modelMap.addAttribute("hotel", hotel);
        modelMap.addAttribute("freeRooms", roomList);

        return "hotelPage";
    }

//    @ResponseBody
//    @RequestMapping(value = "hotel/{hotelName}/more/{pageno}")
//    public Page<Room> getFreeRooms(@PathVariable("hotelName") String hotelName, @PathVariable("pageno") int pageno){
//        //Dodati filter po hotelu
//        Page<Room> processedRooms = roomService.findAll(new PageRequest(pageno, 10));
//        List<Room> modifiedList = processedRooms.getContent();
//        List<Room> newList = new ArrayList<>();
//
//        for ( Room room : modifiedList) {
//            if(room.getHotel().getName().equals(hotelName) && !room.isFree()){
//                newList.add(room);
//            }
//        }
//
//        Page<Room> newApplicationsPage = new PageImpl<Room>(newList, new PageRequest(pageno, 10), newList.size());
//        return  newApplicationsPage;
//    }

}