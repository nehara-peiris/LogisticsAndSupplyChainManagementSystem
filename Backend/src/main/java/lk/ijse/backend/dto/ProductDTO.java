package lk.ijse.backend.dto;

import lk.ijse.backend.entity.Category;
import lk.ijse.backend.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private Category category;
    private Set<Supplier> suppliers;

}
