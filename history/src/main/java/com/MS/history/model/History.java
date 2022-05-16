package com.MS.history.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "history")
public class History {

    @Transient
    public static final String SEQUENCE_NAME = "histories_sequence";

    @Id
    private Long history_id;

    @DocumentReference
    private User user;

    @DocumentReference
    private List<Purchases> purchases = new ArrayList<>();

}
