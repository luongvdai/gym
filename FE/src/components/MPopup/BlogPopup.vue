<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "bài viết" }}</label>
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
            <label class="label-infor">Chi tiết bài viết</label>
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
                  :maxlength="100"
                  :typeInput="String"
                  :placeholder="'Tiêu đề'"
                  @updateAlert="UpdateAlert"
                  v-model="Blog.title"
                  @isSubmit="checkTitle"
                />
                <div class="alertInputEm" v-if="isShowAlertName">
                  {{ AlertNameMess }}
                </div>
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Link đính kèm</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :style="'min-height: 36px;'"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="String"
                  :placeholder="'Link đính kèm'"
                  @updateAlert="UpdateAlert"
                  v-model="Blog.attach_url"
                />
              </div>
            </div>
          </div>
          <div class="popup-center-left-input">
            <div class="input-right">
              <div class="input-top">
                <label class="input-label">Tác giả</label>
              </div>
              <div class="input-bottom">
                <MInput
                  class="popup-input"
                  :tab="1"
                  :inValue="inValue_Name"
                  :maxlength="100"
                  :typeInput="String"
                  :placeholder="'Tác giả'"
                  @updateAlert="UpdateAlert"
                  v-model="Blogcreateby"
                  :readOnly="true"
                  :style="'min-height: 36px; cursor: no-drop; background-color: rgb(227 227 227)'"
                />
              </div>
            </div>
            <div class="input-left">
              <div class="input-top">
                <label class="input-label">Thể loại</label>
              </div>
              <div class="input-bottom c">
                <MCombobox
                  v-if="drop"
                  :Drstyle="'height: 100%;'"
                  :iconadd="roleValue == 'ADMIN'"
                  :inValueCombox="inValue_Unit"
                  :Comboboxmodel="Blog.typeId"
                  :DropboxItem="BlogTypeList"
                  :value="'id'"
                  :label="'name'"
                  :isShow="isShowDropbox"
                  :maxlength="100"
                  @combobox-value="getType"
                  @open-popup-edit="openTypeClick"
                  @handlSearchCombobox="searchCombobox"
                  :onSearch="isSearching"
                  ref="combobox"
                  :tab="3"
                />
                <button v-if="roleValue == 'ADMIN'" @click="openTypeClick" class="popup-settup">
                  <i class="ti ti-category"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="popup-center-left-input b">
            <div class="input-center">
              <div class="input-top">
                <label class="input-label">Nội dung</label>
              </div>
              <div class="input-bottom b">
                <MCKEditor v-model="Blog.content" :editorData="Blog.content" />
              </div>
            </div>
          </div>

          <div class="popup-center-left-input c">
            <div class="popup-center-left-input_left">
              <div class="input-add">
                <MButton
                  :text="popupLabel + 'bài viết'"
                  @click="addBlog"
                  :ButtonCss="'content-top-btn-content'"
                  class="button-add me-2"
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
            <div class="input-preview">
              <v-row justify="center">
                <v-dialog v-model="preview" width="800">
                  <template v-slot:activator="{ props }">
                    <v-btn color="white" v-bind="props"> Xem trước </v-btn>
                  </template>
                  <v-card>
                    <v-card-title>
                      <span class="text-h5">{{ Blog.title }}</span>
                    </v-card-title>
                    <v-card-text>
                      <div
                        v-html="
                          `<style scoped>img{width:100% !important; height: 100% !important; p{width:100%;}</style>${this.Blog.content}`
                        "
                      ></div>
                    </v-card-text>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-row>
            </div>
          </div>
        </div>
        <div class=""></div>
      </div>
    </div>
    <MPopupType
      v-if="isOpenType"
      :urlType="'blog-types/'"
      :Type="BlogTypeList"
      @close-popup-click="closeHistoryPop"
      @load_Type="loadBlogType"
      :Popup_label="'Loại bài viết'"
    />
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import MPopupType from "@/components/MPopupEdit/MPopupType.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import MCKEditor from "../MInput/MCKeditor.vue";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "BlogPopup",
  components: {
    MInput,
    MButton,
    MCombobox,
    MCKEditor,
    MPopupType,
  },
  props: {
    blogSelected: Object,
    Mode: Number,
    popupLabel: String,
    selectedBranch: String,
  },
  created() {
    try {
      var username = JSON.parse(localStorage.getItem("userData"))["username"];
    if (this.blogSelected) {
      this.Blog = { ...this.blogSelected };
      this.Blogcreateby = this.Blog.user.username;
    } else {
      this.Blogcreateby = username;
    }
    this.loadBlogType();
    this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
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
    getType(value) {
      this.Blog.typeId = value;
    },
    closeHistoryPop() {
      this.isOpenType = false;
    },
    openTypeClick() {
      this.isOpenType = true;
    },
    searchCombobox(value) {
      console.log(value);
      this.WhereValue = value;
      this.loadBlogType();
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
      this.isShowAlertCode = true;
      this.inValue_Code = false;
      this.isShowAlertName = true;
      this.inValue_Name = false;
      if (code == 404) {
        this.AlertNameMess = "Không tìm thấy tên người dùng";
      } else if (code == 401) {
        this.isShowAlertName = false;
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
      //trường hợp xóa ô input
      if (!this.Blog.title) {
        this.inValue_Name = false;
      }
    },
    validateContent() {
      try {
        //Giới hạn số lượng ký tự
        const maxlength = 2000;
        const textContent =
          new DOMParser().parseFromString(this.Blog.content, "text/html").body
            .textContent || "";
        const words = textContent.split(/\s+/);
        let validate = true;
        // Kiểm tra nếu độ dài của nội dung vượt quá maxlength
        if (words.length > maxlength) {
          this.ToastInfor = {
            show: true,
            icon: toast.Toastcssicon_faild,
            css: toast.Toastcss_faild,
            color: toast.ToastMess_color_faild,
            message: `Số lượng không vượt quá ${maxlength} ký tự`,
          };
          this.displayToast(this.ToastInfor);
          validate = false;
        }
        return validate;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    /**
     * hàm kiểm tra có rỗng mã, tên, đơn vị không
     * Nguyễn Văn Cương 2/10/2022
     */
    validateEmpty() {
      var validate = true; //dữ liệu cần thiết không trống
      //kiểm tra trường cần thiết có trống không
      if (!this.Blog.title) {
        validate = false;
      }
      return validate;
    },
    async loadBlogType() {
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
      if (where == null) {
        //nếu không có, mặc định là rỗng
        where = "";
      }
      const filter = `?keyword=${where}`;
      fetch(`${apiConfig}/blog-types` + filter, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.BlogTypeList = data;
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

    async addBlog() {
   if(this.isSubmitTitle){
     var method = "POST";
      var url = "blogs/";
      if (this.Mode == 2) {
        method = "PUT";
        url = url + this.Blog.id;
      }
      if (this.validateEmpty() && this.validateContent()) {
        try {
          fetch(
            `${apiConfig}/` + url,
            await fetchOptions(method, {
              title: this.Blog.title,
              content: this.Blog.content,
              attach_url: this.Blog.attach_url,
              typeId: this.Blog.typeId,
              branchId: this.selectedBranch,
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
  },
  watch: {},
  data() {
    return {
      drop: false,
      Blog: {},
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      BlogTypeList: [],
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
      AlertNameMess: "",
      AlertCodeMess: "",
      Blogcreateby: "",
      preview: false,
      isOpenType: false,
      isSearching: false,
      WhereValue: null,
      isSubmitTitle: false,
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
  max-width: 650px;
  height: 750px;
  position: relative;
  top: 20px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.popup-top {
  width: 100%;
  height: 12%;
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
.button-add {
  width: 150px;
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
  display: flex;
  align-items: center;
}
.label-infor {
  font-size: 17px;
  font-weight: bold;
}
.input-right {
  width: 45%;
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
  height: 73%;
}
.input-bottom.c {
  width: 95%;
  height: 50%;
  display: flex;
}
.input-preview {
  margin-top: 10px;
}
.popup-settup {
  width: 40px;
  height: 40px;
  display: flex;
  font-size: 23px;
  align-items: center;
  margin-left: 6px;
}
.popup-input {
  height: 100%;
}
.input-left {
  width: 42%;
  height: 70px;
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
}
.popup-img {
  width: 60%;
  height: 80%;
  border: 1px solid #ababab;
  border-radius: 10%;
}
.popup-center-right-btn {
  width: 100%;
  height: 20%;
}
.input-add {
  width: auto;
  height: auto;
}
.input-cancel {
  width: auto;
  height: auto;
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
.input-center {
  display: block;
  max-width: 600px;
  height: fit-content;
}
.input-bottom.b {
  width: 600px;
  height: 400px;
  overflow: auto;
}

.popup-center-left-input.c {
  display: flex;
  justify-content: space-between;
  padding-right: 20px;
  margin-top: 20px;
}
.popup-center-left-input_left {
  display: flex;
}
</style>
