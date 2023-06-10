package com.ape.utility.reuseableMethods;

import com.ape.model.BasketItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Calculators {

    public Double totalPriceWithDiscountCalculate(Integer quantity, Double price, Integer discount){
        return quantity*price*(100-discount)/100;
    }

    public double calculateTaxCost(List<BasketItem> basketItemList) {
        double taxCost = 0.0;
        for (BasketItem each:basketItemList) {
            taxCost+= (each.getProduct().getDiscountedPrice()* each.getQuantity()*(each.getProduct().getTax()))/100;
        }
        return taxCost;
    }

    public double grandTotalCalculator(double subTotal,double discount, double tax) {
        return (subTotal-discount)+tax;
    }

    public double calculateShippingCost(double orderGrandTotal) {
        double shippingCost = 0.0;

        if (orderGrandTotal<=750){
            shippingCost = 5.0;
        } else if (orderGrandTotal <= 1500.0){
            shippingCost = 15.0;
        } else if (orderGrandTotal <= 3000.0) {
            shippingCost = 25.0;
        } else if (orderGrandTotal <= 5000.0) {
            shippingCost = 35.0;
        }else return shippingCost;

        return shippingCost;
    }
}
