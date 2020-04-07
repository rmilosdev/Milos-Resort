package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.service.ReservationService;
import com.milosdevelopmetn.service.RoomService;
import com.milosdevelopmetn.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @RequestMapping("/account")
    public String accountInfo(Model model, Principal principal){

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        return "secured/account/profileInfo";
    }

    @RequestMapping("/account/reservations")
    public String accountReservations(Principal principal, ModelMap modelMap){

        User user = userService.findByUsername(principal.getName());
        List<Reservation> usersReservations = user.getReservations();

        modelMap.addAttribute("user", user);
        modelMap.addAttribute("reservations", usersReservations);

        return "secured/account/reservations";
    }

    //Otkazivanje rezervacije na profilu korisnika tj.brisanje rezervacije
    @RequestMapping(value = "account/reservations/{id}", method = RequestMethod.GET)
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes){

        Reservation reservation = reservationService.findById(id);
        Room room = reservation.getRoom();

        reservationService.delete(reservation);
        room.setFree(false);
        room.setReservation(null);
        roomService.save(room);

        redirectAttributes.addFlashAttribute( "flash", new FlashMessage("Rezervacija je uspesno otkazana.", FlashMessage.Status.SUCCESS));

        return "redirect:/account/reservations";
    }

    @RequestMapping("/account/{id}/edit/form")
    public String editAccountForm(ModelMap modelMap, @PathVariable Long id){
        if(!modelMap.containsAttribute("user")){
            User user = userService.findById(id);
            modelMap.addAttribute("user", user);
        }
        return "secured/account/edit";
    }


    @RequestMapping(value = "/account/{id}/edit", method = RequestMethod.POST)
    public String editAccount(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable Long id){

        if(bindingResult.hasErrors()){

            //include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);

            redirectAttributes.addFlashAttribute("user", userService.findById(id));


            return "redirect:/account/{id}/edit/form";
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Izmene uspesno sacuvane!", FlashMessage.Status.SUCCESS));
        return "redirect:/account";
    }


    @RequestMapping(value = "/account/{id}/updatePassword/form")
    public String changePasswordForm(@PathVariable Long id, Principal principal, ModelMap modelMap){

        if(!modelMap.containsAttribute("user")){
            User user = userService.findById(id);
            modelMap.addAttribute("user", user);
        }

        return "secured/account/updatePassword";
    }

    @RequestMapping(value = "/account/{id}/updatePassword", method = RequestMethod.POST)
    public String changePassword(@Valid User user, @PathVariable Long id, RedirectAttributes redirectAttributes, BindingResult bindingResult){
        if(bindingResult.hasErrors()){

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userService.findById(id));

            return "redirect:/account/{id}/updatePassword/form";
        }

        userService.updatePassword(user);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Nova sifra je uspesno sacuvana!", FlashMessage.Status.SUCCESS));
        return "redirect:/account";
    }
}
