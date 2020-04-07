package com.milosdevelopmetn;

import com.milosdevelopmetn.model.Hotel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;


@Component
public class HotelValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Hotel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Hotel hotel = (Hotel)target;

        // Validate file only if the Gif's id is null (with a null id, it must be a new Gif),
        // so that existing Gif can be updated without uploading new file
        if(hotel.getId() == null && (hotel.getFile() == null || hotel.getFile().isEmpty())) {
            errors.rejectValue("file","file.required","Odaberite sliku za hotel");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "location.empty", "Polje lokacija mora biti popunjeno");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Polje naziv mora biti popunjeno");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"description","description.empty","Polje opis mora biti popunjeno");

        ValidationUtils.rejectIfEmpty(errors,"numStar","numStar.empty","Morate odabrati broj zvezda");
    }
}
