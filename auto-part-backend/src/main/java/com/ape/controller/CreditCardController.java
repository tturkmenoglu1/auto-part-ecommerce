package com.ape.controller;

import com.ape.dto.CreditCardDTO;
import com.ape.dto.request.CreditCardRequest;
import com.ape.model.CreditCard;
import com.ape.service.CreditCardService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> addCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest){
        creditCardService.saveCard(creditCardRequest);
        APEResponse response = new APEResponse(ResponseMessage.CREDIT_CARD_SAVED_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/option")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<CreditCardDTO>> getAllCards(){
        List<CreditCardDTO> creditCardDTOList = creditCardService.getAllCards();
        return ResponseEntity.ok(creditCardDTOList);
    }


}
