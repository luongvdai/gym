<template>
  <div class="container-changepass" v-show="isShow">
    <div class="content-changepass">
      <div class="label-title">
        <label class="title">Đổi mật khẩu </label>
      </div>
      <div class="oldpass">
        <m-input
          :inputLabel="'Mật khẩu cũ'"
          v-model="Password.oldpass"
          :inValue="inValue_OldPass"
        ></m-input>
        <div class="alertInputEm" v-if="isShowAlertOldPass">
          {{ AlertOldPassMess }}
        </div>
      </div>
      <div class="newpass">
        <m-input
          :inputLabel="'Mật khẩu mới'"
          v-model="Password.newpass"
          :inValue="inValue_NewPass"
        ></m-input>
        <div class="alertInputEm" v-if="isShowAlertNewPass">
          {{ AlertNewPassMess }}
        </div>
      </div>
      <div class="renewpass">
        <m-input
          :inputLabel="'Nhập lại khẩu mới'"
          v-model="Password.renewpass"
          :inValue="inValue_RenewPass"
        ></m-input>
        <div class="alertInputEm" v-if="isShowAlertCPass">
          {{ AlertCPassMess }}
        </div>
      </div>
      <div class="submit-change">
        <m-button :text="'Cập nhật'" @click="updatePassword"></m-button>
      </div>

      <div class="close-button" @click="closePopup">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          x="0px"
          y="0px"
          viewBox="0 0 50 50"
        >
          <path
            d="M 7.71875 6.28125 L 6.28125 7.71875 L 23.5625 25 L 6.28125 42.28125 L 7.71875 43.71875 L 25 26.4375 L 42.28125 43.71875 L 43.71875 42.28125 L 26.4375 25 L 43.71875 7.71875 L 42.28125 6.28125 L 25 23.5625 Z"
          ></path>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "../MButton/MButton.vue";
import MInput from "../MInput/MInput.vue";
import regex from "@/resouce/regex";
import toast from "@/resouce/toast";
import apiConfig from "@/api/config/apiConfig";


