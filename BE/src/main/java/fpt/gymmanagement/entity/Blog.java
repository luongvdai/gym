package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blogs")
public class Blog extends CommonEntity {

    private static final long serialVersionUID = 1L;

    @Length(max = 200, message = "Độ dài tiêu đề không quá 100 kí tự")
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String attach_url;

    @Column(name = "blog_type_id")
    private String typeId;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_type_id", insertable = false, updatable = false)
    private BlogType type;

    @Column(name = "branch_id")
    private String branchId;

    @ManyToOne
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
}
