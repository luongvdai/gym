<template>
  <div class="forgotpage">
    <div class="content-right top">
      <div class="top-label">
        <label class="system-label">Quên mật khẩu</label>
      </div>
    </div>
    <div class="content-right center">
      <div class="center-input">
        <div class="input-login">
          <MInput
            :tab="0"
            :maxlength="30"
            :placeholder="'Tên người dùng'"
            :typeInput="'username'"
            v-model="User.username"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertName">
            {{ AlertNameMess }}
          </div>
        </div>
        <div class="input-login">
          <MInput
            :tab="1"
            :maxlength="100"
            :placeholder="'Email'"
            :typeInput="'email'"
            v-model="User.email"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertCode">
            {{ AlertCodeMess }}
          </div>
        </div>
      </div>
      <div class="center-button">
        <m-button
          :text="'Đặt lại mật khẩu'"
          :class="'content-top-btn-content'"
          class="button-login"
          @click="openPopupEdit"
        ></m-button>
      </div>
      <div class="center-checkbox">
        <div class="button-checkbox">
          <label class="button-checkbox-label"
            >Quên mật khẩu? Vui lòng nhập tên đăng nhập và địa chỉ email.
          </label>
          <label class="button-checkbox-label"
            >Bạn sẽ nhận được một liên kết tạo mật khẩu mới qua email.</label
          >
        </div>
        <div class="backtologin">
          <label class="signin-label" @click="toLogin">Quay về đăng nhập</label>
        </div>
      </div>
      <MPopupEdit
        v-if="isShowEdit"
        :PopupEdit_label="'Đổi mật khẩu'"
        :recordvalue="UnitValue"
        :height="'height: 350px;'"
        :inputShow="2"
        :userName="User.username"
        @close-product-popup="closeProductPopup"
        @send-code="openPopupEdit"
      />
    </div>
  </div>
</template>
<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import MPopupEdit from "@/components/MPopupEdit/MPopupEdit.vue";
import toast from "@/resouce/toast";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";

