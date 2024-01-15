package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "check_ins")
public class Checkin extends CommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "created_date", updatable = false)
    private Long createdDate = new Date().getTime();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
