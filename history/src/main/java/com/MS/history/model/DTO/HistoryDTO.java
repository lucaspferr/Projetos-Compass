package com.MS.history.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

    private UserForm user;
    private List<PurchasesDTO> purchases;
}
