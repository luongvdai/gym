<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{
              popupLabel + " thông số cơ thể"
            }}</label>
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
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Chiều cao (m)</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập chiều cao'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.height"
                />

                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Cân nặng (kg)</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="2"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập cân nặng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.weight"
                />
                <div class="alertInputEm b" v-if="isShowAlertAddress">
                  {{ AlertAddressMess }}
                </div>
              </div>
            </div>
          </div>

          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Số đo vòng 1</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="7"
                  :style="'min-height: 36px;'"
                  :maxlength="50"
                  :placeholder="'Nhập số đo'"
                  :typeInput="'number'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.bustMeasurement"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Số đo vòng 2</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="8"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập số đo'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.waistMeasurement"
                />

                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Số đo vòng 3</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="9"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="50"
                  :placeholder="'Nhập số đo'"
                  :typeInput="'number'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.hipMeasurement"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
          </div>

          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tỉ lệ nước trong cơ thể(ecw)</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="3"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập tỉ lệ'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.ecw_tbw"
                />
              </div>
            </div>
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Chuẩn dưới ecw</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="3"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập tỉ lệ'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.ecw_tbwNormal"
                />
              </div>
            </div>
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Chuẩn trên ecw</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="3"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập tỉ lệ'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.ecw_tbwNormalTo"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Hàm lượng Protein</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="4"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập hàm lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.protein"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Hàm lượng Protein chuẩn dưới</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="4"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập hàm lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.proteinNormal"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Hàm lượng Protein chuẩn trên</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="4"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập hàm lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.proteinNormalTo"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tỉ lệ mỡ</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="5"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="50"
                  :typeInput="'number'"
                  :placeholder="'Nhập số lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.pbf"
                />
              </div>
            </div>
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tỉ lệ mỡ chuẩn dưới</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="5"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="50"
                  :typeInput="'number'"
                  :placeholder="'Nhập số lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.pbfNormal"
                />
              </div>
            </div>
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tỉ lệ mỡ chuẩn trên</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="5"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="50"
                  :typeInput="'number'"
                  :placeholder="'Nhập số lượng'"
                  @updateAlert="UpdateAlert"
                  v-model="InfoGym.pbfNormalTo"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input textarea">
            <div class="input-right textarea">
              <div class="input-top">
                <label class="input-label">Ghi chú</label>
              </div>
              <div class="input-bottom nd">
                <textarea
                  placeholder="Thêm ghi chú chi tiết"
                  v-model="InfoGym.note"
                  rows="5"
                ></textarea>
              </div>
            </div>
          </div>
          <div class="popup-center-left-input my-5">
            <div class="input-add">
              <MButton
                :text="popupLabel + ' thông số'"
                @click="checkAddOrUpdate"
                :ButtonCss="'content-top-btn-content'"
                class="button-add"
              />
            </div>
            <div class="input-cancel">
              <MButton
                :text="'Hủy'"
                :ButtonCss="'btn-button-cancel'"
                @click="closePopup"
              />
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
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: {
    MInput,
    MButton,
    MPopupViewImgVue,
    MPopupNotification,
  },
  props: {
    recordSelected: Object,
    Mode: Number,
    UserCourseId: String,
    UserId: String,
  },
  created() {
   try {
      this.userId = this.UserId;
    this.userCourseId = this.UserCourseId;
    
    if (this.recordSelected.userId) {
      this.popupLabel = "Sửa";
      (async () => {
        this.InfoGym = this.recordSelected;
      })();
    } else this.popupLabel = "Thêm";
   } catch (error) {
     console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
   }
  },
  methods: {
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

    searchCombobox(value) {
      console.log(value);
      this.WhereValue = value;
      this.ProvinceQuery();
    },

    /**
     * Hàm xóa ảnh

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
      this.isShowAlertAddress = true;
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 404) {
        this.AlertNameMess = "Không tìm thấy";
      } else if (code == 304) {
        this.AlertNameMess = "Chưa có ảnh";
        this.isShowAlertAddress = false;
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
      this.isShowAlertAddress = false;
      this.inValue_Name = true;
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     * Nguyễn Văn Cương 2/10/2022
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (
        !this.InfoGym.height ||
        !this.InfoGym.weight ||
        !this.InfoGym.ecw_tbw ||
        !this.InfoGym.protein ||
        !this.InfoGym.pbf ||
        !this.InfoGym.bustMeasurement ||
        !this.InfoGym.waistMeasurement ||
        !this.InfoGym.hipMeasurement
      ) {
        validate = false;
      }
      return validate;
    },
    checkAddOrUpdate() {
      console.log("check" + this.InfoGym);
      if (!this.recordSelected.userId) {
        this.addGymInfor();
      } else this.updateGymInfor();
    },

    async addGymInfor() {
      try {
        var method = "POST";
        var url = "/gym-information";
        if (this.validateEmpty() == true) {
          try {
            fetch(
              `${apiConfig}` + url,
              await fetchOptions(method, {
                userId: this.userId,
                height: this.InfoGym.height,
                weight: this.InfoGym.weight,
                ecw_tbw: this.InfoGym.ecw_tbw,
                ecw_tbwNormal: this.InfoGym.ecw_tbwNormal,
                ecw_tbwNormalTo: this.InfoGym.ecw_tbwNormalTo,
                protein: this.InfoGym.protein,
                proteinNormal: this.InfoGym.proteinNormal,
                proteinNormalTo: this.InfoGym.proteinNormalTo,
                pbf: this.InfoGym.pbf,
                pbfNormal: this.InfoGym.pbfNormal,
                pbfNormalTo: this.InfoGym.pbfNormalTo,
                bustMeasurement: this.InfoGym.bustMeasurement,
                waistMeasurement: this.InfoGym.waistMeasurement,
                hipMeasurement: this.InfoGym.hipMeasurement,
                note: this.InfoGym.note,
                userCourseId: this.userCourseId,
              })
            )
              .then((res) => res.json())
              .then((res) => {
                if (!res.id) {
                  this.ShowToast(3, res.message);
                } else {
                  this.$emit("close-popup-click");
                  this.$emit("load-data");
                  this.ShowToast((this.ToastStatus = 1));
                }
              })
              .catch((res) => {
                console.log(res.message);
            this.ShowToast(3, "Thêm thất bại");
              });
          } catch (error) {
            console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
          }
        }
      } catch (error) {
        console.log(error);console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async updateGymInfor() {
      try {
        var method = "PUT";
        var url = `/gym-information/${this.InfoGym.id}`;
        if (this.validateEmpty() == true) {
          try {
            fetch(
              `${apiConfig}` + url,
              await fetchOptions(method, {
                userId: this.recordSelected.userId,
                height: this.InfoGym.height,
                weight: this.InfoGym.weight,
                ecw_tbw: this.InfoGym.ecw_tbw,
                ecw_tbwNormal: this.InfoGym.ecw_tbwNormal,
                ecw_tbwNormalTo: this.InfoGym.ecw_tbwNormalTo,
                protein: this.InfoGym.protein,
                proteinNormal: this.InfoGym.proteinNormal,
                proteinNormalTo: this.InfoGym.proteinNormalTo,
                pbf: this.InfoGym.pbf,
                pbfNormal: this.InfoGym.pbfNormal,
                pbfNormalTo: this.InfoGym.pbfNormalTo,
                bustMeasurement: this.InfoGym.bustMeasurement,
                waistMeasurement: this.InfoGym.waistMeasurement,
                hipMeasurement: this.InfoGym.hipMeasurement,
                note: this.InfoGym.note,
                userCourseId: this.recordSelected.userCourseId,
              })
            )
              .then((res) => res.json())
              .then((res) => {
                if (!res.id) {
                  this.ShowToast(3, res.message);
                } else {
                  this.$emit("close-popup-click");
                  this.$emit("load-data");

                  this.ShowToast((this.ToastStatus = 2));
                }
              })
              .catch((res) => {
                console.log(res.message);
            this.ShowToast(3, "Cập nhật thất bại");
              });
          } catch (error) {
            console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
          }
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
  },
  data() {
    return {
      InfoGym: {},
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
      //hiển thị đỏ nhập tên khi trống
      inValue_Name: {
        type: Boolean,
        default: true,
      },
      AlertNameMess: "",
      AlertCodeMess: "",
      isShowAlertAddress: "",
      AlertAddressMess: "",
      urlImage: null,
      urlImageBackground: null,
      isOpenImg: false,
      drop: false,
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      popupLabel: "",
      userId:"",
      userCourseId:"",
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
.popup {
  max-width: 885px;
  min-height: 650px;
  position: relative;
  top: 20px;
  margin: auto auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
}
.popup-top {
  display: flex;
  height: 12%;
  align-items: center;
}
.popup-top-left {
  width: 50%;
  height: 100%;
  position: relative;
  white-space: nowrap;
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
}
.popup-top-right.btn {
  width: 15%;
  height: 100%;
  position: absolute;
  right: 0;
  top: 0;
}
.btn-exit {
  width: 30px;
  height: 30px;
  border: none;
  background: #fff;
  font-size: 25px;
  position: absolute;
  right: 0%;
  top: 0%;
}
.popup-center {
  width: 100%;
  height: 88%;
  display: flex;
  position: relative;
}
.popup-center-left {
  width: 100%;
  height: 100%;
  margin-left: 5%;
}
.popup-center-left-input {
  width: 100%;
  height: 70px;
  display: flex;
  align-items: center;
}
.popup-center-left-input.textarea {
  height: 22%;
}

.popup-center-left-input .nd textarea {
  border: 1px solid #ababab;
  width: 100%;
  min-height: 40px;
  border-radius: 5px;
}
.label-infor {
  font-size: 17px;
  font-weight: bold;
}
.input-right {
  width: 33%;
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
  height: 40%;
}
.input-bottom.b {
  width: 95%;
  display: flex;
  text-align: justify;
}
.input-bottom textarea:focus {
  border: 1px solid #50b83c;
  outline: none;
}
.input-bottom textarea {
  resize: none;
  padding: 5px;
}
.popup-input {
  height: 100%;
}
.button-add {
  width: 150px;
}
.input-left {
  width: 33%;
  height: 100%;
  position: relative;
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
  width: 10px;
  height: 15px;
  font-size: 23px;
  position: absolute;
  right: 50px;
  cursor: pointer;
}
.input-add {
  width: 30%;
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
.input-bottom.nd {
  width: 100%;
}
.input-right.textarea {
  width: 63%;
}
@media (min-width: 1535px) and (max-width: 1600px) {
  .popup-center-left-input{
    height: 70px;
  }
}
</style>
