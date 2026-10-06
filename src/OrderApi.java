import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import spring.Api.GET;
import spring.Api.POST;
import spring.Api.RestController;

@RestController
public class OrderApi {

    private final OrderService service;

    public OrderApi(OrderService service) {
        this.service = service;
    }

    @GET("/orders")
    public List<OrderDto> listOrders() {
        return service.listAll().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @GET("/orders/{id}")
    public OrderDto getOrder(String id) {
        return service.findById(id)
                .map(OrderDto::from)
                .orElseThrow(() -> new IllegalArgumentException("no such order: " + id));
    }
    /**
     * Create a new order for the specified customer.
     *
     * @param customerId the customer identifier; must not be null or blank.
     * @param amount the order amount; must not be null and must be greater than zero.
     * @return the created order as a DTO.
     * @throws IllegalArgumentException if {@code customerId} is null, blank, or empty, or if {@code amount} is null or less than or equal to zero.
     */

    @POST("/orders")
    public OrderDto createOrder(String customerId, BigDecimal amount) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        return OrderDto.from(service.create(customerId, amount));
    }

    @POST("/orders/{id}/cancel")
    public OrderDto cancelOrder(String id) {
        if (!service.cancel(id)) {
            throw new IllegalStateException("cannot cancel: " + id);
        }
        return service.findById(id).map(OrderDto::from).orElseThrow();
    }
}
