<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "thiết bị" }}</label>
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
      <div class="popup-cente" v-if="isLoading && isLoading === true">
        <v-skeleton-loader
          v-for="i in 3"
          :key="i"
          class="mx-auto my-3 border"
          max-width="1000"
          type="article"
        ></v-skeleton-loader>
      </div>
      <div class="popup-center" v-else>
        <div class="popup-center-left">
          <div class="popup-center-left-input b">
            <label class="label-infor">Chi tiết thiết bị</label>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tên</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="36"
                  :typeInput="'name'"
                  :placeholder="'Tên thiết bị '"
                  @updateAlert="UpdateAlert"
                  v-model="Device.name"
                  @isSubmit="checkName"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Trạng thái</label>
              </div>
              <div class="input-bottom">
                <MDropItems
                  :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                  :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                  :DropItemsModel="Device.status"
                  :RecordDrop="DeviceStatus"
                  :MDropSta="2"
                  @dropitem-value="getStatus"
                  :label="'name'"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Giá tiền</label>
                <label class="input-label sao">*</label>
                <label class="input-label mx-3">{{ FormatVND }}</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="5"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'number'"
                  :placeholder="'Nhập giá tiền '"
                  @updateAlert="UpdateAlert"
                  v-model="Device.price"
                  @isSubmit="checkPrice"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Loại thiết bị</label>
              </div>
              <div class="input-bottom b">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 73%;'"
                  :iconadd="roleValue == 'ADMIN'"
                  :inValueCombox="inValue_Unit"
                  :Comboboxmodel="Device.typeId"
                  :DropboxItem="TypeList"
                  :value="'id'"
                  :label="'name'"
                  :isShow="isShowDropbox"
                  :maxlength="100"
                  @combobox-value="getType"
                  @handlSearchCombobox="searchCombobox"
                  @open-popup-edit="openTypeClick"
                  :onSearch="isSearching"
                  ref="combobox"
                  :tab="4"
                />
                <button v-if="roleValue == 'ADMIN'" @click="openTypeClick" class="popup-settup">
                  <MTooltip :content="'Chỉnh sửa'" />
                  <i class="ti ti-category"> </i>
                </button>
              </div>
            </div>
          </div>

          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Thời hạn bảo dưỡng</label>
              </div>
              <div class="input-bottom b">
                <MInput
                  class="popup-input"
                  :style="'min-height: 36px;'"
                  :tab="3"
                  :inValue="inValue_Name"
                  :maxlength="10"
                  :typeInput="Number"
                  :placeholder="'Thời hạn'"
                  @updateAlert="UpdateAlert"
                  v-model="Device.rangeMaintain"
                  @isSubmit="checkRangeMaintain"
                />
                <button
                  class="popup-settup"
                  @click="openDeviceHistory(Device.id)"
                >
                  <MTooltip :content="'Chỉnh sửa'" />
                  <i class="ti ti-history-toggle"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="popup-center-left-input">
            <div class="input-add">
              <MButton
                :text="popupLabel + 'thiết bị'"
                @click="addDevice"
                :ButtonCss="'content-top-btn-content'"
                class="button-add"
              />
            </div>
            <div class="input-cancel">
              <MButton
                :text="'Hủy'"
                :ButtonCss="'btn-button-cancel'"
                @click="closePopup"
                class="button-add"
              />
            </div>
          </div>
        </div>
        <div class="popup-center-right">
          <div class="popup-center-right-label">
            <label class="label-img">Ảnh thiết bị</label>
          </div>
          <div class="popup-center-right-img">
            <div class="popup-img" @click="openImg(urlImage)">
              <MTooltip :content="'Click để xem'" />
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
              class="btn-add-img"
              @click="openFileInput"
              :text="popupLabel + 'ảnh'"
              :ButtonCss="'content-top-btn-content'"
            />
            <div class="btn-delete-img" @click="openPopupAsk">
              <MTooltip :content="'Click để xóa ảnh'" />
              <i class="ti ti-trash"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <MPopupHistoryVue
      v-if="isOpenHistory"
      :nameHistory="HistoryName"
      :id="HistoryId"
      @close-popup-click="closeHistoryPop"
      :PopupHistory_label="'Lịch sử bảo dưỡng'"
    />
    <MPopupType
      v-if="isOpenType"
      :urlType="'device-types/'"
      :Type="TypeList"
      @close-popup-click="closeHistoryPop"
      @load_Type="loadDeviceType"
      :Popup_label="'Loại thiết bị'"
    />
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
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import MPopupHistoryVue from "@/components/MPopupEdit/MPopupHistory.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MPopupType from "@/components/MPopupEdit/MPopupType.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import fireStorage from "@/plugins/fireStorage";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "DevicePopup",
  components: {
    MInput,
    MButton,
    MTooltip,
    MDropItems,
    MPopupHistoryVue,
    MCombobox,
    MPopupType,
    MPopupViewImgVue,
    MPopupNotification,
  },
  props: {
    deviceSelected: Object,
    Mode: Number,
    popupLabel: String,
    selectedBranch: String,
  },
  created() {
    try {
      this.loadDeviceType();
      if (this.deviceSelected) {
        this.isSubmitName = true;
        this.isSubmitPrice = true;
        this.isSubmitRangeMaintain = true;
        this.Device = { ...this.deviceSelected };
        this.Device.rangeMaintain = this.Device.rangeMaintain + " tháng";
        this.DeviceTypeId = this.Device.type.id;
        (async () => {
          this.urlImage = await fireStorage.displayImage(this.Device.imageUrl);
        })();
      } else {
        this.Device.rangeMaintain = 1 + " tháng";
        this.Device.status = true;
        this.DeviceTypeId = 1;
      }
      this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
    } catch (error) {
      console.log(error);
      this.ShowToast(3, "Lỗi hệ thống");
    }
  },
  computed: {
    FormatVND() {
      try {
        const suffixes = ["", "nghìn", "triệu", "tỷ", "nghìn tỷ", "triệu tỷ"]; // Các hậu tố đơn vị tiền tệ
        let suffixIndex = 0; // Chỉ số của hậu tố đơn vị tiền tệ
        let amount = Number(this.Device.price);
        if (isNaN(amount)) {
          return "0 đồng"; // Trả về chuỗi rỗng nếu không phải là số hợp lệ
        }
        while (amount >= 1000) {
          amount /= 1000;
          suffixIndex++;
        }
        return `${amount.toFixed(0)} ${suffixes[suffixIndex]} đồng`;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi Format VNĐ");
        return "0 đồng";
      }
    },
  },
  methods: {
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
    closeHistoryPop() {
      this.isOpenHistory = false;
      this.isOpenType = false;
    },
    closePopupImg() {
      this.isOpenImg = false;
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
    },

    getStatus(selectedit) {
      console.log(selectedit);
      this.Device.status = selectedit;
    },

    getType(selectedit) {
      console.log(selectedit);
      this.DeviceTypeId = selectedit;
    },

    openDeviceHistory(id) {
      this.HistoryId = id;
      this.isOpenHistory = true;
    },

    openTypeClick() {
      this.isOpenType = true;
    },

    /**
     * Hàm xóa ảnh
     */
    deleteImg() {
      (async () => {
        await fireStorage.deleteImage(this.Device.imageUrl);
      })();
      this.Device.imageUrl = null;
      this.addDevice();
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
      if (!this.Device.name) {
        this.inValue_Name = false;
      }
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.Device.name) {
        validate = false;
      }
      return validate;
    },

    searchCombobox(value) {
      this.WhereValue = value;
      this.loadDeviceType();
    },

    async loadDeviceType() {
      this.isLoading = true;
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}`;
        fetch(`${apiConfig}/device-types` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.TypeList = data.content;
            this.drop = true;
            if (this.WhereValue) {
              this.isSearching = true;
            }
          })
          .catch((res) => {
            console.log(res);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.isLoading = false;
      }
    },

    async addDevice() {
      if (this.checkSubmitAll()) {
        var method = "POST";
        var url = "devices/";
        let imageUrl =
          this.Mode === 2
            ? this.Device.imageUrl
            : await fireStorage.getURL(
                "device",
                Date.now(),
                this.$refs.fileImage.files[0]
              );
        var rangemaintain = this.Device.rangeMaintain.replace(/[^0-9]*/g, "");
        if (this.Mode == 2) {
          method = "PUT";
          url = url + this.Device.id;
          imageUrl =
            this.isImageUpdate == true
              ? await fireStorage.getURL(
                  "device",
                  Date.now(),
                  this.$refs.fileImage.files[0]
                )
              : this.Device.imageUrl;
        }
        if (this.validateEmpty() == true) {
          try {
            await fetch(
              `${apiConfig}/` + url,
              await fetchOptions(method, {
                name: this.Device.name,
                rangeMaintain: rangemaintain,
                price: parseFloat(this.Device.price),
                imageUrl: imageUrl,
                status: this.Device.status,
                typeId: this.DeviceTypeId,
                branchId: this.selectedBranch,
              })
            )
              .then((res) => res.json())
              .then(async (res) => {
                if (!res.id) {
                  this.ShowToast(3, res.message);
                  this.showAlert(res.status);
                } else {
                  this.$emit("close-popup-click");
                  this.$emit("load-data");
                  if (this.Mode == 2) {
                    this.ShowToast((this.ToastStatus = 2));
                    if (this.isImageUpdate) {
                      await fireStorage.deleteImage(this.Device.imageUrl);
                    }
                  } else {
                    this.ShowToast((this.ToastStatus = 1));
                  }
                }
              })
              .catch((res) => {
                console.log(res);
                this.ShowToast(3, "Lỗi máy chủ");
                this.showAlert(500);
              });
          } catch (error) {
            this.ShowToast(3, "Lỗi hệ thống");
            this.showAlert(500);
          }
        }
      }
    },
    checkName(submit) {
      this.isSubmitName = submit;
    },
    checkPrice(submit) {
      this.isSubmitPrice = submit;
    },
    checkRangeMaintain(submit) {
      this.isSubmitRangeMaintain = submit;
    },
    checkSubmitAll() {
      if (
        this.isSubmitName &&
        this.isSubmitPrice &&
        this.isSubmitRangeMaintain
      ) {
        return true;
      } else return false;
    },

    beforeUnmount() {
      // Giải phóng URL tạm thời khi component bị xóa
      if (this.urlImage) {
        URL.revokeObjectURL(this.urlImage);
      }
    },
  },
  data() {
    return {
      isLoading: false,
      drop: false,
      Device: {},
      DeviceTypeId: "",
      DeviceStatus: [
        { name: "Đang sử dụng", id: true },
        { name: "Ngưng sử dụng", id: false },
      ],
      DeviceType: [
        { name: "Máy chạy bộ", id: 1 },
        { name: "Tạ", id: 2 },
      ],
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      HistoryId: "",
      HistoryName: "",
      openDropitem: false,
      //lưu trạng thái alert
      isShowAlertName: false,
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
      AlertNameMess: "",
      isOpenHistory: false,
      isOpenType: false,
      TypeList: [],
      urlImage: null,
      isImageUpdate: false,
      WhereValue: null,
      isSearching: false,
      isOpenImg: false,
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      isSubmitName: false,
      isSubmitPrice: false,
      isSubmitRangeMaintain: true,
      roleValue: null,
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
  width: 885px;
  height: 460px;
  position: relative;
  top: 20%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popup-top {
  width: 100%;
  height: 13%;
  display: flex;
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
  align-items: flex-end;
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
  height: 85%;
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
  height: 22%;
  display: flex;
  align-items: center;
}
.popup-center-left-input.b {
  height: 16%;
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
.button-add {
  width: 150px;
}
.input-label.sao {
  color: red;
}
.input-bottom {
  width: 90%;
  height: 63%;
}
.input-bottom.b {
  width: 95%;
  display: flex;
  height: 56%;
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
  height: 25%;
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
  height: 55%;
  position: relative;
  cursor: pointer;
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
  width: 10px;
  height: 15px;
  font-size: 23px;
  position: absolute;
  right: 50px;
  cursor: pointer;
}
.input-add {
  width: 27%;
  height: 100%;
  display: flex;
  align-items: center;
}
.input-cancel {
  width: 30%;
  height: 100%;
  display: flex;
  align-items: center;
  margin-left: 30px;
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
  position: relative;
}
</style>
