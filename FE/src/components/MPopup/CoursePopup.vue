<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "khóa tập" }}</label>
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
            <label class="label-infor">Chi tiết khóa tập</label>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tiêu đề</label>
                <label class="input-label sao">*</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="50"
                  :typeInput="'title'"
                  :placeholder="'Tiêu đề khóa tập '"
                  @updateAlert="UpdateAlert"
                  v-model="Course.title"
                  @isSubmit="checkTitle"
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
                  :DropItemsModel="Course.isActive"
                  :RecordDrop="courseStatus"
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
                <label class="input-label">Thời lượng ( tháng )</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :style="'min-height: 36px;'"
                  :tab="3"
                  :inValue="inValue_Name"
                  :maxlength="10"
                  :typeInput="Number"
                  :placeholder="'Thời hạn'"
                  @updateAlert="UpdateAlert"
                  v-model="Course.rangeProcess"
                  @isSubmit="checkTime"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Loại khóa tập</label>
              </div>
              <div class="input-bottom b">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 35px;'"
                  :iconadd="roleValue == 'ADMIN'"
                  :inValueCombox="Course.courseTypeId"
                  :Comboboxmodel="Course.courseTypeId"
                  :DropboxItem="TypeList"
                  :value="'id'"
                  :label="'name'"
                  :isShow="isShowDropbox"
                  :maxlength="20"
                  @combobox-value="getType"
                  @handlSearchCombobox="searchCombobox"
                  @open-popup-edit="openTypeClick"
                  :onSearch="isSearching"
                  ref="combobox"
                  :tab="4"
                />
                <button
                  v-if="roleValue == 'ADMIN'"
                  @click="openTypeClick()"
                  class="popup-settup"
                >
                  <i class="ti ti-category"></i>
                </button>
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
                  :maxlength="15"
                  :typeInput="'number'"
                  :placeholder="'Nhập giá tiền '"
                  @updateAlert="UpdateAlert"
                  v-model="Course.price"
                  @isSubmit="checkPrice"
                />
                <div class="alertInputEm" v-if="false">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Thêm PT</label>
              </div>
              <div class="input-bottom">
                <MCombobox
                  v-if="dropPT"
                  :Drstyle="'height: 35px;'"
                  :inValueCombox="inValue_Unit"
                  :ComboboxMutiModel="ListPT"
                  :DropboxItem="UserTable"
                  :value="'id'"
                  :label="'username'"
                  :code="'gender'"
                  :CombolabelLeft="'Tên'"
                  :CombolabelRight="'Giới tính'"
                  :isShow="isShowDropbox"
                  :maxlength="15"
                  @comboboxMuti-value="getlistPT"
                  @deletMuti-value="prepareDeletePTCouse"
                  @handlSearchCombobox="searchCombobox"
                  @open-popup-edit="openTypeClick"
                  :onSearch="isSearching"
                  :ComboClass="2"
                  ref="combobox"
                  :tab="7"
                />
              </div>
              <!-- <div class="input-top">
                <label class="input-label">Giảm giá</label>
              </div>
              <div class="input-bottom b">
                <MDropItems
                  :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                  :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                  :DropItemsModel="Course.discountId || 0"
                  :RecordDrop="Discount"
                  :MDropSta="2"
                  @dropitem-value="Discount"
                  :label="'name'"
                  :tab="6"
                />
                <button
                  class="popup-settup"
                  @click="openDeviceHistory(Device.id)"
                >
                  <i class="ti ti-rocket"></i>
                </button>
              </div> -->
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right"></div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-center">
              <div class="input-top">
                <label class="input-label">Mô tả</label>
              </div>
              <div class="input-bottom nd">
                <textarea
                  placeholder="Thêm mô tả chi tiết"
                  v-model="Course.description"
                  rows="5"
                ></textarea>
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-add me-2">
              <MButton
                :text="popupLabel + 'khóa tập'"
                @click="addDevice"
                class="button-add"
                :ButtonCss="'content-top-btn-content'"
              />
            </div>
            <div class="input-cancel">
              <MButton
                :ButtonCss="'btn-button-cancel'"
                :text="'Hủy'"
                @click="closePopup"
                class="button-add"
              />
            </div>
          </div>
        </div>
        <div class="popup-center-right">
          <div class="popup-center-right-label">
            <label class="label-img">Ảnh khóa tập</label>
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
      :urlType="'course-types/'"
      :Type="TypeList"
      @close-popup-click="closeHistoryPop"
      @load_Type="loadCoursesType"
      :Popup_label="'Loại khóa học'"
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
import MPopupHistoryVue from "../MPopupEdit/MPopupHistory.vue";
import MPopupType from "@/components/MPopupEdit/MPopupType.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import fireStorage from "@/plugins/fireStorage";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "CoursePopup",
  components: {
    MInput,
    MButton,
    MDropItems,
    MPopupHistoryVue,
    MCombobox,
    MPopupType,
    MPopupViewImgVue,
    MTooltip,
    MPopupNotification,
  },
  props: {
    courseSelected: Object,
    Mode: Number,
    popupLabel: String,
    selectedBranch: String,
  },
  created() {
    try {
      this.loadCoursesType();
      this.loadUser();
      if (this.courseSelected) {
        this.isSubmitPrice = true;
        this.isSubmitTime = true;
        this.isSubmitTitle = true;
        this.Course = { ...this.courseSelected };
        this.Course.rangeProcess = this.Course.rangeProcess + " tháng";
        (async () => {
          this.urlImage = await fireStorage.displayImage(this.Course.imageUrl);
        })();
      } else {
        this.Course.rangeProcess = 1 + " tháng";
        this.Course.isActive = true;
        this.dropPT = true;
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
        let amount = Number(this.Course.price);
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
        this.ShowToast(3, "Lỗi hệ thống");
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
      this.Course.courseTypeId = selectedit;
    },
    getUser(selectedit) {
      this.Course.userId = selectedit;
    },
    searchCombobox(value) {
      console.log(value);
      this.WhereValue = value;
      this.loadUser();
    },

    openTypeClick() {
      this.isOpenType = true;
    },

    /**
     * Hàm xóa ảnh
     */
    deleteImg() {
      this.Course.imageUrl = null;
      this.isShowAskDelete = false; //hiện popup hỏi người dùng
      this.urlImage = null;
      // this.addDevice();
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
          message: message,
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
      this.inValue_Name = true;
    },

    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     * Nguyễn Văn Cương 2/10/2022
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.Course.title) {
        validate = false;
      }
      return validate;
    },

    async loadCoursesType() {
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}`;
        fetch(`${apiConfig}/course-types/` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.TypeList = data.content;
            this.drop = true;
            if (this.WhereValue) {
              this.isSearching = true;
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
    },

    async loadUser() {
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.selectedBranch}&roleId=4&keyword=${where}`;
        fetch(`${apiConfig}/manage/users` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.UserTable = data.content; //lưu dữ liệu
            if (this.courseSelected) {
              this.getPTs(this.Course.id);
            }
            if (this.WhereValue) {
              this.isSearching = true;
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
    },

    async getPTs(id) {
      try {
        fetch(
          `${apiConfig}/pt-courses/mentor?courseId=${id}&isActive=true`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.ListPT = data.content;
            this.dropPT = true;
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

    getlistPT(listPT) {
      this.listPTAdd = listPT;
      if (this.Course.id) {
        this.addListPT(this.Course.id);
      }
    },

    prepareDeletePTCouse(id) {
      if (this.Course.id) {
        this.deletePTCouse(id);
      }
    },

    async deletePTCouse(id) {
      try {
        fetch(
          `${apiConfig}/pt-courses`,
          await fetchOptions("DELETE", {
            ptId: id,
            courseId: this.Course.id,
          })
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            if (data.message) {
              this.ShowToast(3, "Lỗi hệ thống");
            } else {
              this.ShowToast(1, "Xóa thành công");
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
    },

    async addListPT(id) {
      try {
        if (this.listPTAdd) {
          this.listPTAdd.forEach((i) => {
            (async () => {
              fetch(
                `${apiConfig}/pt-courses`,
                await fetchOptions("POST", {
                  ptId: i,
                  courseId: id,
                })
              )
                .then((res) => res.json())
                .then(async (res) => {
                  if (!res.id) {
                    this.ShowToast(3, "Không thể thêm PT");
                  } else {
                    this.ShowToast(1, "Thêm PT thành công");
                  }
                })
                .catch((res) => {
                  console.log(res.message);
                  this.ShowToast(3, "Lỗi máy chủ");
                });
            })();
          });
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async addDevice() {
      if (this.checkSubmitAll()) {
        var method = "POST";
        var url = "courses/";
        let imageUrl =
          this.Mode === 2
            ? this.Course.imageUrl
            : await fireStorage.getURL(
                "courses",
                Date.now(),
                this.$refs.fileImage.files[0]
              );
        let rangeProcess = this.Course.rangeProcess.replace(/[^0-9]*/g, "");
        if (this.Mode == 2) {
          method = "PUT";
          url = url + this.Course.id;
          imageUrl =
            this.isImageUpdate == true
              ? await fireStorage.getURL(
                  "courses",
                  Date.now(),
                  this.$refs.fileImage.files[0]
                )
              : this.Course.imageUrl;
        }
        if (this.validateEmpty() == true) {
          try {
            await fetch(
              `${apiConfig}/` + url,
              await fetchOptions(method, {
                title: this.Course.title,
                rangeProcess: rangeProcess,
                imageUrl: imageUrl,
                description: this.Course.description,
                isActive: this.Course.isActive,
                price: this.Course.price,
                courseTypeId: this.Course.courseTypeId,
                branchId: this.selectedBranch,
              })
            )
              .then((res) => res.json())
              .then(async (res) => {
                if (!res.id) {
                  this.ShowToast(3, "Không thể thêm khóa học");
                  this.showAlert(res.status);
                } else {
                  this.$emit("close-popup-click");
                  if (this.Mode == 2) {
                    this.ShowToast((this.ToastStatus = 2));
                    if (this.isImageUpdate) {
                      await fireStorage.deleteImage(this.Device.imageUrl);
                    }
                  } else {
                    this.addListPT(res.id);
                    this.ShowToast(1, "Thêm khóa học thành công");
                    (async () => {
                      await fireStorage.deleteImage(this.Course.imageUrl);
                    })();
                  }
                  this.$emit("load-data");
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
      }
    },
    checkTitle(submit) {
      this.isSubmitTitle = submit;
    },
    checkTime(submit) {
      this.isSubmitTime = submit;
    },
    checkPrice(submit) {
      this.isSubmitPrice = submit;
    },
    checkSubmitAll() {
      if (this.isSubmitPrice && this.isSubmitTime && this.isSubmitTitle) {
        return true;
      } else return false;
    },

    beforeUnmount() {
      // Giải phóng URL tạm thời khi component bị xóa
      if (this.urlImage) {
        URL.revokeObjectURL(this.urlImage);
      }
    },
    computed: {
      ...mapGetters({
        getBranch: GET_BRANCH_SELECTED,
      }),
    },
  },
  data() {
    return {
      drop: false,
      dropPT: false,
      Device: {
        id: 1,
        title: "Khóa học 1",
        range_process: 3,
        price: 100.0,
      },
      Course: {},
      UserTable: {},
      DeviceTypeId: "",
      courseStatus: [
        { name: "Đang sử dụng", id: true },
        { name: "Ngưng sử dụng", id: false },
      ],
      Discount: [
        { name: "Chưa giảm giá", id: 0 },
        { name: "10", id: 1 },
        { name: "20", id: 2 },
        { name: "30", id: 3 },
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
      //hiển thị đỏ nhập tên khi trống
      inValue_Name: {
        type: Boolean,
        default: true,
      },
      AlertNameMess: "",
      isOpenType: false,
      TypeList: [],
      urlImage: "",
      isImageUpdate: false,
      WhereValue: null,
      isSearching: false,
      isOpenImg: false,
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      ListPT: null,
      listPTAdd: null,
      isSubmitTitle: false,
      isSubmitTime: true,
      isSubmitPrice: false,
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
  max-width: 885px;
  min-height: 460px;
  position: relative;
  top: 100px;
  margin: auto auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
}
.popup-top {
  display: flex;
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
  height: 75%;
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
  margin: 15px;
  display: flex;
  align-items: center;
}
.popup-center-left-input .nd {
  width: 500px;
}
.popup-center-left-input .nd textarea {
  border: 1px solid #ababab;
  width: 100%;
  min-height: 50px;
  border-radius: 5px;
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
.input-left {
  width: 42%;
  height: 100%;
  position: relative;
  margin-left: 10%;
}
.popup-center-right {
  width: 30%;
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
  height: 40%;
  position: relative;
}
.popup-img {
  width: 60%;
  height: 170px;
  border: 1px solid #ababab;
  border-radius: 10%;
  overflow: hidden;
  margin: 5px auto;
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
.button-add {
  width: 150px;
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
