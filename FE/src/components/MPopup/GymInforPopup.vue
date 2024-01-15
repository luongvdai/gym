<template>
  <div class="cover-popup">
    <div class="popup">
      <div class="popup-top">
        <label for="">Thông tin chi tiết</label>
        <button class="btn-exit" @click="closePopup">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <div class="popup-center">
        <div class="popup-center-element">
          <label class="label-left"> </label>
          <label class="label-center b"> Chỉ số bản thân </label>
          <label class="label-right b">Khoảng tiêu chuẩn </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Ngày cập nhật: </label>
          <label class="label-center">{{
            formatUnixTime(this.dataSelected.updatedDate)
          }}</label>
          <label class="label-right"> </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Người tạo: </label>
          <label class="label-center">{{
            getMentorName(this.dataSelected.mentor)
          }}</label>
          <label class="label-right"> </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Chiều cao: </label>
          <label class="label-center">{{
            this.dataSelected.height + "m"
          }}</label>
          <label class="label-right"> </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Cân nặng: </label>
          <label class="label-center">{{
            this.dataSelected.weight + "kg"
          }}</label>
          <label class="label-right"> </label>
        </div>

        <div class="popup-center-element">
          <label class="label-left">Tỷ lệ nước: </label>
          <label class="label-center">
            <v-chip
              class="bmi-chip"
              :color="
                formatPropertyColor(
                  this.dataSelected.ecw_tbw,
                  this.dataSelected.ecw_tbwNormal,
                  this.dataSelected.ecw_tbwNormalTo
                )
              "
            >
              {{ this.dataSelected.ecw_tbw*100 +"%" }}
            </v-chip>
          </label>
          <label class="label-right"
            >{{
              this.dataSelected.ecw_tbwNormal*100 +"%" +
              " - " +
              this.dataSelected.ecw_tbwNormalTo*100 +"%"
            }}
          </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Lượng cơ: </label>
          <label class="label-center">
                     <v-chip
              class="bmi-chip"
              :color="
                formatPropertyColor(
                  this.dataSelected.protein,
                  this.dataSelected.proteinNormal,
                  this.dataSelected.proteinNormalTo
                )
              "
            >{{this.dataSelected.protein*100 +"%"}}
             </v-chip>
          </label>
          <label class="label-right">{{
            this.dataSelected.proteinNormal*100 +"%" +
            " - " +
            this.dataSelected.proteinNormalTo*100 +"%"
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Phần trăm mỡ: </label>
          <label class="label-center">
                     <v-chip
              class="bmi-chip"
              :color="
                formatPropertyColor(
                  this.dataSelected.pbf,
                  this.dataSelected.pbfNormal,
                  this.dataSelected.pbfNormalTo
                )
              "
            >{{this.dataSelected.pbf*100 +"%"}} </v-chip>
          </label>
          <label class="label-right">{{
            this.dataSelected.pbfNormal*100 +"%" + " - " + this.dataSelected.pbfNormalTo*100 +"%"
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Vòng ngực: </label>
          <label class="label-right">{{
            this.dataSelected.bustMeasurement + "cm"
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Vòng eo: </label>
          <label class="label-right">{{
            this.dataSelected.waistMeasurement + "cm"
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Vòng mông: </label>
          <label class="label-right">{{
            this.dataSelected.hipMeasurement + "cm"
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">BMI: </label>
          <label class="label-center">
            <v-chip class="bmi-chip" :color="this.bmiColor">
              {{
                this.formatBMI(
                  this.dataSelected.weight /
                    (this.dataSelected.height * this.dataSelected.height)
                )
              }}
            </v-chip></label
          >
          <label class="label-right">18 - 25.5 </label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Lưu ý của mentor: </label>
          <textarea
            class="note-text"
            v-model="this.dataSelected.note"
            readonly="true"
          ></textarea>
        </div>
      </div>
      <div class="popup-bottom">
        <div class="bottom-left">
          <MButton
            :text="'Đóng'"
            :class="'btn-button-cancel'"
            class="button-send"
            @click="closePopup"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "../MButton/MButton.vue";

export default {
  name: "GymInforPopup",
  components: {
    MButton,
  },
  props: {
    recordSelected: Object,
  },
  created() {
    this.dataSelected = { ...this.recordSelected };
    console.log(this.dataSelected);
  },
  methods: {
    closePopup() {
      this.$emit("close-popup-click");
    },
    formatUnixTime(unixTimestamp) {
      try {
        //const utcTime = new Date(unixTimestamp); // Nhân với 1000 để chuyển sang milliseconds
        //const localTime = new Date(utcTime.getTime() + 7 * 60 * 60 * 1000); // Áp dụng múi giờ Việt Nam (UTC+7)
        let localTime = new Date(unixTimestamp);

        const day = localTime.getDate();
        const month = localTime.getMonth() + 1; // Tháng được đếm từ 0
        const year = localTime.getFullYear();
        const hours = localTime.getHours();
        const minutes = localTime.getMinutes();

        const formattedDateTime = `${hours}:${minutes} - ${day}/${month}/${year}`;

        return formattedDateTime; // Trả về ngày và giờ đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
      }
    },
    getMentorName(mentor) {
      return mentor.fullName;
    },
    formatBMI(bmi) {
      if (bmi < 18.5) {
        this.bmiRate = "Gầy";
        this.bmiColor = "red";
      } else if (bmi > 25) {
        this.bmiRate = "Béo";
        this.bmiColor = "red";
      } else {
        this.bmiRate = "Cân đối";
        this.bmiColor = "green";
      }
      return bmi.toFixed(2);
    },
    formatPropertyColor(index, from, to) {
      if (index >= from && index <= to) {
        return "green";
      } else return "red";
    },
  },
  data() {
    return {
      dataSelected: {},
      bmiRate: "",
      bmiColor: "",
    };
  },
};
</script>

<style scoped>
.cover-popup {
  position: fixed;
  width: 100%;
  height: 100%;
  right: 0;
  left: 0;
  top: 0;
  bottom: 0;
  background-color: rgba(12, 12, 12, 0.3);
  z-index: 1000;
}
.popup {
  max-width: 600px;
  width: 90%;
  height: 650px;
  position: relative;
  top: 5%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  padding: 1% 2%;
}
.popup-top {
  width: 100%;
  height: 7%;
}
.popup-top label {
  width: 100%;
  height: 100%;
  font-size: 25px;
  font-weight: bold;
  color: green;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.btn-exit {
  width: 30px;
  height: 30px;
  border: none;
  background: #fff;
  font-size: 25px;
  position: absolute;
  right: 1%;
  top: 1%;
  color: red;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}
.btn-exit:hover {
  background-color: rgb(203, 200, 200);
  transform: scale(1.15);
  transition: all 0.3s ease;
}
.popup-center {
  width: 100%;
  height: 86%;
  padding: 15px 0;
}
.popup-center-element {
  width: 100%;
  height: 7%;
  display: flex;
  align-items: center;
  padding: 0 0%;
}
.popup-center-element:last-child {
  width: 100%;
  height: 16%;
  padding-top: 10px;
  align-items: start;
}
.popup-center-element label {
  font-size: 15px;
  text-align: center;
}
.label-left {
  height: 100%;
  width: 35%;
  font-weight: bold;
  display: flex;
  align-items: center;
}
.label-center {
  height: 100%;
  width: 32.5%;
  word-break: break-word;
}
.label-center.b {
  font-weight: bold;
  color: blue;
}
.label-right {
  height: 100%;
  width: 32.5%;
  word-break: break-word;
  display: flex;
  align-items: center;
  justify-content: center;
}
.label-right.b {
  font-weight: bold;
  color: blue;
  display: block;
}
.popup-bottom {
  width: 100%;
  height: 7%;
  display: flex;
  flex-wrap: wrap;
}
.note-text {
  width: 50%;
  height: 90%;
  min-height: 40px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical; /* Cho phép chỉnh sửa chiều cao theo chiều dọc */
}
.bmi-chip {
  width: fit-content;
}
@media screen and (max-width: 1200px) {
  .popup-bottom {
    height: 10%;
  }
  .popup-center {
    height: 83%;
  }
  .popup-center-element{
    font-size: 12px;
  }
}
</style>