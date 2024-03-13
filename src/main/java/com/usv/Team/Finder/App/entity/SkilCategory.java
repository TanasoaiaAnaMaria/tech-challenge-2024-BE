package com.usv.Team.Finder.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "skilCategory")
public class SkilCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSkilCategory;

    private String skilCategoryName;

    UUID idOrganisation;
}
