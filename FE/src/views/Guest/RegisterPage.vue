<template>
  <div class="registerpage">
    <div class="content-right top">
      <div class="top-label">
        <label class="system-label">Đăng ký</label>
      </div>
    </div>
    <div class="register-content-right center">
      <div class="register-center-input">
        <div class="register-input-login">
          <MInput
            :tab="0"
            :maxlength="100"
            :placeholder="'Tên đăng nhập'"
            :typeInput="'username'"
            v-model="User.username"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
        </div>
        <div class="register-input-login">
          <MInput
            :tab="1"
            :maxlength="100"
            :placeholder="'Họ và tên'"
            :typeInput="'text'"
            v-model="User.fullName"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
        </div>
        <div class="register-input-login">
          <MInput
            :tab="2"
            :maxlength="100"
            :placeholder="'Số điện thoại'"
            :typeInput="'phonenumber'"
            v-model="User.phonenumber"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
        </div>
        <div class="register-input-login">
          <MInput
            :tab="3"
            :maxlength="100"
            :placeholder="'Email'"
            :typeInput="'email'"
            v-model="User.email"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
        </div>
        <div class="register-input-login mdatetime">
          <div class="input-datetime">
            <MDatetime @getDateTime="getDate" />
            <div class="alertInputEm b" v-if="isShowAlertDate">
              {{ AlertDateMess }}
            </div>
          </div>
          <div class="input-dropdown">
            <MDropItems
              @dropitem-value="getGender"
              :labeldrop="'Giới tính'"
              :Drstyle="'width: 100%'"
              :label="'name'"
              :MDropSta="2"
              :RecordDrop="GenderDropItems"
            />
          </div>
        </div>
        <div class="register-input-login">
          <MInput
            :tab="4"
            :maxlength="100"
            :placeholder="'Địa chỉ'"
            :typeInput="'address'"
            v-model="User.address"
            :inValue="inValue_Name"
            :autocomplete="'off'"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
        </div>
        <div class="register-input-login">
          <MDropItems
            v-if="isDropItems"
            @dropitem-value="getBranch"
            :labeldrop="'Chi nhánh muốn đăng ký'"
            :Drstyle="'width: 100%'"
            :label="'name'"
            :MDropSta="2"
            :RecordDrop="Branches"
          />
        </div>
        <div class="register-input-login">
          <MInput
            :tab="5"
            :maxlength="100"
            :placeholder="'Mật khẩu'"
            :typeInput="'password'"
            v-model="User.password"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertCode">
            {{ AlertCodeMess }}
          </div>
        </div>
        <div class="register-input-login">
          <MInput
            :tab="6"
            :maxlength="100"
            :placeholder="'Nhập lại mật khẩu'"
            :typeInput="'password'"
            v-model="User.confirmpassword"
            :inValue="inValue_Name"
            @isSubmit="handleIsSubmit"
            @updateAlert="UpdateAlert"
          />
          <div class="alertInputEm" v-if="isShowAlertCPass">
            {{ AlertCPassMess }}
          </div>
        </div>
      </div>
      <div class="register-center-button">
        <m-button
          :text="'Đăng ký'"
          :class="'content-top-btn-content'"
          class="register-button-login"
          @click="registerClick"
        ></m-button>
      </div>
      <div class="register-signup-link">
        <div class="register-signup-link-label">
          <div class="link-label" @click="toLogin">Tôi đã có tài khoản</div>
        </div>
      </div>
    </div>
    <div class="content-right bottom">
      <div class="signup-other">
        <div class="signup-other-label">
          <label>Đăng ký bằng:</label>
        </div>
        <div class="signup-other-button">
          <m-button-google class="google-btn"></m-button-google>
          <m-button-facebook class="facebook-btn"></m-button-facebook>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import MButton from "@/components/MButton/MButton.vue";
