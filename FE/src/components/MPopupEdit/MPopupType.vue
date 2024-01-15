<template>
  <div class="cover-popup">
    <div class="popupedit" :style="height">
      <div class="popupedit-top">
        <div class="popupedit-top-left">
          <div class="popupedit-top-left-label" :style="width">
            {{ Popup_label }}
          </div>
        </div>
        <div class="popupedit-top-right">
          <div class="popup-top-right btn">
            <button class="btn-exit" @click="closePopup">
              <i class="ti ti-x"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="popupedit-content">
        <div class="history-content-top">
          <label class="history-label name">Tên</label>
          <label class="history-label problem">Chức năng</label>
        </div>
        <div class="history-content">
          <div class="history-data" v-for="index in Type" :key="index">
            <div class="history-label name">
              <MInput
                class="popup-input b"
                :tab="1"
                :inValue="inValue_Name"
                :maxlength="20"
                :typeInput="'name'"
                @updateAlert="UpdateAlert"
                v-model="index.name"
                :style="'border: none;'"
              />
            </div>
            <div class="history-label problem">
              <label class="history-label edit" @click="addType(2, index.id, index.name)">Sửa</label>
              <label class="history-label delete" @click="openPopupAsk(index.id, index.name)">Xóa</label>
            </div>
          </div>
        </div>
        <div class="history-input">
          <div class="history-label name input">
            <MInput
              class="popup-input"
              :tab="1"
              :inValue="inValue_Name"
              :maxlength="50"
              :typeInput="'name'"
              @updateAlert="UpdateAlert"
              v-model="newType.name"
              ref="inputFocus"
            />
          </div>
        </div>
      </div>
      <div class="popupedit-bottom">
        <div class="popupedit-content-bottom-left">
          <div class="btn-product-popup-save1" @click="addType(1)">
            <MButton
              :tab="5"
              :ButtonCss="'content-top-btn-content'"
              :text="'Thêm'"
            />
          </div>
        </div>
        <div class="popupedit-content-bottom-right">
          <div @click="closePopup">
            <MButton :tab="7" :ButtonCss="'btn-button-cancel'" :text="'Hủy'" />
          </div>
        </div>
      </div>
    </div>
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskDelete"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="deleteType"
        @close-notification-click="ClosePopupAsk"
        :getRecordCode="PopupNotilable"
        :MPopupN="PopupNotiType"
      />
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
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
    MButton,
    MInput,
    MPopupNotification,
  },
  props: {
    height: String,
    Popup_label: String,
    id: String,
    Type: Array,
    urlType: String,
  },
  mounted() {
    this.handleLoopFocus();
  },
  created() {},
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closePopup() {
      this.$emit("close-popup-click");
    },

    /**
     * hàm tabindex vòng lặp
     */
    handleLoopFocus() {
      console.log(1);
      this.$refs.inputFocus.$el.focus();
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
      }else if (Tstatus == 4) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Xóa thành công",
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

    async addType(mode, id, name) {
      var method = "POST";
      var url = this.urlType;
      if (mode == 2) {
        method = "PUT";
        url = url + id;
        this.newType.name = name;
      }
      try {
        fetch(
          `${apiConfig}/` + url,
          await fetchOptions(method, this.newType)
        )
          .then((res) => res.json())
          .then(async (res) => {
            if (!res.id) {
              this.ShowToast(3, res.message);
            } else {
              this.newType = {};
              this.$emit("load_Type");
              if (mode == 2) {
                this.ShowToast((this.ToastStatus = 2));
              } else {
                this.ShowToast((this.ToastStatus = 1));
              }
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
    },
    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
    },

    openPopupAsk(id, name) {
        this.PopupNotilable = name;
        this.isShowAskDelete = true; //hiện popup hỏi người dùng
        this.idRecordDelete = id; //lưu id cần xóa
    },

    /**
     * Hàm xóa theo id
     */
    async deleteType() {
      try {
        var id = this.idRecordDelete;
        //đóng popup hỏi người dùng
        this.ClosePopupAsk();
        fetch(`${apiConfig}/` + this.urlType + id, await fetchOptions("DELETE"))
          .then((data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
              this.isShowAskDelete = true;
              this.PopupNotiType = 5;
              this.PopupNotilable = this.Popup_label;
            } else {
              this.$emit("load_Type");
              this.ShowToast((this.ToastStatus = 4));
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Xóa thất bại");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
  },
  data() {
    return {
      newType: {},
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      //lưu id  cần xóa
      idRecordDelete: 0,
      PopupNotilable: "",
      PopupNotiType: 2,
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
.popupedit {
  width: 500px;
  height: 370px;
  position: relative;
  top: 30%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
::-webkit-scrollbar {
  height: 8px;
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #fff;
  border-radius: 4px;
}
/* Handle */
::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 4px;
  cursor: pointer;
  width: 8px;
  height: 8px;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #7c869c;
}
.popupedit-top {
  width: 100%;
  height: 70px;
  display: flex;
  position: relative;
}
.popupedit-top-left {
  width: 50%;
  height: 100%;
}
.popupedit-top-left-label {
  height: 60px;
  width: 100%;
  font-size: 24px;
  font-weight: 700;
  margin-top: 17px;
}
.popupedit-top-right {
  position: absolute;
  right: 0;
  display: flex;
  top: 10px;
  width: 70px;
  height: 50px;
}
.popupedit-top-right {
  position: absolute;
  right: 0;
  display: flex;
  top: 10px;
  width: 70px;
  height: 50px;
}
.popupedit-content {
  width: 87%;
  height: 63%;
  border: 1px solid #bbbb;
  margin-left: 32px;
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
  right: 140%;
  top: 22%;
}
.popupedit-bottom {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 96%;
  height: 60px;
}
.popupedit-content-bottom-left {
  position: absolute;
  left: 15px;
  width: 30%;
  height: 100%;
  display: flex;
  align-items: center;
}
.popupedit-content-bottom-right {
  position: absolute;
  right: 0;
  width: 26%;
  height: 100%;
  display: flex;
  align-items: center;
}
.history-content-top {
  height: 20%;
  width: 100%;
  display: flex;
  align-items: center;
  font-size: 13px;
  border-bottom: 1px solid #bbbb;
}
.history-label.name {
  display: flex;
  width: 60%;
  margin-left: 30px;
}
.history-label.name.input {
  display: flex;
  width: 85%;
  margin-left: 30px;
}
.history-label.problem {
  width: 30%;
}
.history-content {
  width: 100%;
  height: 55%;
  overflow: scroll;
}
.history-data {
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 100;
  cursor: pointer;
}
.history-input {
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 100;
  cursor: pointer;
}
.popup-input {
  height: 100%;
}
.popup-input.b:hover{
  border: 1px solid #50b83c;
}
.history-label.delete,
.history-label.edit {
  margin-left: 20px;
  cursor: pointer;
  color: rgb(9, 130, 243);
}
</style>
