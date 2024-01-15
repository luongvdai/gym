<template>
  <div class="cover-popup">
    <div class="popupedit" :style="height">
      <div class="popupedit-top">
        <div class="popupedit-top-left">
          <div class="popupedit-top-left-label" :style="width">
            {{ PopupEdit_label }}
          </div>
        </div>
      </div>

      <div class="popupedit-content item2" v-if="inputShow == 2">
        <div class="popupedit-input5" ref="inputCode">
          <label class="item-label product">Mã đổi mật khẩu</label>
          <label class="item-labelsao"> *</label>
          <MInput
            :inValue="inValue_Code"
            :maxlength="36"
            :tab="1"
            :typeInput="'code'"
            ref="inputFocus"
            @updateAlert="UpdateAlert"
            v-model="User.code"
          />
          <div class="alertInputEd" v-if="isShowAlertName">
            {{ AlertNameMess }}
          </div>
        </div>
        <div class="popupedit-input5 newpass" ref="inputCode">
          <label class="item-label product">Nhập mật khẩu mới</label>
          <label class="item-labelsao"> *</label>
          <MInput
            :inValue="inValue_Code"
            :maxlength="36"
            :tab="2"
            :typeInput="'password'"
            @updateAlert="UpdateAlert"
            v-model="User.newPassword"
          />
        </div>

        <div class="popupedit-input6">
          <label class="input6-label"
            >Tự động gửi lại mã đổi mật khẩu sau:
          </label>
          <label class="input6-label countdown" v-if="countDown > 0"
            >{{ countDown }}s</label
          >
          <label
            class="input6-label countdown b"
            v-if="countDown == 0"
            @click="sendCode"
            >gửi lại mã</label
          >
        </div>
      </div>

      <div class="popupedit-content item3" v-if="inputShow == 3">
        <div class="popupedit-input5 oldpass" ref="inputCode">
          <label class="item-label product">Nhập mật khẩu cũ</label>
          <label class="item-labelsao"> *</label>
          <MInput
            :inValue="inValue_Code"
            :maxlength="36"
            :tab="1"
            :typeInput="'password'"
            ref="inputFocus"
            v-model="User.oldPass"
            @updateAlert="UpdateAlert"
          />
        </div>

        <div class="popupedit-input5 oldpass" ref="inputCode">
          <label class="item-label product">Nhập mật khẩu mới</label>
          <label class="item-labelsao"> *</label>
          <MInput
            :inValue="inValue_Code"
            :maxlength="36"
            :tab="2"
            :typeInput="'password'"
            v-model="User.newPassword"
            @updateAlert="UpdateAlert"
          />
        </div>

        <div class="popupedit-input6 repeatoldpass">
          <label class="item-label product">Nhập lại mật khẩu</label>
          <label class="item-labelsao"> *</label>
          <MInput
            :inValue="inValue_Code"
            :maxlength="36"
            :tab="3"
            :typeInput="'password'"
            v-model="User.reNewPass"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEd" v-if="isShowAlertReNewPass">
            {{ AlertReNewPassMess }}
          </div>
        </div>
      </div>

      <div class="popupedit-bottom">
        <div class="popupedit-content-bottom-left">
          <div @click="handleCloseProductPopup">
            <MButton :tab="7" :ButtonCss="'btn-button-cancel'" :text="'Hủy'" />
          </div>
        </div>
        <div class="popupedit-content-bottom-right">
          <div class="btn-product-popup-save1" @click="handlePassword">
            <MButton
              :tab="5"
              :ButtonCss="'content-top-btn-content'"
              :text="'Xác nhận'"
            />
          </div>
        </div>
      </div>
      <div></div>
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import fetchOptions from "@/api/base/fetchOptions";
import apiConfig from "@/api/config/apiConfig";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import { LOGOUT_ACTION } from "@/stores/modules/storeconstants";
import { mapActions } from "vuex";
export default {
  components: {
    MButton,
    MInput,
  },
  props: {
    //tiêu đề popup
    PopupEdit_label: String,
    //trạng thái input
    inputShow: Boolean,
    //chiều cao popup
    height: String,
    //chiều dài popup
    width: String,
    //record đang chọn
    recordsSelected: Object,
    //mảng
    recordvalue: [],
    //url
    detailFormMode: Number,
    //loại validate
    ValidateUnit: Boolean,
    //tên người dùng
    userName: String,
  },
  created() {
    this.countDownTimer();
  },
  mounted() {
    //focus vào ô input
    this.handleLoopFocus();
  },
  unmounted() {},
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    ...mapActions("auth", {
      logout: LOGOUT_ACTION,
    }),

    handlePassword() {
      if (this.inputShow == 2) {
        this.resetPassword();
      } else {
        this.updatePassword();
      }
    },
    handleCloseProductPopup() {
      this.$emit("close-product-popup");
    },
    sendCode() {
      this.$emit("send-code");
      this.countDown = 60;
      this.countDownTimer();
    },

    /**
     * hàm tabindex vòng lặp
     */
    handleLoopFocus() {
      this.$refs.inputFocus.$el.focus();
    },

    countDownTimer() {
      if (this.countDown > 0) {
        setTimeout(() => {
          this.countDown -= 1;
          this.countDownTimer();
        }, 1000);
      }
    },

    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
        };

        //trường hợp toast cập nhật thành công
      } else {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: message,
        };
      }
      this.displayToast(this.ToastInfor);
    },

    /**
     * Hàm ẩn arlet sau khi người dùng nhập ô inpu
     */
    UpdateAlert() {
      //trường hợp nhập vào ô input
      this.isShowAlertName = false;
      this.isShowAlertCode = false;

      this.inValue_Name = true;
      this.inValue_Code = true;
      //trường hợp xóa ô input
      if (!this.User.code) {
        this.inValue_Name = false;
      }
      if (!this.User.newPassword) {
        this.inValue_Code = false;
      }
    },

    showAlert(code) {
      this.isShowAlertCode = true;
      this.inValue_Code = false;
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 3) {
        this.isShowAlertReNewPass = true;
        this.AlertReNewPassMess = "Nhập lại đúng mật khẩu";
      } else if (code == 400) {
        this.isShowAlertName = false;
        this.AlertNameMess = "Mã code dùng để thay đổi mật khẩu không đúng!";
      } else {
        this.AlertNameMess = "Lỗi máy chủ";
        this.AlertCodeMess = "Lỗi máy chủ";
      }
    },

    /**
     * hàm validate dữ liệu
     */
    validateAll() {
      var validate = true;
      if (this.User.newPassword != this.User.reNewPass && this.inputShow == 3) {
        this.ShowToast(3, "Nhập lại đúng mật khẩu");
        this.showAlert(3);
        return validate == false;
      }
      return validate;
    },

    async resetPassword() {
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      try {
        fetch(
          `${apiConfig}/users/reset-password/`,
          await fetchOptions("POST", {
            username: this.userName,
            code: this.User.code,
            newPassword: this.User.newPassword,
          })
        )
          .then((res) => res.text())
          .then((res) => {
            if (res.status) {
              if (res.status != "200") {
                this.ShowToast(3, res.message);
                this.showAlert(res.status);
              }
            } else {
              this.ShowToast((this.ToastStatus = 1));
              this.timeout = setTimeout(() => {
                this.$router.push("home");
              }, 2000);
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Thay đổi mật khẩu thất bại");
            this.showAlert(500);
          });
      } catch (error) {
       console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        this.showAlert(500);
      }
    },

    async updatePassword() {
      console.log(1);
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      if (this.validateAll() == true) {
        try {
          fetch(
            `${apiConfig}/users/change-password/`,
            await fetchOptions("PATCH", {
              currentPassword: this.User.oldPass,
              newPassword: this.User.newPassword,
              reNewPassword: this.User.reNewPass,
            })
          )
            .then((response) => response.json().catch(() => response))
            .then((res) => {
              if (res.status != "200") {
                this.ShowToast(3, res.message);
                  this.showAlert(res.status);
              } else {
                
                this.ShowToast((this.ToastStatus = 1));
                this.handleCloseProductPopup();
                this.logout();
                var rememberMe = localStorage.getItem("rememberMe");
                if (rememberMe === "false") {
                  localStorage.removeItem("userName");
                  localStorage.removeItem("password");
                  localStorage.removeItem("rememberMe");
                }
                this.$emit("close-product-popup");
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Thay đổi mật khẩu thất bại");
              this.showAlert(500);
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
          this.showAlert(500);
        }
      }
    },
  },
  data() {
    return {
      User: {},
      //lưu dữ liệu nhân viên
      inValue_Code: {
        type: Boolean,
        default: true,
      },
      //hiển thị đỏ nhập tên khi trống
      inValue_Name: {
        type: Boolean,
        default: true,
      },
      //hiển thị đỏ đơn vị nhập tên khi trống
      inValue_Unit: {
        type: Boolean,
        default: true,
      },
      inValue: true,
      //focus vào input khi mở popup
      inputFocus: null,
      //lưu trạng thái alert
      isShowAlertName: false,
      //lưu trạng thái alert
      isShowAlertCode: false,
      countDown: 60,
      AlertNameMess: "",
      AlertCodeMess: "",
      isShowAlertOldPass: false,
      isShowAlertNewPass: false,
      isShowAlertReNewPass: false,
      AlertReNewPassMess: "",
    };
  },
};
</script>

<style scoped>
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
.popupedit {
  width: 480px;
  height: 345px;
  position: relative;
  top: 30%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popupedit-top {
  width: 100%;
  height: 75px;
  display: flex;
  position: relative;
}
.popupedit-top-left {
  width: 50%;
  height: 100%;
}
.popupedit-top-left-label {
  height: 60px;
  width: 100%;
  font-size: 24px;
  font-weight: 700;
  margin-top: 26px;
  margin-left: 30px;
}
.popupedit-top-right {
  position: absolute;
  right: 0;
  display: flex;
  top: 10px;
  width: 70px;
  height: 50px;
}
.product-tooltip {
  width: 70px;
  height: 18px;
  font-size: 12px;
  position: absolute;
  top: 30px;
  right: 0;
  background-color: #505050;
  border-radius: 4px;
  padding: 2px 4px;
  z-index: 5;
  text-align: center;
  color: #fff;
  visibility: hidden;
}
.ask-icon:hover .product-tooltip {
  visibility: visible;
  opacity: 1;
  right: 20px;
  width: 60px;
}
.question-icon:hover .product-tooltip {
  visibility: visible;
  opacity: 1;
}
.popupedit-bottom {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 96%;
  height: 60px;
}
.popupedit-content-bottom-left {
  position: absolute;
  left: 0;
  width: 30%;
  height: 100%;
}
.popupedit-content-bottom-right {
  position: absolute;
  right: 0;
  width: 228px;
  height: 100%;
}
.popupedit-content {
  width: 87%;
  height: 195px;
  border-bottom: 1px solid #bbbb;
  margin-left: 32px;
}
.item-label.product {
  font-size: 12px;
}
.popupedit-input5 {
  text-align: left;
  width: 80%;
  height: 60px;
  top: 0%;
  left: 5%;
  position: relative;
}
.popupedit-input5.newpass {
  top: 8%;
}
.popupedit-input5.oldpass {
  height: 32%;
}
.popupedit-input6.repeatoldpass {
  width: 76%;
  left: 11%;
  top: 60%;
}
.popupedit-input6 {
  position: absolute;
  width: 100%;
  left: 11%;
  top: 63%;
  height: 60px;
  text-align: left;
}
.popupedit-input6.repeatpass {
  width: 70%;
  left: 10%;
  top: 55%;
}
.popupedit-content.item2 {
  margin-top: 5px;
  height: 56%;
}
.popupedit-content.item3 {
  width: 95%;
  margin-top: 5px;
  height: 60%;
}
.alertInputEd {
  width: 150px;
  height: 18px;
  font-size: 12px;
  position: absolute;
  top: 60px;
  right: -18px;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 2px 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
}
.alertInputEd.b {
  right: 40px;
}
.alertInputEd.c {
  right: 100px;
}
.alertInputEd::after {
  content: " ";
  position: absolute;
  top: -15px;
  right: 76px;
  border-width: 9px 9px;
  border-style: solid;
  border-radius: 4px;
  border-color: transparent transparent #ff7777 transparent;
  cursor: pointer;
}
.item-label.product {
  font-size: 13px;
  font-weight: bold;
}
.item-labelsao {
  color: red;
}
.input6-label {
  font-size: 14px;
}
.input6-label.countdown {
  font-size: 20px;
  font-weight: bold;
  color: rgb(9, 130, 243);
}
.input6-label.countdown.b {
  text-decoration: aqua;
}
</style>
