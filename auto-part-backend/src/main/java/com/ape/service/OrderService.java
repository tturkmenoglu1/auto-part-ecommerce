package com.ape.service;

import com.ape.dto.OrderDTO;
import com.ape.dto.request.OrderRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.OrderMapper;
import com.ape.model.*;
import com.ape.model.enums.OrderStatus;
import com.ape.model.enums.PaymentStatus;
import com.ape.model.enums.TransactionStatus;
import com.ape.repository.OrderRepository;
import com.ape.service.email.EmailSender;
import com.ape.service.email.EmailService;
import com.ape.utility.ErrorMessage;
import com.ape.utility.reuseableMethods.Calculators;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final BasketService basketService;

    private final UserService userService;

    private final UserAddressService userAddressService;

    private final Calculators calculator;

    private final OrderItemService orderItemService;

    private final ProductService productService;

    private final TransactionService transactionService;

    private final PaymentService paymentService;

    private final EmailSender emailSender;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, BasketService basketService, UserService userService, @Lazy UserAddressService userAddressService, Calculators calculator, OrderItemService orderItemService, ProductService productService, TransactionService transactionService, PaymentService paymentService, EmailSender emailSender, EmailService emailService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.userService = userService;
        this.userAddressService = userAddressService;
        this.calculator = calculator;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.transactionService = transactionService;
        this.paymentService = paymentService;
        this.emailSender = emailSender;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
    }

    public boolean existsByAddress(UserAddress userAddress) {
        return orderRepository.existsByAddress(userAddress);
    }

    public OrderDTO createOrder(String basketUUID, OrderRequest orderRequest) {
        Basket basket = basketService.findBasketByUUID(basketUUID);
        List<BasketItem> basketItemList = basket.getBasketItem();
        DecimalFormat df = new DecimalFormat("#.##");
        Order order = new Order();
        Payment payment = new Payment();
        Transaction transaction = new Transaction();
        User user = userService.getCurrentUser();
        UserAddress address = userAddressService.getAddressById(orderRequest.getAddressId());

        if (basketItemList.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.UUID_NOT_FOUND_MESSAGE);
        }
        String[] shippingCompany = {"UPS", "FedEx", "Amazon Logistics", "USPS", "DHL Express", "OnTrac", "Purolator", "LaserShip", "Aramex", "ShipBob"};
        String[] provider = {"PayPal", "Stripe", "Square", "Authorize.net", "Braintree", "Dwolla", "Amazon Pay", "Google Pay", "Apple Pay", "Visa Checkout"};
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            randomNumber.append(digits.get((int) ((Math.random() * 10))));
        }
        double discount = 0.0;
        double tax = calculator.calculateTaxCost(basketItemList);
        double subTotal = 0.0;

        for (BasketItem each : basketItemList) {
            Product product = each.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            if (each.getQuantity() > each.getProduct().getStockAmount()) {
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE, each.getProduct().getId()));
            }
            orderItem.setQuantity(each.getQuantity());
            orderItem.setDiscount(product.getDiscount());
            orderItem.setTax(product.getTax());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubTotal(each.getProduct().getPrice() * each.getQuantity());
            orderItemService.save(orderItem);
            order.getOrderItem().add(orderItem);

            discount += ((product.getPrice() - product.getDiscountedPrice()) * each.getQuantity());
            subTotal += product.getPrice() * each.getQuantity();

            Integer newStockAmount = product.getStockAmount() - each.getQuantity();
            product.setStockAmount(newStockAmount);
            productService.save(product);
        }

        double grandTotal = Double.parseDouble(df.format(calculator.grandTotalCalculator(subTotal,discount,tax)).replaceAll(",","."));
        double shippingCost = calculator.calculateShippingCost(grandTotal);

        transaction.setTransaction(TransactionStatus.CREATED);
        user.getTransactions().add(transaction);
        user.getOrders().add(order);
        payment.setAmount(grandTotal+shippingCost);
        payment.setProvider(provider[(int)(Math.random()*shippingCompany.length)]);
        payment.setStatus(PaymentStatus.COMPLETED);
        transactionService.save(transaction);
        userService.save(user);
        paymentService.save(payment);



        order.setCode(UUID.randomUUID().toString());
        order.setContactName(orderRequest.getContactName());
        order.setContactPhone(orderRequest.getPhoneNumber());
        order.setGrandTotal(grandTotal+shippingCost);
        order.setShippingCost(calculator.calculateShippingCost(order.getGrandTotal()));
        order.setStatus(OrderStatus.PENDING);
        order.setAddress(address);
        order.setTax(Double.parseDouble(df.format(tax).replaceAll(",",".")));
        order.setDiscount(Double.parseDouble(df.format(discount).replaceAll(",",".")));
        order.setSubTotal(Double.parseDouble(df.format(subTotal).replaceAll(",",".")));
        order.setUser(user);
        order.setShippingDetails(shippingCompany[(int)(Math.random()*shippingCompany.length)] + " : "+ randomNumber);
        order.getTransaction().add(transaction);
        order.getPayments().add(payment);
        orderRepository.save(order);

        emailSender.send(
                user.getEmail(),
                emailService.buildOrderMail(order)
        );

        basketService.cleanBasket(basketUUID);
        return orderMapper.orderToOrderDTO(order);
    }

}