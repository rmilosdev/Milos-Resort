package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Role;
import com.milosdevelopmetn.model.Room;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.service.ReservationService;
import com.milosdevelopmetn.service.RoleService;
import com.milosdevelopmetn.service.RoomService;
import com.milosdevelopmetn.service.UserService;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.mysql.cj.xdevapi.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/admin/users/list")
    public String usersList(ModelMap modelMap){
        List<User> users = userService.findAll();
        Collections.sort(users);
        modelMap.addAttribute("users", users);
        return "admin/users/list";
    }

    @RequestMapping(value = "/admin/user/{id}/delete/")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes){
        User user = userService.findById(id);
        if(!user.getReservations().isEmpty()){
            for(Reservation reservation : user.getReservations()){
                Room room = reservation.getRoom();
                room.setFree(false);
                room.setReservation(null);
                reservationService.delete(reservation);
            }
        }
        userService.deleteUser(user);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno obrisan korisnik.", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/users/list";
    }

    @RequestMapping("/admin/user/{id}/update/")
    public String updateUserForm(@PathVariable Long id, ModelMap modelMap){
        User user = userService.findById(id);
        List<Role> roles = roleService.findAll();

        modelMap.addAttribute("roles", roles);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("action", String.format("/admin/users/%s/update", user.getId()));
        return "admin/users/update";
    }

    @RequestMapping(value = "/admin/users/{id}/update", method = RequestMethod.POST)
    public String updateUser(User user, RedirectAttributes redirectAttributes){

        userService.save(user);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Uspesno izmenjeni podaci.", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/users/list";
    }

    @RequestMapping(value = "/admin/user/{userId}/reservations/", method = RequestMethod.GET)
    public String userReservations(@PathVariable Long userId, ModelMap modelMap, RedirectAttributes redirectAttributes){

        User user = userService.findById(userId);
        boolean hasCheckedReservations = false;

        for(Reservation res : user.getReservations()){
            if (res.getArrived()){
                hasCheckedReservations = true;
            }
        }

        modelMap.addAttribute("user", user);
        modelMap.addAttribute("hasCheckedReservations", hasCheckedReservations);

        return "admin/users/reservations";
    }

    @RequestMapping(value = "/admin/user/{userId}/reservation/{reservationId}/delete")
    public String deleteReservation(@PathVariable Long userId, @PathVariable Long reservationId){

        Reservation reservation = reservationService.findById(reservationId);
        Room room = roomService.findById(reservation.getRoom().getId());

        room.setReservation(null);
        room.setFree(false);
        reservationService.delete(reservation);

        return "redirect:/admin/user/{userId}/reservations/";
    }

    @RequestMapping(value = "/admin/user/{userId}/reservation/{reservationId}/check", method = RequestMethod.GET)
    public String checkReservation(@PathVariable Long userId, @PathVariable Long reservationId, RedirectAttributes redirectAttributes){

        Reservation reservation = reservationService.findById(reservationId);
        User user = userService.findById(userId);
        boolean hasCheckedReservations = false;

        reservation.setArrived(true);
        Calendar leavingDate = (Calendar)reservation.getDate().clone();
        leavingDate.add(Calendar.DAY_OF_MONTH, reservation.getNumbOfDays());
        reservation.setLeaving(leavingDate);
        reservationService.save(reservation);

        for(Reservation res : user.getReservations()){
            if (res.getArrived()){
                hasCheckedReservations = true;
            }
        }

        redirectAttributes.addAttribute("hasCheckedReservations", hasCheckedReservations);

        return "redirect:/admin/user/{userId}/reservations/";
    }

}