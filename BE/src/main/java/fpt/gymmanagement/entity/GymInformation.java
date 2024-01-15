package fpt.gymmanagement.entity;

import fpt.gymmanagement.common.CommonEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gym_informations")
public class GymInformation extends CommonEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_course_id")
    private String userCourseId;

    private Double height; // chiều cao

    private Double weight; // nặng

    private Double ecw_tbw; //tỷ lệ nước ngoại bào (ECW/TBW)

    @Column(name = "ecw_tbw_normal")
    private Double ecw_tbwNormal; //tỷ lệ nước ngoại bào (ECW/TBW)

    @Column(name = "ecw_tbw_normal_to")
    private Double ecw_tbwNormalTo; //tỷ lệ nước ngoại bào (ECW/TBW)

    //Khối lượng Protein trong cơ thể. Protein cấu tạo nên cơ bắp, bao gồm các cơ trơn ở nội tạng và cơ tim.
    // Chỉ số này đạt tiêu chuẩn ở mức 14-16% với nữ, 16-18% với nam.
    private Double protein;

    @Column(name = "protein_normal")
    private Double proteinNormal;

    @Column(name = "protein_normal_to")
    private Double proteinNormalTo;

    // Phần trăm mỡ cơ thể = Khối lượng mỡ/ Trọng lượng.
    // Chỉ số PBF tiêu chuẩn là  15% đối với nam, 23% đối với nữ.
    private Double pbf;

    @Column(name = "pbf_normal")
    private Double pbfNormal;

    @Column(name = "pbf_normal_to")
    private Double pbfNormalTo;

    //Tỷ lệ eo trên hông = Vòng bụng/Vòng mông. WHR cho ra chẩn đoán 1 phần mỡ vùng bụng, mỡ nội tạng của 1 người.
    // Ở nữ: Mức siêu chuẩn là < 0.75
    //Ở nam: Mức siêu chuẩn là < 0.85
    private Double whr;

    @Column(name = "whr_normal")
    private Double whrNormal;

    @Column(name = "whr_normal_to")
    private Double whrNormalTo;

    @Column(name = "bust_measurement")
    private Double bustMeasurement;

    // Số đo vòng 2 (vòng eo)
    @Column(name = "waist_measurement")
    private Double waistMeasurement;

    // Số đo vòng 3 (vòng mông)
    @Column(name = "hip_measurement")
    private Double hipMeasurement;

    //Trường này pt note lại thông tin cho user: đang béo, thiếu cân,....
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_course_id", insertable = false, updatable = false)
    private UserCourse userCourse;
}