export default {
  components: { MInput, MButton },
  data() {
    return {
      Password: {},
      ToastStatus: 1,
      isShowAlertCPass: false,
      isShowAlertNewPass: false,
      isShowAlertOldPass: false,
      AlertCPassMess: "",
      AlertOldPassMess: "",
      AlertNewPassMess: "",
      inValue_OldPass: {
        type: Boolean,
        default: true,
      },
      inValue_NewPass: {
        type: Boolean,
        default: true,
      },
      inValue_RenewPass: {
        type: Boolean,
        default: true,
      },
    };
  },
  props: {
    isShow: {
      type: Boolean,
    },
  },
  methods: {
    closePopup() {
      const isShowUpdate = !this.isShow;
      this.$emit("closePopup", isShowUpdate);
    },
    updatePassword() {
      try {
        const apiUrl = `${apiConfig}/users/change-password`;
        //dữ liệu được gửi đi
        const data = {
          currentPassword: this.Password.oldpass,
          newPassword: this.Password.newpass,
          reNewPassword: this.Password.renewpass,
        };
        fetch(apiUrl, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
            Authorization:
              "Bearer " +
              JSON.parse(localStorage.getItem("userData")).access_token,
          },
          body: JSON.stringify(data),
        })
          .then((response) => {
            if (response.status == 200) {
              this.ShowToast(1,"success");
              // this.closePopup();
            } else {
              this.showAlert(400);
              this.ShowToast(2,response.message);
            }
          })
          .catch((error) => {
            console.log(error);
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3,"Đổi mật khẩu thất bại");
        this.showAlert(500);
      }
    },
    validateEmpty() {
      var validate = true;
      if (
        this.Password.oldpass == null ||
        this.Password.newpass == null ||
        this.Password.renewpass == null
      ) {
        this.showAlert(999);
        validate = false;
      }
      return validate;
    },
    validPassword(password) {
      var re = regex.RegexPassword;
      return re.test(password);
    },
    validateAll() {
      var validate = true;
      //kiểm tra xem mã nhân viên hoặc tên nhân viên có chưa

      if (
        !this.validPassword(this.Password.oldpass) &&
        this.Password.oldpass != null
      ) {
        this.showAlert(999);
        return (validate = false);
      }
      if (
        !this.validPassword(this.Password.newpass) &&
        this.Password.oldpass != null
      ) {
        this.showAlert(999);
        return (validate = false);
      }
      if (
        !this.validPassword(this.Password.renewpass) &&
        this.Password.renewpass != null
      ) {

        this.showAlert(999);
        return (validate = false);
      }
      if (this.Password.newpass != this.Password.renewpass) {
       
        this.showAlert(3);
        return (validate = false);
      }
      return validate;
    },
    showAlert(code) {
      this.inValue_OldPass = false;
      this.inValue_RenewPass = false;
      this.inValue_NewPass = false;
      if (code == 404) {
        this.isShowAlertCPass = true;
        this.AlertCPassMess = "cpass";
        this.AlertOldPassMess = "old pass";
        this.AlertNewPassMess = "new pass";
      } else if (code == 400) {
        this.isShowAlertOldPass = true;
        this.isShowAlertCPass = false;
        this.isShowAlertNewPass = false;
        this.AlertOldPassMess = "Mật khẩu hiện tại không đúng";
      } else if (code == 999) {
        this.isShowAlertOldPass = true;
        this.isShowAlertNewPass = true;
        this.isShowAlertCPass = true;
        this.AlertNewPassMess = "Tối thiểu 6 ký tự, 1 chữ, 1 số";
        this.AlertOldPassMess = "Tối thiểu 6 ký tự, 1 chữ, 1 số";
        this.AlertCPassMess = "Tối thiểu 6 ký tự, 1 chữ, 1 số";
      } else if (code == 3) {
        this.isShowAlertCPass = true;
        this.AlertCPassMess = "Nhập lại đúng mật khẩu";
      } else {
        this.isShowAlertCPass = true;
        this.isShowAlertNewPass = true;
        this.isShowAlertOldPass = true;
        this.AlertCPassMess = "Lỗi máy chủ";
        this.AlertNewPassMess = "Lỗi máy chủ";
        this.AlertOldPassMess = "Lỗi máy chủ";
      }
    },
    ShowToast(Tstatus,message) {
      //hiển thị toast
      this.isShowToast = true;
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.Toastcssicon = toast.Toastcssicon_sus;
        this.Toastcss = toast.Toastcss_sus;
        this.ToastMess_color = toast.ToastMess_color_sus;
        this.ToastMess = "Đổi mật khẩu thành công";

        //trường hợp toast cập nhật thành công
     
        //trường hợp toast hành động thất bại
      } else {
        this.Toastcssicon = toast.Toastcssicon_faild;
        this.Toastcss = toast.Toastcss_faild;
        this.ToastMess_color = toast.ToastMess_color_faild;
        this.ToastMess = message;
      }
    },
  },
};
</script>

<style scoped>
.container-changepass {
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
.content-changepass {
  position: relative;
  width: 500px;
  height: auto;
  border: 1px solid black;
  background-color: #ffff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  row-gap: 40px;
}
.label-title {
  flex: none;
  width: 100%;
  background-color: #feffed;
  border-bottom: 1px solid rgb(195, 191, 191);
  color: red;
  height: 20%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.title {
  font-size: 20px;
  font-weight: bold;
}
.oldpass,
.newpass,
.renewpass {
  position: relative;
  width: 50%;
}
.submit-change {
  width: 25%;
  margin-bottom: 20px;
}
.close-button {
  position: fixed;
  top: 0;
  right: 0;
  height: 20px;
  width: 20px;
  padding-left: 2px;
  padding-bottom: 2px;
}
.close-button:hover {
  background-color: rgb(255, 67, 67);
}
.alertInputEm {
  width: 100%;
  height: 15px;
  font-size: 12px;
  position: absolute;
  right: 0%;
  bottom: -60%;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 0px 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
}
.alertInputEm.b {
  right: 10px;
}
.alertInputEm::after {
  content: " ";
  position: absolute;
  top: -15px;
  right: 0%;
  border-width: 9px 9px;
  border-style: solid;
  border-radius: 4px;
  border-color: transparent transparent #ff7777 transparent;
  cursor: pointer;
}
</style>