export default {
  name: "ForgotPasswordPage",
  components: {
    MButton,
    MInput,
    MPopupEdit,
  },
  methods: {
    toLogin() {
      this.$emit("change-Page", 1);
      this.User = {};
      this.UpdateAlert();
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
    },

    showAlert(code) {
      this.isShowAlertCode = true;
      this.inValue_Code = false;
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 404) {
        this.isShowAlertCode = false;
        this.AlertNameMess = "Không tìm thấy tên người dùng";
      }else if (code == 100) {
        this.isShowAlertName = false;
        this.AlertCodeMess = "Không được để trống";
      } else if (code == 400) {
        this.isShowAlertName = false;
        this.AlertCodeMess = "Email không khớp với tài khoản";
      } else {
        this.AlertNameMess = "Lỗi máy chủ";
        this.AlertCodeMess = "Lỗi máy chủ";
      }
    },

    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus) {
      //hiển thị toast
      this.isShowToast = true;
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.Toastcssicon = toast.Toastcssicon_sus;
        this.Toastcss = toast.Toastcss_sus;
        this.ToastMess_color = toast.ToastMess_color_sus;
        this.ToastMess = "Gửi email thành công";

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.Toastcssicon = toast.Toastcssicon_sus;
        this.Toastcss = toast.Toastcss_sus;
        this.ToastMess_color = toast.ToastMess_color_sus;
        this.ToastMess = toast.ToastMessUpdate_sus;

        //trường hợp toast hành động thất bại
      } else {
        this.Toastcssicon = toast.Toastcssicon_faild;
        this.Toastcss = toast.Toastcss_faild;
        this.ToastMess_color = toast.ToastMess_color_faild;
        this.ToastMess = "Gửi email thất bại";
      }
      this.$emit(
        "show-toast",
        this.Toastcssicon,
        this.Toastcss,
        this.ToastMess_color,
        this.ToastMess
      );
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.User.username || !this.User.email) {
        validate = false;
      }
      return validate;
    },

    handleIsSubmit(isValid, index) {
      if (index >= 0 && index < this.isSubmit.length) {
          // Thay đổi giá trị của phần tử tại index trực tiếp
          this.isSubmit[index] = isValid;

          // Đối với Vue.js phiên bản 2.1.8 trở lên, bạn có thể sử dụng $forceUpdate để kích thích lại
          // this.$forceUpdate();
        } else {
          console.error("Index is out of range");
        }
    },

    async openPopupEdit() {
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      if (this.isSubmit.every((item) => item)) {
        try {
          fetch(
            `${apiConfig}/users/request-new-password?email=${this.User.email}&username=${this.User.username}`,
            await fetchOptions("GET")
          )
            .then((res) => {
              if (res.status != "200") {
                this.ShowToast((this.ToastStatus = 3));
                this.showAlert(res.status);
              } else {
                this.ShowToast((this.ToastStatus = 1));
                this.isShowEdit = true;
              }
            })
            .catch((res) => {
              console.log(res);
              this.ShowToast((this.ToastStatus = 3));
              this.showAlert(res.status);
            });
        } catch (error) {
          console.log(error);
          this.ShowToast((this.ToastStatus = 3));
          this.showAlert(500);
        }
      }else{
        this.showAlert(100);
      }
    },
    

    closeProductPopup() {
      this.isShowEdit = false;
    },
  },
  data() {
    return {
      isShowEdit: false,
      User: {},
      isShowAlertCode: false,
      isShowAlertName: false,
      AlertNameMess: "",
      AlertCodeMess: "",
      isSubmit: [false, false],
    };
  },
};
</script>
<style scoped>
.forgotpage {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  background-color: #fff;
}
.content-right {
  width: calc(100% - 50%);
  height: 100%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  margin-left: 6%;
}
.content-right.top {
  width: 100%;
  height: 10%;
  margin-top: 5%;
}
.top-label {
  width: 45%;
  height: 100%;
}
.system-label {
  font-size: 30px;
  font-weight: bold;
}
.content-right.center {
  width: 100%;
  height: 45%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
}
.center-label {
  width: 100%;
  height: 10%;
  text-align: left;
}
.login-label {
  margin-left: 100px;
  font-size: 30px;
  font-weight: bold;
}
.center-input {
  width: 100%;
  height: 35%;
}
.input-login {
  width: 60%;
  height: 50%;
  margin-left: 100px;
  position: relative;
}
.center-button {
  width: 84%;
  height: 20%;
  align-content: center;
}
.button-login {
  width: 70%;
}
.center-checkbox {
  width: 89%;
  height: 15%;
}
.button-checkbox {
  width: 100%;
  height: 100%;
  align-content: center;
  padding: 0 20%;
}
.button-checkbox-label {
  width: 100%;
}
.backtologin {
  width: 49%;
  height: 100%;
  margin-top: 2%;
}
.signin-label {
  color: rgb(9, 130, 243);
  cursor: pointer;
}
.alertInputEm {
  width: 230px;
  height: 16px;
  font-size: 12px;
  position: absolute;
  top: 40px;
  right: 110px;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 0px 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
}
.alertInputEm.b {
  right: 40px;
}
.alertInputEm::after {
  content: " ";
  position: absolute;
  top: -15px;
  right: 100px;
  border-width: 9px 9px;
  border-style: solid;
  border-radius: 4px;
  border-color: transparent transparent #ff7777 transparent;
  cursor: pointer;
}
@media (max-width: 1200px) {
  .content-right.center {
    margin-left: 0px;
  }
  .input-login {
    margin-left: 14%;
    width: 70%;
  }
  .center-button {
    display: unset;
    width: 98%;
  }
  .top-label {
    width: 88%;
  }
  .button-login {
    margin-right: 0%;
  }
  .button-checkbox {
    margin-left: 0%;
    padding: 0px 5px;
    font-size: 13px;
  }
  .backtologin {
    width: 100%;
    height: 100%;
    margin-top: 3%;
    padding-left: 4%;
    display: flex;
    align-items: center;
  }
  .alertInputEm {
    right: 16px;
  }
  .center-checkbox {
    width: 100%;
  }
}
</style>
