package com.ape.service;

import com.ape.dto.CreditCardDTO;
import com.ape.dto.request.CreditCardRequest;
import com.ape.exception.ConflictException;
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
}
