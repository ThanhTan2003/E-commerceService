package org.programmingtechie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "customer")
public class Customer
{
    @Id
    private String id;

    @Column(length = 100)
    private String name;

    @Column(length = 150)
    private String address;

    @Column(nullable = false, unique = true, length = 100)
    private String phoneNumber;
}
