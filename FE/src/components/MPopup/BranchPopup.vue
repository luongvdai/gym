<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "chi nhánh" }}</label>
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
            <label class="label-infor">Chi tiết chi nhánh</label>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tên chi nhánh</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'name'"
                  :placeholder="'Tên chi nhánh'"
                  @updateAlert="UpdateAlert"
                  v-model="Brach.name"
                  @isSubmit="checkName"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Tỉnh thành</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 35px;'"
                  :DropboxItem="province"
                  @combobox-value="getProvince"
                  :Comboboxmodel="fullAddress.province"
                  :isBranchAddress="true"
                  :value="'name'"
                  :label="'name'"
                  :maxlength="15"
                  ref="combobox"
                  :tab="7"
                  @handlSearchCombobox="searchCombobox"
                  :onSearch="isSearching"
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
                <label class="input-label">Quận/huyện</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 35px;'"
                  @combobox-value="getDistricts"
                  :DropboxItem="districts"
                  :Comboboxmodel="fullAddress.district"
                  :isBranchAddress="true"
                  :value="'name'"
                  :label="'name'"
                  ref="combobox"
                  :tab="7"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Phường</label
                ><label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 35px;'"
                  @combobox-value="getWards"
                  :DropboxItem="wards"
                  :Comboboxmodel="fullAddress.ward"
                  :isBranchAddress="true"
                  :value="'name'"
                  :label="'name'"
                  :maxlength="15"
                  ref="combobox"
                  :tab="7"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Địa chỉ</label>
              </div>
              <div class="input-bottom">
                <MInput
                  v-if="drop"
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :maxlength="50"
                  :typeInput="'address'"
                  :placeholder="'Địa chỉ cụ thể'"
                  v-model="addressChild"
                  @isSubmit="checkAddress"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Mô tả</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="'name'"
                  :placeholder="'Nhập mô tả'"
                  @updateAlert="UpdateAlert"
                  v-model="Brach.description"
                  @isSubmit="checkDes"
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
                <label class="input-label">Logo</label>
              </div>
              <div class="input-bottom">
                <input
                  @change="handleFileSelect"
                  type="file"
                  ref="logoImage"
                  class="d-none"
                  name="logoImage"
                  accept="image/jpeg, image/png"
                />
                <MInput
                  class="popup-input"
                  :tab="3"
                  :maxlength="100"
                  :typeInput="'url'"
                  :placeholder="'Đường dẫn logo'"
                  @updateAlert="UpdateAlert"
                  v-model="Brach.logoUrl"
                  :readOnly="true"
                  :style="'min-height: 36px; cursor: pointer;'"
                  @click="openFileInput(true)"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Banner</label>
              </div>
              <div class="input-bottom">
                <input
                  @change="handleFileSelect"
                  type="file"
                  ref="bannerImage"
                  name="bannerImage"
                  class="d-none"
                  accept="image/jpeg, image/png"
                />
                <MInput
                  class="popup-input"
                  :tab="3"
                  :maxlength="100"
                  :typeInput="'url'"
                  :placeholder="'Đường dẫn banner'"
                  @updateAlert="UpdateAlert"
                  v-model="Brach.backgroundUrl"
                  :readOnly="true"
                  :style="'min-height: 36px; cursor: pointer;'"
                  @click="openFileInput(false)"
                />
              </div>
            </div>
          </div>

          <div class="popup-center-left-input my-5">
            <div class="input-add">
              <MButton
                :text="popupLabel + 'chi nhánh'"
                @click="addBrach"
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
            <label class="label-img">{{ ImageName }} chi nhánh</label>
          </div>
          <div class="popup-center-right-img">
            <div class="popup-img" @click="openImg(urlImage)">
              <MTooltip :content="'Click xem ảnh'" />
              <img :src="urlImage" v-if="urlImage" />
              <img src="@\assets\photos\2.jpg" v-if="!urlImage" />
            </div>
          </div>
          <div class="popup-center-right-btn my-5">
            <MButton
              class="btn-add-img"
              @click="openImg(urlImage)"
              :text="'Xem ảnh'"
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
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
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
  name: "BrachPopup",
  components: {
    MInput,
    MButton,
    MTooltip,
    MPopupViewImgVue,
    MCombobox,
    MPopupNotification,
  },
  props: {
    branchSelected: Object,
    Mode: Number,
    popupLabel: String,
  },
  created() {
    try {
      this.ProvinceQuery();
      if (this.branchSelected) {
        this.isSubmitBranchName = true;
        this.isSubmitAddress = true;
        this.isSubmitDes = true;
        (async () => {
          this.Brach = { ...this.branchSelected };
          this.urlImage = await fireStorage.displayImage(this.Brach.logoUrl);
          this.urlImageBackground = await fireStorage.displayImage(
            this.Brach.backgroundUrl
          );
          await this.convertStringToObject(this.Brach.address);
          this.districts = this.convertAddress(
            this.province,
            this.fullAddress.province,
            "districts"
          );
          this.wards = this.convertAddress(
            this.districts,
            this.fullAddress.district,
            "wards"
          );
          this.addressChild = this.fullAddress.address;
          this.drop = true;
        })();
      } else {
        this.drop = true;
      }
    } catch (error) {
      console.log(error);
      this.ShowToast(3, "Lỗi hệ thống");
    }
  },
  methods: {
    getWards(data) {
      this.fullAddress.ward = data;
    },
    getDistricts(data) {
      try {
        const foundDistricts = this.districts.find(
          (value) => value.name === data
        );
        this.wards = foundDistricts.wards;
        this.fullAddress.district = data;
        this.fullAddress.ward = "";
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    getProvince(data) {
      try {
        const foundProvince = this.province.find(
          (value) => value.name === data
        );
        this.districts = foundProvince.districts;
        this.wards = [];
        this.fullAddress = {
          province: data,
          district: "",
          ward: "",
          address: "",
        };
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async convertStringToObject(addressString) {
      try {
        const [address, locationString] = addressString.split(" | ");
        const [ward, district, province] = locationString.split(", ");

        this.fullAddress = {
          province: province ? province.trim() : "",
          district: district ? district.trim() : "",
          ward: ward ? ward.trim() : "",
          address: address ? address.trim() : "",
        };
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    convertAddress(searchArray, endArray, label) {
      try {
        let result = null;
        searchArray.forEach((i) => {
          if (i.name == endArray) {
            result = i[label];
          }
        });
        return result;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    ProvinceQuery() {
      try {
        fetch("https://provinces.open-api.vn/api/?depth=3", fetchOptions("GET"))
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.province = response;
          })
          .catch((error) => {
            console.log(error.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    handleFileSelect(event) {
      try {
        // lấy ra file
        const file = event.target.files[0];
        //check file là hình ảnh
        if (file && this.isImageFile(file)) {
          //urlImage là hình ảnh được chọn
          this.urlImage = URL.createObjectURL(file);
          //check xem có phải là update không?
          if (event.srcElement.name === "logoImage") {
            this.isLogoUpdate = this.Mode === 2 ? true : false;
            this.ImageName = "Logo";
          } else {
            this.isBannerUpdate = this.Mode === 2 ? true : false;
            this.ImageName = "Banner";
          }
        } else {
          alert("Chọn file hình ảnh");
        }
      } catch (error) {
        console.log(error);
      }
    },
    openFileInput(isLogo) {
      if (isLogo) {
        this.$refs.logoImage.click();
      } else {
        this.$refs.bannerImage.click();
      }
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

    searchCombobox(value) {
      console.log(value);
      this.WhereValue = value;
      this.ProvinceQuery();
    },

    /**
     * Hàm xóa ảnh
     */
    deleteImg() {
      try {
        this.Brach.backgroundUrl = null;
        this.Brach.logoUrl = null;
        this.isShowAskDelete = false; //hiện popup hỏi người dùng
        this.urlImage = null;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
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
      if (!this.Brach.name || !this.fullAddress) {
        validate = false;
      }
      return validate;
    },
    checkSubmitAll() {
      if (this.isSubmitAddress && this.isSubmitBranchName && this.isSubmitDes) {
        return true;
      } else false;
    },
    async addBrach() {
      if (this.checkSubmitAll()) {
        try {
          this.fullAddress.address = this.addressChild;
          var method = "POST";
          var url = "branches/";
          let logoUrl =
            this.Mode === 2
              ? this.Brach.logoUrl
              : await fireStorage.getURL(
                  "branches",
                  Date.now(),
                  this.$refs.logoImage.files[0]
                );
          let backgroundUrl =
            this.Mode === 2
              ? this.Brach.backgroundUrl
              : await fireStorage.getURL(
                  "branches",
                  Date.now(),
                  this.$refs.bannerImage.files[0]
                );
          if (this.Mode == 2) {
            method = "PUT";
            url = url + this.Brach.id;
            logoUrl =
              this.isLogoUpdate == true
                ? await fireStorage.getURL(
                    "branches",
                    Date.now(),
                    this.$refs.logoImage.files[0]
                  )
                : this.Brach.logoUrl;
            backgroundUrl =
              this.isBannerUpdate == true
                ? await fireStorage.getURL(
                    "branches",
                    Date.now(),
                    this.$refs.bannerImage.files[0]
                  )
                : this.Brach.backgroundUrl;
          }
          if (this.validateEmpty() == true) {
            try {
              await fetch(
                `${apiConfig}/` + url,
                await fetchOptions(method, {
                  name: this.Brach.name,
                  address: `${
                    this.fullAddress.address === undefined
                      ? ""
                      : this.fullAddress.address
                  } | ${this.fullAddress.ward}, ${this.fullAddress.district}, ${
                    this.fullAddress.province
                  }`,
                  description: this.Brach.description,
                  logoUrl: logoUrl,
                  backgroundUrl: backgroundUrl,
                })
              )
                .then((res) => res.json())
                .then((res) => {
                  if (!res.id) {
                    this.ShowToast(3, res.message);
                    this.showAlert(res.status);
                  } else {
                    this.$emit("close-popup-click");
                    this.$emit("load-data");
                    if (this.Mode == 2) {
                      this.ShowToast((this.ToastStatus = 2));
                    } else {
                      this.ShowToast((this.ToastStatus = 1));
                    }
                    (async () => {
                      if (this.isBannerUpdate) {
                        await fireStorage.deleteImage(this.Brach.backgroundUrl);
                      }
                      if (this.isLogoUpdate) {
                        await fireStorage.deleteImage(this.Brach.logoUrl);
                      }
                    })();
                    this.$router.go("");
                  }
                })
                .catch((res) => {
                  console.log(res.message);
                  this.ShowToast(3, "Lỗi máy chủ");
                  this.showAlert(500);
                });
            } catch (error) {
              console.log(error);
              this.ShowToast(3, "Lỗi hệ thống");
              this.showAlert(500);
            }
          }
        } catch (error) {
          console.log(error);
          this.ShowToast(3, "Lỗi hệ thống");
        }
      }
    },
    checkName(submit) {
      this.isSubmitBranchName = submit;
    },
    checkDes(submit) {
      this.isSubmitDes = submit;
    },
    checkAddress(submit) {
      this.isSubmitAddress = submit;
    },
  },
  data() {
    return {
      Brach: {},
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
      fullAddress: {
        province: "",
        district: "",
        ward: "",
        address: "",
      },
      province: [],
      districts: [],
      wards: [],
      drop: false,
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      isSubmitBranchName: false,
      isSubmitDes: false,
      isSubmitAddress: false,
      ImageName: "Banner",
      addressChild: null,
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
  min-height: 480px;
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
  padding-top: 13px;
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
.button-add {
  width: 150px;
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
  height: 230px;
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
  height: 25%;
  display: flex;
  align-items: center;
  margin: 10px 0;
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
.input-bottom {
  height: 94%;
}
.input-label {
  font-size: 12px;
  color: #000;
  font-weight: bold;
}
.input-label.sao {
  color: red;
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
  height: 400px;
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
  height: 26px;
  font-size: 23px;
  position: absolute;
  right: 40px;
  cursor: pointer;
}
.input-add {
  width: 28%;
  height: 100%;
  display: flex;
  align-items: center;
}
.input-cancel {
  width: 30%;
  height: 100%;
  display: flex;
  align-items: center;
  margin-left: 20px;
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
.alertInputEm.b {
  width: 200px;
  right: 19px;
}
</style>
