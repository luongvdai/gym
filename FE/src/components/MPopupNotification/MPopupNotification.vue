<template>
  <div class="cover-popup" >
    <div class="popupnoti" v-if="MPopupN == 1"> 
      <div class="popupask-top">
        <div :class="classcssicon"></div>
        <div class="popupask-icon-label-ask">
          <label class="label-ask b">{{ errors }}</label>
        </div>
      </div>
      <div class="popupask-bottom-ask">
        <button class="popupask-bottom-right" @click="popupaskcance">
          Đóng
        </button>
        <div class="popupask-bottom-btn-ask b" v-if="btnOut"  @click="agreedelete">
            <MButton :ButtonCss="'btn-button-openPopup-Employee'" :text="'Đăng nhập'" />
        </div>
        
      </div>
    </div>

    <div class="popupask" v-if="MPopupN == 2">
      <div class="popupask-top">
        <div class="popupask-icon icon-ask"></div>
        <div class="popupask-icon-label-ask">
          <label class="label-ask"
            >Bạn có thực sự muốn xóa {{record}}</label>
          <label class="label-ask">{{ getRecordCode }} không?</label>
        </div>
      </div>
      <div class="popupask-bottom-ask">
        <button class="popupask-bottom-right" @click="popupaskcance">
          Không
        </button>
        <div class="popupask-bottom-btn-ask"  @click="agreedelete">
            <MButton :ButtonCss="'btn-button-openPopup-Employee'" :text="'Có'" />
        </div>
        
      </div>
    </div>

     <div class="popupask" v-if="MPopupN == 3">
      <div class="popupask-top">
        <div class="popupask-icon icon-ask-edit"></div>
        <div class="popupask-icon-label-ask-edit">
          <label class="label-ask-edit"
            >Hãy kiểm tra chi tiết. Bạn có muốn {{ getRecordCode }} không?</label
          >
        </div>
      </div>
      <div class="popupask-bottom-edit">
        <button class="popupask-bottom-right-edit" @click="popupaskcance">Hủy</button>
        <div class="popupask-bottom-left">
          <button class="popupask-bottom-left-edit" @click="popupaskcance">Không</button>
          <div class="popupask-bottom-left-btn" @click="agreedelete">
            <MButton :ButtonCss="'btn-button-openPopup-Employee'" :text="'Có'" />
          </div>
        </div>
      </div>
    </div>
    <div class="popupincurred" v-if="MPopupN == 4">
      <div class="popupincurred-top">
        <div class="popupask-icon-icon-incurred"></div>
        <div class="popupask-icon-label-incurred">
          <label class="label-incurred"
            >Yêu cầu không thành công</label
          >
          <label class="label-incurred-edit"
            >{{errors}}</label
          >
        </div>
      </div>
      <div class="popupincurred-bottom">
          <button class="popupincurred-bottom-right" @click="closeNotification">Đóng</button>
      </div>
    </div>
    <div class="popupincurred" v-if="MPopupN == 5">
      <div class="popupincurred-top">
        <div class="popupask-icon-icon-incurred"></div>
        <div class="popupask-icon-label-incurred">
          <label class="label-incurred"
            >Yêu cầu không thành công</label
          >
          <label class="label-incurred-edit"
            >{{ getRecordCode }} đang được sử dụng!</label
          >
        </div>
      </div>
      <div class="popupincurred-bottom">
          <button class="popupincurred-bottom-right" @click="closeNotification">Đóng</button>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";

