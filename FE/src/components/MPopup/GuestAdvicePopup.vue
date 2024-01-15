<template>
  <div class="cover-popup">
    <div class="popup">
      <div class="popup-top">
        <label for="">Thắc mắc về dịch vụ</label>
        <button class="btn-exit" @click="closePopup">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <div class="popup-center">
        <div class="popup-center-element">
          <div class="input-top">
            <label class="input-label">Tên của bạn</label>
            <label class="input-label sao">*</label>
          </div>
          <div class="input-bottom">
            <MInput
              :tab="1"
              :style="'min-height: 36px;'"
              :inValue="inValue_Name"
              :update:modelValue="inValue_Name"
              :maxlength="36"
              :placeholder="'Họ và tên '"
              @updateAlert="UpdateAlert"
              v-model="this.formData.fullName"
              @isSubmit="checkFullname"
            />
          </div>
        </div>
        <div class="popup-center-element">
          <div class="input-top">
            <label class="input-label">Số điện thoại</label>
            <label class="input-label sao">*</label>
          </div>
          <div class="input-bottom">
            <MInput
              :style="'min-height: 36px;'"
              :typeInput="'phonenumber'"
              :maxlength="36"
              :placeholder="'Số điện thoại'"
              @updateAlert="UpdateAlert"
              v-model="this.formData.phoneNumber"
              @isSubmit="checkPhone"
            />
          </div>
        </div>
        <div class="popup-center-element">
          <div class="input-top">
            <label class="input-label">Email</label>
            <label class="input-label sao">*</label>
          </div>
          <div class="input-bottom">
            <MInput
              :style="'min-height: 36px;'"
              :typeInput="'email'"
              :maxlength="36"
              :placeholder="'Địa chỉ email'"
              @updateAlert="UpdateAlert"
              v-model="this.formData.email"
              @isSubmit="checkEmail"
            />
          </div>
        </div>
        <div class="popup-center-element">
          <div class="input-top">
            <label class="input-label">Cơ sở bạn quan tâm</label>
            <label class="input-label sao">*</label>
          </div>
          <div class="input-bottom">
            <MDropItems
              :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
              :Drstyle="'width: 100%; top: 42px;right: 1px;'"
              :MDropSta="2"
              :label="'name'"
              @dropitem-value="getBranchId"
              :RecordDrop="listBranch"
            />
          </div>
        </div>
        <div class="popup-center-element">
          <div class="input-top">
            <label class="input-label"
              >Hãy cho chúng tôi biết về nhu cầu của bạn</label
            >
          </div>
          <div class="input-bottom">
            <MInput
              :style="'min-height: 36px;'"
              :inValue="inValue_Name"
              :update:modelValue="inValue_Name"
              :maxlength="200"
              :placeholder="'Thắc mắc của bạn'"
              @updateAlert="UpdateAlert"
              v-model="this.formData.note"
              @isSubmit="checkNote"
            />
          </div>
        </div>
      </div>
      <div class="popup-bottom">
        <div class="bottom-left">
          <MButton
            :text="'Gửi'"
            :class="'content-top-btn-content'"
            class="button-send"
            @click="this.sendData"
          />
        </div>
        <div class="bottom-right">
          <MButton
            :text="'Hủy'"
            :class="'btn-button-cancel'"
            @click="closePopup"
            class="button-cancel"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "../MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "GuestAdvicePopup",
  components: {
    MButton,
    MInput,
    MDropItems,
  },
  created() {
    this.loadBranch();
  },
  methods: {
    async loadBranch() {
      try {
        fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listBranch = data.content;
            this.listBranch.map(async (branch) => {
              branch.backgroundUrl = await fireStorage.displayImage(
                branch.backgroundUrl
              );
            });
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async sendData() {
      if (this.checkSubmitAll()) {
        const guestFormData = {
          name: this.formData.fullName,
          phoneNumber: this.formData.phoneNumber,
          email: this.formData.email,
          note: this.formData.note,
          branchId: this.formData.branchId,
        };
        try {
          fetch(
            `${apiConfig}/guest-consultant`,
            await fetchOptions("POST", guestFormData)
          )
            .then((res) => res.json())
            .then((response) => {
              if (response.id) {
                // Xử lý thành công
                this.ShowToast(1);
                this.closePopup();
              } else {
                this.ShowToast(3, response.message);
              }
            })
            .catch((res) => {
              console.log(res.message);
              this.ShowToast(3, "Thất bại");
            });
        } catch (error) {
          console.log(error);
          this.ShowToast(3, "Lỗi hệ thống");
        }
      }
    },
    getBranchId(selectedit) {
      this.formData.branchId = selectedit;
    },
    closePopup() {
      this.$emit("close-popup-click");
    },
    UpdateAlert() {
      //trường hợp nhập vào ô input
      this.isShowAlertName = false;
      this.inValue_Name = true;
      //trường hợp xóa ô input
      if (!this.formData.fullName) {
        this.inValue_Name = false;
      }
    },
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Gửi thành công",
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
    checkFullname(submit) {
      this.isSubmitFullName = submit;
    },
    checkPhone(submit) {
      this.isSubmitPhoneNumber = submit;
    },
    checkEmail(submit) {
      this.isSubmitEmail = submit;
    },
    checkNote(submit) {
      this.isSubmitNote = submit;
    },
    checkSubmitAll() {


      if (
        this.isSubmitEmail &&
        this.isSubmitPhoneNumber &&
        this.isSubmitFullName &&
        this.isSubmitNote
      ) {
        return true;
      } else return false;
    },
  },
  data() {
    return {
      listBranch: {},
      formData: {},
      inValue_Name: {
        type: Boolean,
        default: true,
      },
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
        isSubmitFullName: false,
        isSubmitPhoneNumber: false,
        isSubmitEmail: false,
        isSubmitNote: false,
      },
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
  height: 20%;
}
.input-top {
  width: 100%;
  height: 25%;
  text-align: left;
}
.input-label {
  color: #000;
  font-weight: bold;
}
.input-label.sao {
  color: red;
  margin-left: 2px;
}
.input-bottom {
  width: 100%;
  height: 75%;
}
.popup-bottom {
  width: 100%;
  height: 7%;
  display: flex;
  flex-wrap: wrap;
}
.bottom-left {
  height: 100%;
  width: 50%;
}
.bottom-right {
  height: 100%;
  width: 50%;
  display: flex;
  justify-content: right;
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