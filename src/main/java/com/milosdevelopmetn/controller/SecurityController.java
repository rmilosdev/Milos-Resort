package com.milosdevelopmetn.controller;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@SuppressWarnings("Duplicates")
public class SecurityController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
	public String loginPage(HttpServletRequest request, ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken))  {
			if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
				return "redirect:/admin/hotels";
			}
			return "redirect:/home";
		}
		try{
			Object flash = request.getSession().getAttribute("flash1");
			modelMap.addAttribute("flash", flash);

			request.getSession().removeAttribute("flash1");
		}catch (Exception ex){}

		return "login";
	}


	@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
	public String adminLogin(ModelMap modelMap, HttpServletRequest request){

		modelMap.addAttribute("admin", new User());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken))  {
			if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
				return "redirect:/admin/hotels";
			}
			return "redirect:/home";
		}

		try{
			Object flash1= request.getSession().getAttribute("flash2");
			modelMap.addAttribute("flash1", flash1);
			request.getSession().removeAttribute("flash2");
		}catch (Exception ex){}

		return "admin/login";
	}


	@RequestMapping(value = "/manager", method = {RequestMethod.GET, RequestMethod.POST})
	public String managerLogin(ModelMap modelMap, HttpServletRequest request){

		modelMap.addAttribute("manager", new User());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken))  {
			if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))){
				return "redirect:/manager/index";
			}
			return "redirect:/home";
		}

		try{
			Object flash1= request.getSession().getAttribute("flash1");
			modelMap.addAttribute("flash1", flash1);
			request.getSession().removeAttribute("flash1");
		}catch (Exception ex){}

		return "manager/login";
	}

	@RequestMapping("/register")
	public String registerForm(ModelMap modelMap){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken))  {
			if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
				return "redirect:/admin/hotels";
			}
			return "redirect:/home";
		}

		if(!modelMap.containsAttribute("user")){
			User user = new User();
			modelMap.addAttribute("user", user);
	}
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerProcess(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
		//Check for validations
		if(bindingResult.hasErrors()){
			//include validation errors upon redirect
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);

			//add user if invalid was returned
			redirectAttributes.addFlashAttribute("user", user);

			return "redirect:register";
		}

		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("flash1", new FlashMessage("Korisnik uspesno registrovan! Ulogujte se.", FlashMessage.Status.SUCCESS));
		return "redirect:login";
	}
}


