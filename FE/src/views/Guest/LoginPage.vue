<template>
  <div class="loginpage">
    <div class="content-right top">
      <div class="top-label">
        <label class="system-label">Đăng nhập</label>
      </div>
    </div>
    <div class="content-right center">
      <div class="center-input">
        <div class="input-login" ref="inputName">
          <MInput
            :tab="0"
            :inValue="inValue_Name"
            :maxlength="30"
            :placeholder="'Tên người dùng hoặc Email'"
            :typeInput="'username'"
            @isSubmit="handleIsSubmit"
            v-model="User.username"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertName">
            {{ AlertNameMess }}
          </div>
        </div>
        <div class="input-login" ref="inputName">
          <MInput
            :tab="1"
            :inValue="inValue_Code"
            :maxlength="18"
            :placeholder="'Mật khẩu'"
            :typeInput="'password'"
            v-model="User.password"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertCode">
            {{ AlertCodeMess }}
          </div>
        </div>
      </div>
      <div class="center-checkbox">
        <div class="button-checkbox">
          <m-checkbox
            @click="rememberClick"
            :stateRemember="rememberMe"
          ></m-checkbox>
          <label class="button-checkbox-label">Nhớ tôi</label>
        </div>
        <div class="link-forgot" @click="toForgotPassword">Quên mật khẩu</div>
      </div>
      <div class="center-button">
        <m-button
          :text="'Đăng nhập'"
          @click="Login"
          :class="'content-top-btn-content'"
          class="button-login"
        ></m-button>
      </div>
      <div class="signup-link">
        <div class="signup-link-label">
          <label>Bạn đã có tài khoản hay chưa?</label>
        </div>
        <div class="link-label" @click="toRegister">Đăng ký ngay</div>
      </div>
    </div>
    <div class="content-right bottom">
      <div class="signup-other">
        <div class="signup-other-label">
          <label class="signup-label">Hoặc đăng nhập với</label>
        </div>
        <div class="signup-other-button">
          <m-button-google></m-button-google>
          <m-button-facebook></m-button-facebook>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import MButton from "@/components/MButton/MButton.vue";
