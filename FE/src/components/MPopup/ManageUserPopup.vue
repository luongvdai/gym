<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "người dùng" }}</label>
          </div>
        </div>
        <div class="popup-top-right">
          <div class="popup-top-right btn">
            <button class="btn-exit" @click="closePopup">
              <i class="ti ti-x"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="popup-center">
        <div class="popup-center-left">
          <div class="popup-center-left-input b">
            <label class="label-infor">Thông tin người dùng</label>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tên đăng nhập </label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Username"
                  :update:modelValue="inValue_Username"
                  :maxlength="36"
                  :typeInput="'username'"
                  :placeholder="'Tên đăng nhâp '"
                  @updateAlert="UpdateAlert"
                  v-model="User.username"
                  @isSubmit="checkSubmitUsername"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Mật khẩu</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Password"
                  :update:modelValue="inValue_Password"
                  :maxlength="36"
                  :typeInput="'password'"
                  :placeholder="'Mật khẩu '"
                  @updateAlert="UpdateAlert"
                  v-model="User.password"
                  @isSubmit="checkSubmitPassword"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tên </label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :update:modelValue="inValue_Name"
                  :maxlength="36"
                  :typeInput="'text'"
                  :placeholder="'Họ và tên '"
                  @updateAlert="UpdateAlert"
                  v-model="User.fullName"
                  @isSubmit="checkSubmitFullname"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Vai trò</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MDropItems
                  :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                  :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                  :MDropSta="2"
                  :label="'position'"
                  @dropitem-value="getRoleId"
                  :RecordDrop="listRoleData"
                  :DropItemsModel="User.roleId"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Email</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :style="'min-height: 36px;'"
                  :tab="1"
                  :inValue="inValue_Email"
                  :update:modelValue="inValue_Email"
                  :typeInput="'email'"
                  :maxlength="30"
                  :placeholder="'Email'"
                  v-model="User.email"
                  @isSubmit="checkSubmitEmail"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">SĐT</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :style="'min-height: 36px;'"
                  :tab="1"
                  :inValue="inValue_Phone"
                  :update:modelValue="inValue_Phone"
                  :maxlength="30"
                  :placeholder="'Số điện thoại'"
                  @updateAlert="UpdateAlert"
                  v-model="User.phoneNumber"
                  :typeInput="'phonenumber'"
                  @isSubmit="checkSubmitPhonenumber"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Địa chỉ </label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Address"
                  :maxlength="100"
                  :typeInput="'address'"
                  :placeholder="'Địa chỉ '"
                  @updateAlert="UpdateAlert"
                  v-model="User.address"
                  @isSubmit="checkSubmitAddress"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Ngày sinh </label>
              </div>
              <div class="input-bottom c">
                <MDatetime
                  v-model="User.dateOfBirth"
                  @getDateTime="getDate"
                  :style="'min-height: 36px;'"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Giới tính</label>
              </div>
              <div class="input-bottom b input-gender">
                <m-input-radio
                  name="gender"
                  :label="'Nam'"
                  :checked="User.gender == true"
                  @click="getGender(true)"
                >
                </m-input-radio>
                <m-input-radio
                  name="gender"
                  :label="'Nữ'"
                  :checked="User.gender == false"
                  @click="getGender(false)"
                ></m-input-radio>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Chi nhánh</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MDropItems
                  :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                  :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                  :MDropSta="2"
                  :label="'name'"
                  @dropitem-value="getBranchId"
                  :RecordDrop="listBranchData"
                  :DropItemsModel="User.branchId"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input c">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Kích hoạt</label>
              </div>
              <div class="input-bottom">
                <MDropItems
                  :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                  :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                  :MDropSta="2"
                  :label="'name'"
                  @dropitem-value="getIsVerify"
                  :RecordDrop="listIsVerify"
                  :DropItemsModel="User.isVerify"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Mô tả</label>
              </div>
              <div class="input-bottom">
                <v-textarea
                  counter
                  class="input-bottom-textarea"
                  rows="3"
                  v-model="User.description"
                  variant="outlined"
                  style="height: 100px"
                ></v-textarea>
              </div>
            </div>
          </div>

          <div class="popup-center-left-input">
            <div class="input-add">
              <MButton
                :text="popupLabel + 'người dùng'"
                @click="checkAddOrUpdate"
                class="button-add"
                :class="'content-top-btn-content'"
              />
            </div>
            <div class="input-cancel">
              <MButton
                :text="'Hủy'"
                @click="closePopup"
                class="btn-add-img"
                :class="'btn-button-cancel'"
              />
            </div>
          </div>
        </div>
        <div class="popup-center-right">
          <div class="popup-center-right-label">
            <label class="label-img">Ảnh đại diện</label>
          </div>
          <div class="popup-center-right-img">
            <div class="popup-img" @click="openImg(urlImage)">
              <MTooltip :content="'Click xem ảnh'" />
              <img :src="urlImage" v-if="urlImage" />
              <img src="@\assets\photos\2.jpg" v-if="!urlImage" />
            </div>
          </div>
          <div class="popup-center-right-btn">
            <input
              @change="handleFileSelect"
              type="file"
              ref="fileImage"
              class="d-none"
              accept="image/jpeg, image/png"
            />
            <MButton
              @click="openFileInput"
              :text="popupLabel + 'ảnh'"
              class="btn-add-img"
              :class="'content-top-btn-content'"
            />
            <div class="btn-delete-img" @click="openPopupAsk">
              <MTooltip :content="'Click để xóa ảnh'" />
              <i class="ti ti-trash"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <MPopupViewImgVue
      v-if="isOpenImg"
      :urlImg="urlImage"
      @close-popupimg-click="closePopupImg"
    />
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskDelete"
        :record="'ảnh'"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="deleteImg"
        @close-notification-click="ClosePopupAsk"
        :getRecordCode="'này'"
        :MPopupN="2"
      />
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MInputRadio from "@/components/MInputRadio/MInputRadio.vue";
import MDatetime from "@/components/MDatetime/MDatetime.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import apiConfig from "@/api/config/apiConfig";
// import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import fireStorage from "@/plugins/fireStorage";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "UserPopup",
  components: {
    MInput,
    MButton,
    MInputRadio,
    MDatetime,
    MDropItems,
    MPopupViewImgVue,
    MTooltip,
    MPopupNotification,
  },
  props: {
    userSelected: Object,
    Mode: Number,
    popupLabel: String,
    listRole: {},
    listBranch: {},
  },
  created() {
    try {
      this.listRoleData = this.listRole;
    this.listBranchData = this.listBranch;
    if (this.userSelected) {
      this.User = { ...this.userSelected };
      //this.User.dateOfBirth = this.User.dateOfBirth / 1000;
      this.isSubmitUsername = true;
      this.isSubmitAddress = true;
      this.isSubmitEmail = true;
      this.isSubmitFullname = true;
      this.isSubmitPassword = true;
      this.isSubmitPhonenumber = true;

      this.stylePass =
        "min-height: 36px;cursor: no-drop; background-color: rgb(227 227 227)";
      (async () => {
        this.urlImage = await fireStorage.displayImage(this.User.avatar);
      })();
    } else {
      this.stylePass = "min-height: 36px;";
    }
    } catch (error) {
      console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
    }
  },
  methods: {
    checkAddOrUpdate() {
      try {
        if (this.User.id) {
          this.updateUser();
        } else {
          this.addUser();
        }
      } catch (error) {
        this.ShowToast(3, error);
      }
    },

    /**
     * Hàm thêm user
     */
    async addUser() {
      if (this.checkSubmitAll()) {
        let imageUrl = await fireStorage.getURL(
          "user",
          Date.now(),
          this.$refs.fileImage.files[0]
        );

        // Dữ liệu người dùng để tạo
        const userInfo = {
          username: this.User.username,
          password: this.User.password,
          fullName: this.User.fullName,
          email: this.User.email,
          phoneNumber: this.User.phoneNumber,
          address: this.User.address,
          gender: this.User.gender,
          dateOfBirth: this.User.dateOfBirth,
          avatar: imageUrl,
          roleId: this.User.roleId,
          branchId: this.User.branchId,
          isActive: true,
          isVerify: this.User.isVerify,
          description: this.User.description,
        };

        try {
          fetch(`${apiConfig}/manage/users/`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization:
                "Bearer " +
                JSON.parse(localStorage.getItem("userData")).access_token,
            },
            body: JSON.stringify(userInfo),
          })
            .then((response) => response.json().catch(() => response))
            .then((response) => {
              if (response.id) {
                // Xử lý thành công
                this.ShowToast(1);
                this.closePopup();
                this.$emit("load-data");
              } else {
                this.ShowToast(3, response.message);
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        }
      }
    },

    /**
     * Hàm cập nhật user
     */
    async updateUser() {
      if(!this.User.password){
        this.User.password = "123xx45yy";
      }
      this.isSubmitPassword = true;
      if (this.checkSubmitAll()) {
        let imageUrl =
          this.isImageUpdate == true
            ? await fireStorage.getURL(
                "user",
                Date.now(),
                this.$refs.fileImage.files[0]
              )
            : this.User.avatar;
        const userData = {
          username: this.User.username,
          password: this.User.password,
          fullName: this.User.fullName,
          email: this.User.email,
          phoneNumber: this.User.phoneNumber,
          address: this.User.address,
          gender: this.User.gender,
          dateOfBirth: this.User.dateOfBirth,
          roleId: this.User.roleId,
          avatar: imageUrl,
          branchId: this.User.branchId,
          isVerify: this.User.isVerify,
          isActive: this.User.isActive,
          description: this.User.description,
        };
        try {
          fetch(`${apiConfig}/manage/users/${this.User.id}`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization:
                "Bearer " +
                JSON.parse(localStorage.getItem("userData")).access_token,
            },
            body: JSON.stringify(userData),
          })
            .then((res) => res.json())
            .then((response) => {
              if (!response.id) {
                this.ShowToast(3, response.message);
              } else {
                // Xử lý thành công
                this.ShowToast(2);
                this.closePopup();
                this.$emit("load-data");
                if (this.isImageUpdate) {
                  (async () => {
                    await fireStorage.deleteImage(this.User.avatar);
                  })();
                }
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        }
      }
    },

    /**
     * Hàm chọn file ảnh
     */
    handleFileSelect(event) {
      const file = event.target.files[0];
      if (file && this.isImageFile(file)) {
        this.urlImage = URL.createObjectURL(file);
        this.isImageUpdate = this.Mode === 2 ? true : false;
      } else {
        confirm("Chọn file hình ảnh");
      }
    },
    openFileInput() {
      this.$refs.fileImage.click();
    },
    isImageFile(file) {
      const acceptedImageTypes = ["image/jpeg", "image/png"];
      return file && acceptedImageTypes.includes(file.type);
    },
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    closePopup() {
      this.$emit("close-popup-click");
    },
    closePopupImg() {
      this.isOpenImg = false;
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
    },
    getRoleId(selectedit) {
      this.User.roleId = selectedit;
    },
    getBranchId(selectedit) {
      this.User.branchId = selectedit;
    },
    getIsVerify(selectedit) {
      this.User.isVerify = selectedit;
    },
    checkSubmitUsername(isSubmit) {
      this.isSubmitUsername = isSubmit;
    },
    checkSubmitPassword(isSubmit) {
      this.isSubmitPassword = isSubmit;
    },
    checkSubmitFullname(isSubmit) {
      this.isSubmitFullname = isSubmit;
    },
    checkSubmitAddress(isSubmit) {
      this.isSubmitAddress = isSubmit;
    },
    checkSubmitEmail(isSubmit) {
      this.isSubmitEmail = isSubmit;
    },
    checkSubmitPhonenumber(isSubmit) {
      this.isSubmitPhonenumber = isSubmit;
    },
    checkSubmitAll() {
      if (
        !this.isSubmitUsername ||
        !this.isSubmitPassword ||
        !this.isSubmitFullname ||
        !this.isSubmitAddress ||
        !this.isSubmitPhonenumber ||
        !this.isSubmitEmail
      ) {
        return false;
      } else return true;
    },
    /**
     * Hàm xóa ảnh
     */
    deleteImg() {
      this.User.avatar = null;
      this.isShowAskDelete = false; //hiện popup hỏi người dùng
      this.urlImage = null;
      //this.updateUser();
    },

    /**
     * hàm mở popup hỏi người dùng có xóa không
     */
    openPopupAsk() {
      if (this.urlImage) {
        this.isShowAskDelete = true; //hiện popup hỏi người dùng
      } else {
        this.showAlert(304);
      }
    },
    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
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
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
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

    showAlert(code) {
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 404) {
        this.AlertNameMess = "Không tìm thấy";
      } else if (code == 304) {
        this.AlertNameMess = "Chưa có ảnh";
      } else {
        this.AlertNameMess = "Lỗi máy chủ";
      }
    },

    /**
     * Hàm ẩn arlet sau khi người dùng nhập ô inpu
     * Nguyễn Văn Cương 22/11/2022
     */
    UpdateAlert() {
      //trường hợp nhập vào ô input
      this.isShowAlertName = false;
      this.inValue_Name = true;
      //trường hợp xóa ô input
      if (!this.User.fullName) {
        this.inValue_Name = false;
      }
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     * Nguyễn Văn Cương 2/10/2022
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.User.fullName) {
        validate = false;
      }
      if (!this.User.username) {
        validate = false;
      }
      if (!this.User.password) {
        validate = false;
      }
      if (!this.User.email) {
        validate = false;
      }
      if (!this.User.phoneNumber) {
        validate = false;
      }
      return validate;
    },
    getDate(date) {
      this.User.dateOfBirth = date;
    },
    getGender(gender) {
      this.User.gender = gender;
    },
  },
  data() {
    return {
      drop: false,
      User: {},
      InforTable: {},
      listRoleData: {},
      listBranchData: {},
      listIsVerify: [
        {
          name: "Có",
          id: true,
        },
        {
          name: "Không",
          id: false,
        },
      ],
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      openDropitem: false,
      //lưu trạng thái alert
      isShowAlertName: false,
      //hiển thị đỏ nhập tên khi trống
      inValue_Name: {
        type: Boolean,
        default: true,
      }, //hiển thị đỏ nhập mật khẩu khi trống
      AlertNameMess: "",
      AlertUsernameMess: "",
      AlertPasswordMess: "",
      AlertEmailMess: "",
      AlertPhonedMess: "",
      TypeList: [],
      isImageUpdate: false,
      isOpenImg: false,
      urlImage: null,
      stylePass: "",
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      isSubmitUsername: false,
      isSubmitFullname: false,
      isSubmitEmail: false,
      isSubmitAddress: false,
      isSubmitPassword: false,
      isSubmitPhonenumber: false,
      isSubmitDob: false,
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
  z-index: 1001;
}
.popup {
  width: 885px;
  height: 700px;
  position: relative;
  top: 2%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popup-top {
  width: 100%;
  height: 8%;
  display: flex;
}
.button-add {
  width: 200px;
}

.popup-top-left {
  width: 50%;
  height: 100%;
  position: relative;
}
.popup-top-left.label {
  width: 50%;
  height: 100%;
  margin-left: 10%;
  display: flex;
  align-items: center;
}
.label-device {
  font-size: 25px;
  font-weight: bold;
  color: #000;
}
.popup-top-right {
  width: 50%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: flex-end;
}
.popup-top-right.btn {
  width: 30%;
  height: 100%;
  position: absolute;
  right: 0;
}
.btn-exit {
  width: 30px;
  height: 30px;
  border: none;
  background: #fff;
  font-size: 25px;
  position: absolute;
  right: 25%;
  top: 30%;
}
.popup-center {
  width: 100%;
  height: 84%;
  display: flex;
  position: relative;
}
.popup-center-left {
  width: 60%;
  height: 100%;
  margin-left: 5%;
}
.popup-center-left-input {
  width: 100%;
  height: 12%;
  display: flex;
  align-items: center;
}
.popup-center-left-input.b {
  height: 13%;
}

.popup-center-left-input.c {
  margin-bottom: 10px;
  height: 22%;
}
.label-infor {
  font-size: 17px;
  font-weight: bold;
}
.input-right {
  width: 50%;
  height: 100%;
  position: relative;
}
.input-top {
  width: 80%;
  display: flex;
  align-content: flex-start;
}
.input-label {
  font-size: 12px;
  color: #000;
  font-weight: bold;
}
.input-label.sao {
  color: red;
}
.input-bottom {
  width: 90%;
  height: 65%;
}
.input-bottom.b {
  width: 95%;
  display: flex;
}
.input-bottom.c {
  height: 40%;
}
.input-bottom-textarea {
  height: 100px;
  width: 100%;
  resize: vertical;
  display: -webkit-box;
}
.v-textarea {
  height: 120px !important;
}
.popup-input {
  height: 100%;
}
.input-left {
  width: 42%;
  height: 100%;
  position: relative;
  margin-left: 10%;
}
.popup-center-right {
  width: 30%;
  height: 100%;
  border-left: 1px solid #ababab;
  margin-left: 5%;
}
.popup-center-right-label {
  width: 100%;
  height: 20%;
  display: flex;
  align-items: center;
}
.label-img {
  font-size: 17px;
  font-weight: bolder;
  width: 100%;
}
.popup-center-right-img {
  width: 100%;
  height: 45%;
  position: relative;
}
.popup-img {
  width: 60%;
  height: 80%;
  border: 1px solid #ababab;
  border-radius: 10%;
  overflow: hidden;
  margin: auto auto;
}
.popup-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.popup-center-right-btn {
  width: 100%;
  height: 20%;
  display: flex;
}
.btn-add-img {
  left: 60px;
}
.btn-delete-img {
  width: 20px;
  height: 15px;
  font-size: 23px;
  position: absolute;
  right: 40px;
  cursor: pointer;
}
.input-add {
  width: 45%;
  height: 100%;
  display: flex;
  align-items: center;
}
.input-cancel {
  width: 30%;
  height: 100%;
  display: flex;
  align-items: center;
}
.alertInputEm {
  width: 230px;
  height: 18px;
  font-size: 12px;
  position: absolute;
  top: 60px;
  right: 28px;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 2px 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
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
.popup-settup {
  width: 40px;
  height: 40px;
  display: flex;
  font-size: 23px;
  align-items: center;
  margin-left: 6px;
}
</style>
