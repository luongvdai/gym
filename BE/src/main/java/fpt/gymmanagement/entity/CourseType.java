package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "course_types")
public class CourseType extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String name;
}
