package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "courses")
public class Course extends CommonEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    @Length(max = 50)
    private String title;

    @Column(name = "range_process")
    private Integer rangeProcess;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "price")
    private Double price;

    @Column(name = "course_type_id")
    private String courseTypeId;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "banner")
    private String banner;

    @ManyToOne
    @JoinColumn(name = "course_type_id", insertable = false, updatable = false)
    private CourseType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
}
