package com.springapp.order.service;

import com.springapp.order.constants.Constant;
import com.springapp.order.dto.InventoryResponse;
import com.springapp.order.dto.OrderLineItemsDto;
import com.springapp.order.dto.OrderRequest;
import com.springapp.order.model.Order;
import com.springapp.order.model.OrderLineItems;
import com.springapp.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes =  order.getOrderLineItemsList().stream()
                                                              .map(OrderLineItems::getSkuCode)
                                                              .toList();

        //Call Order service to place order if product is in stock.
        var result = webClient.get()
                .uri(Constant.INVENTORY_SERVICE_URL,
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        var allProductsInStock = Arrays.stream(result).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock)
            orderRepository.save(order);
        else
            throw new IllegalArgumentException("Product is not in Stock . Please Try later");
        return "Order Placed";
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
