package com.ape.controller;

import com.ape.dto.CreditCardDTO;
import com.ape.dto.request.CreditCardRequest;
import com.ape.model.CreditCard;
import com.ape.service.CreditCardService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "This endpoint is for adding new Credit Card")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> addCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest){
        creditCardService.saveCard(creditCardRequest);
        APEResponse response = new APEResponse(ResponseMessage.CREDIT_CARD_SAVED_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CreditCardDTO> getCardById(@PathVariable Long id){
        CreditCardDTO card = creditCardService.getCardById(id);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/option")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<CreditCardDTO>> getAllCards(){
        List<CreditCardDTO> creditCardDTOList = creditCardService.getAllCards();
        return ResponseEntity.ok(creditCardDTOList);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> updateCardById(@PathVariable Long id, @Valid @RequestBody CreditCardRequest creditCardRequest){
        CreditCardDTO cardDTO = creditCardService.updateCard(id,creditCardRequest);
        APEResponse response = new APEResponse(ResponseMessage.CREDIT_CARD_DELETE_MESSAGE, true, cardDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> deleteCardById(@PathVariable Long id){
        creditCardService.removeById(id);
        APEResponse response = new APEResponse(ResponseMessage.CREDIT_CARD_DELETE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }


}