import MButtonGoogle from "@/components/MButton/MButtonGoogle.vue";
import MButtonFacebook from "@/components/MButton/MButtonFacebook.vue";
import MCheckbox from "@/components/MCheckbox/MCheckbox.vue";
import MInput from "@/components/MInput/MInput.vue";
import toast from "@/resouce/toast";
import { mapActions, mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  LOGIN_ACTION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
export default {
  name: "LoginPage",
  components: {
    MButton,
    MCheckbox,
    MInput,
    MButtonGoogle,
    MButtonFacebook,
  },
  beforeMount() {
    this.getLocal();
  },

  methods: {
    ...mapActions("auth", {
      login_action: LOGIN_ACTION,
    }),
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    getLocal() {
      const rememberMe = localStorage.getItem("rememberMe");
      if (rememberMe === "true") {
        const userName = localStorage.getItem("userName");
        const password = localStorage.getItem("password");
        this.User.username = userName;
        this.User.password = password;
        this.rememberMe = true;
        if (userName && password) {
          this.isSubmit = [true, true];
        }
      }
    },

    toRegister() {
      this.$emit("change-Page", 2);
      //this.User = {};
      this.UpdateAlert();
    },
    toForgotPassword() {
      this.$emit("change-Page", 3);
      //this.User = {};
      this.UpdateAlert();
    },
    rememberClick() {
      this.rememberMe = !this.rememberMe;
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
          message: toast.ToastMessAdd_sus,
        };

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: toast.ToastMessUpdate_sus,
        };
        //trường hợp toast hành động thất bại
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

    showAlert(code, message) {
      this.isShowAlertCode = true;
      this.inValue_Code = false;
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 404) {
        this.AlertNameMess = "Không tìm thấy tên người dùng";
        this.AlertCodeMess = "Sai mật khẩu";
      } else if (code == 100) {
        this.isShowAlertName = false;
        this.AlertCodeMess = "Không được để trống";
      } else if (code == 401) {
        this.isShowAlertName = false;
        this.AlertCodeMess = "Sai mật khẩu";
      } else {
        this.AlertNameMess = message;
        this.AlertCodeMess = message;
      }
    },

    /**
     * Hàm ẩn arlet sau khi người dùng nhập ô inpu
     * Nguyễn Văn Cương 22/11/2022
     */
    UpdateAlert() {
      //trường hợp nhập vào ô input
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      this.inValue_Name = true;
      this.inValue_Code = true;
    },

    /**
     * hàm kiểm tra có rỗng không
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.User.username || !this.User.password) {
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

    async Login() {
      await this.showLoading(true);
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      if (!this.rememberMe) {
        localStorage.removeItem("userName");
        localStorage.removeItem("password");
        localStorage.removeItem("rememberMe");
      }
      if (this.isSubmit.every((item) => item)) {
        try {
          await fetch(
            `${apiConfig}/api/auth/authenticate`,
            await fetchOptions("POST", this.User)
          )
            .then((res) => res.json().catch(() => res))
            .then((res) => {
              if (res.status) {
                if (res.status != "200") {
                  this.ShowToast(3, res.message);
                  this.showAlert(res.status);
                }
              } else {
                this.login_action(res);
                if (this.rememberMe) {
                  localStorage.setItem("userName", this.User.username);
                  localStorage.setItem("password", this.User.password);
                  localStorage.setItem("rememberMe", this.rememberMe);
                }

                this.ShowToast((this.ToastStatus = 1));
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Đăng nhập thất bại");
              this.showAlert(500);
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
          this.showAlert(500);
        }
        await this.showLoading(false);
      }
      await this.showLoading(false);
    },
  },
  data() {
    return {
      User: {},
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      //lưu trạng thái alert
      isShowAlertName: false,
      //lưu trạng thái alert
      isShowAlertCode: false,
      //hiển thị đỏ nhập mã khi trống
      inValue_Code: {
        type: Boolean,
        default: true,
      },
      //hiển thị đỏ nhập tên khi trống
      inValue_Name: {
        type: Boolean,
        default: true,
      },
      isSubmit: [false, false],
      AlertNameMess: "",
      AlertCodeMess: "",
      rememberMe: false,
    };
  },
};
</script>

<style scoped>
.loginpage {
  width: 100%;
  height: 100%;
  padding: 5% 0;
  border-radius: 10px;
  background-color: #fff;
}
.content-right {
  width: calc(100% - 50%);
  height: 100%;
  display: flex;
  margin-left: 20%;
}
.content-right.top {
  width: 100%;
  height: 10%;
}
.top-label {
  width: 45%;
  height: 100%;
  display: flex;
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
  font-size: 30px;
  font-weight: bold;
}
.center-input {
  width: 100%;
  height: 40%;
}
.input-login {
  width: 60%;
  height: 45%;
  position: relative;
}
.center-checkbox {
  display: flex;
}
.button-checkbox {
  width: 48%;
  height: 100%;
  display: flex;
}
.button-checkbox-label {
  margin-left: 10px;
}
.link-forgot {
  width: fit-content;
  height: fit-content;
  cursor: pointer;
  color: rgb(9, 130, 243);
}
.center-button {
  width: 100%;
  height: 15%;
  display: flex;
  align-items: center;
}
.button-login {
  width: 60%;
}
.signup-link {
  width: 100%;
  height: 15%;
  display: flex;
  padding-top: 15px;
}
.signup-link-label {
  width: 35%;
  height: 100%;
}
.link-label {
  cursor: pointer;
  color: rgb(9, 130, 243);
}
.content-right.bottom {
  width: 100%;
  height: 30%;
}
.signup-other {
  width: 100%;
  height: 40%;
}
.signup-other-label {
  width: 60%;
  font-size: 17px;
  white-space: nowrap;
}
.signup-other-label::before,
.signup-other-label::after {
  content: "123456";
  color: transparent;
  min-width: 100px;
  text-decoration: line-through black;
  margin: 0 5px;
}
.signup-other-button {
  width: 60%;
  display: flex;
  justify-content: center;
}

.alertInputEm {
  width: 70%;
  height: 16px;
  font-size: 12px;
  position: absolute;
  top: 76%;
  right: 12%;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 2px 4px;
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
  right: 45%;
  border-width: 9px 9px;
  border-style: solid;
  border-radius: 4px;
  border-color: transparent transparent #ff7777 transparent;
  cursor: pointer;
}
@media (max-width: 1200px) {
  .content-right {
    padding: 3%;
    width: 100%;
    margin-left: 0;
  }
  .top-label {
    width: 100%;
    display: unset;
  }
  .input-login {
    margin-left: 14%;
    width: 70%;
  }
  .center-button {
    display: unset;
  }

  .button-checkbox {
    width: 42%;
    margin-left: 14%;
  }

  .signup-link-label {
    margin-left: 0;
    width: 67%;
  }
  .content-right.bottom {
    margin-left: 10%;
    width: 95%;
  }
  .signup-other-button {
    width: 80%;
  }
}
</style>