export default {
  props:{
      //tiêu đề lỗi
      errors: String,
      //css icon
      classcssicon: String,
      //css tiêu đề
      classlabel: String,
      //code record
      getRecordCode: String,
      //mã 
      record: String,
      //loại popup
      MPopupN: Number,
      //tên buton
      btnOut: Boolean,
  },
  methods: {
    /**
     * hàm đồng ý xóa nhân viên
     * Nguyễn Văn Cương 01/10/2022
     */
    closeNotification() {
      this.$emit("close-notification-click");
    },

    /**
     * hàm nhấn nút hủy xóa nhân viên 
     * Nguyễn Văn Cương 01/10/2022
     */
    popupaskcance() {
      this.$emit("popup-ask-cance");
    },

    /**
     * hàm đồng ý xóa nhân viên
     * Nguyễn Văn Cương 01/10/2022
     */
    agreedelete() {
      this.$emit("agree-delete-click");
      console.log("xxxxx");
    },

    /**
     * hàm nhấn nút không lưu dữ liệu thay đổi
     * Nguyễn Văn Cương 05/10/2022
     */
    popupaskno() {
      this.$emit("popup-ask-no");
    },
    /**
     * hàm đồng ý lưu dữ liệu nhân viên 
     * Nguyễn Văn Cương 05/10/2022
     */
    agreesave() {
      this.$emit("agree-save-click");
    },
  },
  components: { MButton },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.cover-popup {
  position: fixed;
  width: 100%;
  height: 100vh;
  right: 0;
  left: 0;
  top: 0;
  bottom: 0;
  background-color: rgba(12, 12, 12, 0.3);
  z-index: 1000;
}
.popupnoti {
  width: 450px;
  height: 180px;
  position: relative;
  top: 40%;
  margin: 0 auto;
  background-color: #fff;
}
.popupask-top {
  width: 100%;
  height: 100px;
  display: flex;
  position: relative;
}
.popupask-icon {
  width: 50px;
  height: 50px;
  margin-left: 50px;
  margin-top: 50px;
  border-bottom: 1px solid #bbbb;
}
.icon-noti {
  background-image: var(--icon);
  background-position: -752px -462px;
  background-repeat: no-repeat;
}
.icon-noti-duli {
  background-image: var(--icon);
  background-position: -598px -463px;
  background-repeat: no-repeat;
}
.popupask-icon-label {
  width: 300px;
  margin-top: 50px;
  border-bottom: 1px solid #bbbb;
  padding-top: 10px;
}
.popupask-icon-label-duli {
  width: 300px;
  margin-top: 40px;
  border-bottom: 1px solid #bbbb;
  padding-top: 10px;
}
.label-ask {
  color: #000;
}
.popupask-icon2 {
  width: 50px;
  height: 50px;
  background-color: #fff;
  border: none;
}
.icon-cance {
  background-image: var(--icon);
  background-position: -128px -131px;
  background-repeat: no-repeat;
}
.popupask-bottom-btn {
  width: 100px;
  height: 30px;
  position: absolute;
  right: 40%;
  top: 65%;
}.popupask {
  width: 450px;
  height: 180px;
  position: relative;
  top: 40%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popupask-top {
  width: 100%;
  height: 100px;
  display: flex;
  position: relative;
}
.popupask-icon {
  width: 50px;
  height: 50px;
  margin-left: 43px;
  margin-top: 50px;
  border-bottom: 1px solid;
}
.icon-ask {
  background-image: var(--icon);
  background-position: -598px -463px;
  background-repeat: no-repeat;
}
.popupask-icon-label-ask {
  width: 320px;
  margin-top: 30px;
  border-bottom: 1px solid #bbb;
  padding-top: 10px;
  line-height: 25px;
  display: grid;
}
.label-ask {
  line-height: 15px;
}
.label-ask.b {
  line-height: 50px;
  font-size: 16px;
}
.popupask-icon2 {
  width: 50px;
  height: 50px;
  background-color: #fff;
  border: none;
}
.icon-cance {
  background-image: var(--icon);
  background-position: -128px -131px;
  background-repeat: no-repeat;
}
.popupask-bottom-right {
  height: 38px;
  width: 80px;
  border: 1px solid black;
  text-align: center;
  color: black;
  font-weight: bold;
  background-color: #ffff;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 43px;
  position: absolute;
  left: 0;
}.popupask-bottom-btn-ask{
  width: 100px;
  height: 30px;
  position: absolute;
  right: 30px;
  top: 65%;
}
.popupask-bottom-btn-ask{
  width: 120px;
}

.popupask {
  width: 450px;
  height: 180px;
  position: relative;
  top: 40%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popupask-top {
  width: 100%;
  height: 100px;
  display: flex;
  position: relative;
}
.popupask-icon {
  width: 50px;
  height: 50px;
  margin-left: 50px;
  margin-top: 50px;
  border-bottom: 1px solid #bbbb;
}
.icon-ask-edit {
  background-image: var(--icon);
  background-position: -832px -462px;
  background-repeat: no-repeat;
}
.popupask-icon-label-ask-edit {
  width: 320px;
  margin-top: 45px;
  border-bottom: 1px solid #bbbb;
  padding-top: 10px;
  line-height: 25px;
}
.label-ask-edit {
  color: #000;
  font-size: 13px;
}
.popupask-icon2 {
  width: 50px;
  height: 50px;
  background-color: #fff;
  border: none;
}
.icon-cance {
  background-image: var(--icon);
  background-position: -128px -131px;
  background-repeat: no-repeat;
}
.popupask-bottom-right-edit {
  height: 38px;
  width: 80px;
  border: 1px solid #bbbb;
  text-align: center;
  color: black;
  font-weight: bold;
  background-color: #ffff;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 50px;
  position: absolute;
  left: 0;
}.popupask-bottom-edit{
  display: flex;
}.popupask-bottom-left-edit {
  height: 38px;
  width: 80px;
  border: 1px solid #bbbb;
  text-align: center;
  color: black;
  font-weight: bold;
  background-color: #ffff;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 50px;
  position: absolute;
  right: 105px;
}.popupask-bottom-left-btn{
  width: 100px;
  height: 30px;
  position: absolute;
  right: 30px;
  top: 65%;
}.popupincurred{
  width: 430px;
  height: 230px;
  position: relative;
  top: 30%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}.popupincurred-top{
  width: 85%;
  height: 150px;
  margin-left: 32px;
  display: flex;
  position: relative;
  border-bottom: 2px solid #bbbb;
}.popupask-icon-icon-incurred{
  background-image: var(--icon);
  background-position: -777px -1152px;
  background-repeat: no-repeat;
  width: 80px;
  height: 80px;
  margin-top: 40px;
  padding-left: 25px;

}.popupask-icon-label-incurred{
  width: 320px;
  height: 100px;
  margin-top: 40px;
  margin-left: 20px; 
  line-height: 20px;
  display: grid;
}.label-incurred{
  color: #000;
  font-size: 15px;
  font-weight: bold;
}.label-incurred-edit{
  color: #000;
  font-size: 14px;
}.popupincurred-bottom{
  display: flex;
  width: 100%;
  height: 75px;
}.popupincurred-bottom-right{
  height: 38px;
  width: 80px;
  border: 1px solid #bbbb;
  text-align: center;
  color: black;
  font-weight: bold;
  background-color: #ffff;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 50px;
  position: absolute;
  right: 33px;
}.popupincurred-bottom-right-btn{
  width: 143px;
  height: 30px;
  position: absolute;
  right: 125px;
  top: 74%;
}
@media (max-width: 1200px) {
  .popupnoti{
    width: 90%;
  }
  .popupask-icon{
    margin-left: 26px;
  }
  .popupask-icon-label-ask{
    width: 76%;
  }
}
</style>