import MButtonGoogle from "@/components/MButton/MButtonGoogle.vue";
import MButtonFacebook from "@/components/MButton/MButtonFacebook.vue";
import MInput from "@/components/MInput/MInput.vue";
import MDatetime from "@/components/MDatetime/MDatetime.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import apiConfig from "@/api/config/apiConfig";
import toast from "@/resouce/toast";
import notification from "@/resouce/notification";
import fetchOptions from "@/api/base/fetchOptions";
import { mapActions, mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  LOGIN_ACTION,
  TOAST_SHOW_MUTATION,
  SEND_NOTIFICATION_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "RegisterPage",
  components: {
    MButton,
    MInput,
    MButtonGoogle,
    MButtonFacebook,
    MDatetime,
    MDropItems,
  },
  created() {
    this.loadBranch();
  },
  methods: {
    ...mapMutations({
      sendNotification: SEND_NOTIFICATION_MUTATION,
    }),
    ...mapActions("auth", {
      login_action: LOGIN_ACTION,
    }),
    toLogin() {
      this.$emit("change-Page", 1);
      this.UpdateAlert();
    },
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    /**
     * hàm mở popup hỏi người dùng có xóa không
     *  Nguyễn Văn Cương 25/09/2022
     */
    getGender(selectedit) {
      console.log(selectedit);
      this.User.gender = selectedit;
    },
    getDate(date) {
      this.User.dateofbirth = date;
    },
    getBranch(branchid) {
      this.selectedBranch = branchid;
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
          message: "Thêm thành công",
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

    showAlert(code, message) {
      this.inValue_Name = false;
      if (code == 404) {
        (this.isShowAlertDate = true),
          (this.AlertDateMess = "Không được để trống");
      } else if (code == 401) {
        this.isShowAlertCode = true;
        this.AlertCodeMess = "Sai mật khẩu";
      } else if (code == 3) {
        this.isShowAlertCPass = true;
        this.AlertCPassMess = "Nhập lại đúng mật khẩu";
      } else if (code == 400) {
        this.isShowAlertCPass = true;
        this.AlertCPassMess = message;
      } else {
        this.isShowAlertCode = true;
        this.AlertCodeMess = "Lỗi máy chủ";
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
      this.isShowAlertAddress = false;
      this.isShowAlertCPass = false;

      this.inValue_Name = true;
      this.inValue_Code = true;
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (
        !this.User.gender ||
        !this.User.dateofbirth ||
        !this.selectedBranch
      ) {
        validate = false;
        this.showAlert(404);
      }
      return validate;
    },

    /**
     * hàm validate dữ liệu
     */
    validateAll() {
      var validate = true;
      //kiểm tra xem mã nhân viên hoặc tên nhân viên có chưa
      if (this.User.password != this.User.confirmpassword) {
        this.ShowToast(3, "Kiểm tra lại dữ liệu nhập vào");
        this.showAlert(3);
        return validate == false;
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

    async registerClick() {
      this.isShowAlertName = false;
      this.isShowAlertCode = false;
      if (this.validateEmpty() == true && this.validateAll() == true && this.isSubmit.every((item) => item)) {
        try {
          await fetch(
            `${apiConfig}/api/auth/register/`,
            await fetchOptions("POST", {
              username: this.User.username,
              password: this.User.password,
              fullName: this.User.fullName,
              email: this.User.email,
              phoneNumber: this.User.phonenumber,
              address: this.User.address,
              gender: this.User.gender,
              dateOfBirth: this.User.dateofbirth,
              branchId: this.selectedBranch,
            })
          )
            .then((res) => res.json())
            .then(async (res) => {
              if (res.status) {
                if (res.status != "200") {
                  this.ShowToast(3, res.message);
                  this.showAlert(res.status, res.message);
                }
              } else {
                this.ShowToast(1);
                await this.login_action(res);
                await fetch(
                  `${apiConfig}/users/my-profile`,
                  await fetchOptions("GET")
                )
                  .then((data) => data.json().catch(() => data))
                  .then((response) => {
                    this.sendNotification({
                      content: notification.REGISTER,
                      receiverId: response.id,
                    });
                  })
                  .catch((res) => {
                    console.log(res);
                  });
              }
            })
            .catch((res) => {
             console.log(res.message);
            this.ShowToast(3, "Đăng ký thất bại");
              this.showAlert(500);
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
          this.showAlert(500);
        }
      }
    },
    /**
     * hàm load dữ liệu
     */
    async loadBranch() {
      try {
        fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.Branches = data.content; //lưu dữ liệu
          this.isDropItems = true;
        })
        .catch((res) => {
          console.log(res);
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    
  },
  data() {
    return {
      User: {},
      Branches: [],
      isDropItems: false,
      isShowToast: false,
      selectedBranch: null,
      ToastStatus: 1,
      ToastMess: {},
      ToastMess_color: {},
      Toastcss: {},
      Toastcssicon: {},
      //lưu trạng thái alert
      isShowAlertName: false,
      //lưu trạng thái alert
      isShowAlertCode: false,
      isShowAlertCPass: false,
      isShowAlertAddress: false,
      isShowAlertDate: false,
      isShowAlertGender: false,
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
      GenderDropItems: [
        { name: "Nam", id: 1 },
        { name: "Nữ", id: 2 },
      ],
      AlertNameMess: "",
      AlertCodeMess: "",
      AlertPhoneMess: "",
      AlertAddressMess: "",
      AlertCPassMess: "",
      AlertDateMess: "",
      AlertGenderMess: "",
      isSubmit: [false, false, false, false, false, false, false],
    };
  },
};
</script>

<style scoped>
.registerpage {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  background-color: #fff;
}
.register-content-right {
  width: calc(100% - 50%);
  height: 100%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  margin-left: 6%;
}
.content-left {
  width: calc(100% - 60%);
  height: 100%;
}
.register-img {
  width: 100%;
  height: 100%;
}
.content-right.top {
  width: 100%;
  height: 8%;
}
.login-img {
  margin-left: 27.4%;
  width: 90.3%;
  height: 90%;
}
.top-label {
  width: 51%;
  height: 100%;
  align-items: center;
  display: flex;
  padding-left: 17%;
}
.system-label {
  font-size: 30px;
  font-weight: bold;
}
.register-content-right.center {
  width: 100%;
  height: 77%;
  display: flex;
  padding-left: 11%;
}

.login-label {
  margin-left: 100px;
  font-size: 20px;
}
.register-center-input {
  margin-top: 10px;
  width: 100%;
  height: 84%;
  position: relative;
}
.register-input-login {
  width: 60%;
  height: 11%;
  position: relative;
}
.register-input-login.mdatetime {
  width: 60%;
  height: 12%;
  display: flex;
}
.input-datetime {
  width: 35%;
  height: 50%;
  position: relative;
}
.input-dropdown {
  width: 35%;
  height: 50%;
  margin-left: 30%;
}
.center-checkbox {
  width: 100%;
  height: 15%;
  display: flex;
}
.button-checkbox {
  width: 42%;
  height: 100%;
  margin-left: 100px;
  display: flex;
}
.button-checkbox-label {
  margin-left: 10px;
}
.link-forgot {
  width: 20%;
  height: 100%;
  text-align: right;
}
.register-center-button {
  width: 100%;
  height: 8%;
  display: flex;
  align-content: center;
  margin-top: 5px;
}
.register-button-login {
  width: 60%;
}
.register-signup-link {
  width: 100%;
  height: 5%;
}
.register-signup-link-label {
  width: 60%;
  height: 100%;
  text-align: center;
}
.content-right.bottom {
  width: 100%;
  height: 25%;
}
.signup-other {
  width: 100%;
  height: 40%;
}
.signup-other-label {
  width: 100%;
  height: 50%;
  text-align: left;
  margin-left: 18%;
}
.signup-other-button {
  width: 100%;
  display: flex;
}
.signup-other-label {
  font-size: 17px;
}
.google-btn {
  margin-left: 17%;
  margin-right: 3%;
}
.link-label {
  cursor: pointer;
  color: rgb(9, 130, 243);
}
.alertInputEm {
  width: 70%;
  height: 16px;
  font-size: 12px;
  position: absolute;
  top: 76%;
  right: 15%;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 0 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
}
.alertInputEm.b {
  top: 154%;
  right: -104%;
  width: 250px;
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
  .register-content-right.center {
    margin-left: 0px;
    height: 74%;
  }
  .register-input-login,
  .register-input-login.mdatetime {
    width: 88%;
  }
  .register-button-login{
    width: 88%;
  }
  .link-label {
    margin-left: 20%;
  }
  .signup-other-label {
    margin-left: 8%;
  }
  .top-label{
    width: 100%;
    height: 100%;
    display: block;
    padding: 4%;
  }
  .register-center-input{
    height: 84%;
  }
  .register-input-login{
    width: 88%;
    margin: 0;
  }.input-dropdown{
    width: 65%;
    height: 50%;
    margin-left: 13%;
  }.input-datetime{
    width: 70%;
  }.register-signup-link-label{
    width: 73%;
  }.google-btn{
    margin-left: 35%;
  }
}
</style>
