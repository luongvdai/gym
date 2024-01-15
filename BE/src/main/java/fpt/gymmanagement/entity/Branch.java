package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "branches")
public class Branch extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "background_url")
    private String backgroundUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}
