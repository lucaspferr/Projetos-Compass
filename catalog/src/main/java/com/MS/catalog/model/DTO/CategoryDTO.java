package com.MS.catalog.model.DTO;

import com.MS.catalog.model.Product;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long category_id;
    @NotNull @NotEmpty
    private String name;
    @NotNull
    private boolean active;
}
