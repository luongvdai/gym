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
          <label class="label-left">Ngày tạo: </label>
          <label class="label-right">{{
            formatUnixTime(this.dataSelected.createdDate)
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Họ và tên: </label>
          <label class="label-right">{{
            this.dataSelected.name  
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Email: </label>
          <label class="label-right">{{
            this.dataSelected.email
          }}</label>
        </div>
        <div class="popup-center-element">
          <label class="label-left">Số điện thoại: </label>
          <label class="label-right">{{
            this.dataSelected.phoneNumber
          }}</label>
        </div>
        
        <div class="popup-center-element">
          <label class="label-left">Trạng thái: </label>
          <label class="label-right">{{ formatStatus(this.dataSelected.status) }}</label>
        </div>
 
     
        <div class="popup-center-element">
          <label class="label-left">Nội dung: </label>
          <textarea
            class="note-text"
            v-model="this.dataSelected.note"
            readonly= true
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
  name: "ConsultantInforPopup",
  components: {
    MButton,
  },
  props: {
    recordSelected: Object,
  },
  created() {
    this.dataSelected = { ...this.recordSelected };
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
    formatStatus(status){
      if(status == true){
        return "Đã xử lí";
      }
      else return "Chưa xử lí";
    }
    
  },
  data() {
    return {
      dataSelected: {},
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
  max-width: 500px;
  width: 90%;
  height: 500px;
  position: relative;
  top: 15%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  padding: 1% 3%;
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
  height: 14%;
  display: flex;
  align-items: center;
  padding: 0 0%;
}
.popup-center-element:last-child {
  width: 100%;
  height: 30%;
  padding-top: 10px;
  align-items: start;
}
.popup-center-element label {
  font-size: 15px;

}
.label-left {
  height: 100%;
  width: 35%;
  text-align: left;
  font-weight: bold;
}
.label-right {
  height: 100%;
  width: 65%;
  word-break: break-word;
}
.popup-bottom {
  width: 100%;
  height: 7%;
  display: flex;
  flex-wrap: wrap;
}
.note-text {
  width: 65%;
  height: 90%;
  min-height: 40px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical; /* Cho phép chỉnh sửa chiều cao theo chiều dọc */
}
.bmi-chip{
  width: fit-content;
}
@media screen and (max-width: 767px) {
  .popup-bottom {
    height: 10%;
  }
  .popup-center {
    height: 83%;
  }
}
</style>