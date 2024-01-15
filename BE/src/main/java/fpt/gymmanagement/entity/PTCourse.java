package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pt_courses")
public class PTCourse extends CommonEntity {

    @Column(name = "pt_id")
    private String ptId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;
}
