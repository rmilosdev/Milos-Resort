package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Room;
import com.milosdevelopmetn.model.RoomType;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.service.HotelService;
import com.milosdevelopmetn.service.RoomService;
import com.milosdevelopmetn.service.RoomTypeService;
import com.milosdevelopmetn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home(ModelMap modelMap){
        List<Hotel> hotelList = hotelService.findAll();
        modelMap.addAttribute("hotelList", hotelList);
        return "home";
    }
    @RequestMapping("/home")
    public String index(ModelMap modelMap){
        List<Hotel> hotelList = hotelService.findAll();
        modelMap.addAttribute("hotelList", hotelList);
        return "home";
    }

    @RequestMapping("hotels/{hotelId}.jpeg")
    @ResponseBody
    public byte[] image(@PathVariable Long hotelId){
        return hotelService.findById(hotelId).getBytes();
    }

    @RequestMapping("admin/hotels")
    public String adminHotels(){
        return "admin/hotels/operations";
    }

    @RequestMapping("manager/index")
    public String managerIndex(ModelMap modelMap, HttpServletRequest request){
        User manager = userService.findByUsername(request.getUserPrincipal().getName());

        modelMap.addAttribute("manager", manager);
        return "manager/firstPage";
    }

    @RequestMapping(value = "/admin/users")
    public String adminUsers(){
        return "admin/users/operations";
    }

    @RequestMapping(value = "/admin/rooms", method = RequestMethod.GET)
    public String adminRooms(ModelMap modelMap){
        if(!modelMap.containsAttribute("roomtype")) {
            modelMap.addAttribute("roomtype", new RoomType());
        }
        if(!modelMap.containsAttribute("room")){
            modelMap.addAttribute("room", new Room());
        }
        List<Hotel> hotelList = hotelService.findAll();
        List<Room> rooms = roomService.findAll();
        List<RoomType> roomTypes = roomTypeService.findAll();

        modelMap.addAttribute("listTypes", roomTypes);
        modelMap.addAttribute("listHotels", hotelList);
        modelMap.addAttribute("rooms", rooms);
        return "admin/rooms/operations";
    }

}
