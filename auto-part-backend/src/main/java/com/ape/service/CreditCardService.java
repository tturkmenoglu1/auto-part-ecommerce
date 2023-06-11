package com.ape.service;

import com.ape.dto.CreditCardDTO;
import com.ape.dto.request.CreditCardRequest;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.CreditCardMapper;
import com.ape.model.CreditCard;
import com.ape.model.User;
import com.ape.repository.CreditCardRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    private final UserService userService;

    public void saveCard(CreditCardRequest creditCardRequest) {
        CreditCard card = new CreditCard();
        User user = userService.getCurrentUser();

        card.setNameOnCard(creditCardRequest.getNameOnCard());
        card.setCardNumber(creditCardRequest.getCardNumber());
        card.setExpireDate(creditCardRequest.getExpireDate());
        card.setSecurityCode(creditCardRequest.getSecurityCode());
        card.setUser(user);

        creditCardRepository.save(card);
    }

    public List<CreditCardDTO> getAllCards() {
        List<CreditCard> cards = creditCardRepository.findAll();
        return creditCardMapper.creditCardListToCreditCardDTOList(cards);
    }

    public CreditCard findCreditCardById(Long id){
        return creditCardRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.CREDIT_CARD_NOT_EXIST));
    }

    public void removeById(Long id) {
        CreditCard card = findCreditCardById(id);
        creditCardRepository.delete(card);
    }

    public CreditCardDTO updateCard(Long id,CreditCardRequest creditCardRequest) {
        CreditCard card = findCreditCardById(id);

        card.setUser(userService.getCurrentUser());
        card.setNameOnCard(creditCardRequest.getNameOnCard());
        card.setCardNumber(creditCardRequest.getCardNumber());
        card.setExpireDate(creditCardRequest.getExpireDate());
        card.setSecurityCode(creditCardRequest.getSecurityCode());

        creditCardRepository.save(card);
        return creditCardMapper.creditCardToCreditCardDTO(card);
    }

    public CreditCardDTO getCardById(Long id) {
        CreditCard card = findCreditCardById(id);
        return creditCardMapper.creditCardToCreditCardDTO(card);
    }
}
