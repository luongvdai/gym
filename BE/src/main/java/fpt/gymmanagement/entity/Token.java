package fpt.gymmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.*;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
public class Token extends CommonEntity {

    @Column(unique = true, columnDefinition = "text")
    private String token;

    private final String tokenType = "Bearer";

    private Boolean revoked;

    private Boolean expired;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @JsonIgnore
    private User user;
}