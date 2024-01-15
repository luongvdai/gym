<template>
  <div class="cover-popup">
    <div class="popup-request">
      <div class="popup-request-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">Chỉ định nhân viên</label>
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
      <div class="popup-request-center">
        <div class="popup-center-left-input">
          <div class="input-right">
            <MInputSearch
              @InputWhere="getSearchUser"
              :placeholder="'Tìm nhân viên theo tên'"
              :iconsearch="'icon-search'"
            />
            <div ref="infor">
              <MPopupSearch
                :searchItems="UserTable"
                :isUser="true"
                v-if="dropSearch"
                @choise-value="BindingUser"
              />
            </div>
          </div>
        </div>
        <div class="user-infor">
          <div class="user-avatar">
            <img class="avatar" src="@\assets\photos\pt1.jpg" />
          </div>
          <div class="user-label">
            <div class="user-label-top">
              <div class="user user-name">
                <label>Tên:</label>
              </div>
              <div class="user user-yearold">
                <label>Tuổi:</label>
              </div>
              <div class="user user-gender">
                <label>Giới tính:</label>
              </div>
              <div class="user user-role">
                <label>Vai trò:</label>
              </div>
            </div>
            <div class="user-label-bottom" v-if="User">
              <div class="user user-name">
                <label>{{ User.username }}</label>
              </div>
              <div class="user user-yearold">
                <label>{{ formatUnixTime(User.dateOfBirth) }}</label>
              </div>
              <div class="user user-gender">
                <label>{{ formatGenderRecord(User.gender) }}</label>
              </div>
              <div class="user user-role">
                <label>{{ User.role.position }}</label>
              </div>
            </div>
          </div>
        </div>
        <div class="popup-center-left-input">
          <div class="input-add me-2">
            <MButton
              text="Xác nhận"
              @click="agreeRequest"
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
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MPopupSearch from "@/components/MPopupEdit/MPopupSearch.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "RequestPopup",
  components: {
    MInputSearch,
    MPopupSearch,
    MButton,
  },
  props: {
    orderSelected: Object,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  data() {
    return {
      UserTable: [],
      User: null,
      dropSearch: false,
    };
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    closePopup() {
      this.$emit("close-request-click");
    },
    /**
     * hàm nhấn nút hủy xóa nhân viên
     */
    popupaskcance() {
      this.$emit("popup-ask-cance");
    },

    agreeRequest() {
      this.$emit("agree-request", this.User);
    },

    getSearchUser(value) {
      this.loadUser(value);
      console.log(1);
    },

    BindingUser(user) {
      this.User = user;
      this.dropSearch = false;
    },

    async loadUser(where) {
      try {
        const filter = `?branchId=${this.getBranch}&keyword=${where}&roleId=3`;
      fetch(`${apiConfig}/manage/users/` + filter, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.UserTable = data.content;
          if (where == null) {
            this.dropSearch = false;
          } else {
            this.dropSearch = true;
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

    /**
     * hàm format giới tính
     */
    formatGenderRecord(gender) {
      if (gender == true) {
        return "Nam";
      } else return "Nữ";
    },
    /**
     * hàm format UnixTime to date
     */
    formatUnixTime(unixTimestamp) {
      try {
        var date = new Date(unixTimestamp); // Multiply by 1000 to convert to milliseconds
        var day = date.getDate();
        var month = date.getMonth() + 1; // Months are zero-based
        var year = date.getFullYear();

        // Format the date as dd/mm/yyyy
        const formattedDate = `${day}/${month}/${year}`;

        return formattedDate;
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * hàm click outsite
     * Nguyễn Văn Cương 01/10/2022
     */
     clickEventInterrupt(event) {
      try {
        if (this.dropSearch == true) {
          //kiểm tra xem con chuột có click vào dropitem
          const isClick = this.$refs.infor.contains(event.target);
          if (!isClick) {
            this.dropSearch = false;
          }
        }
      } catch (error) {
        console.log(error);
      }
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
          message: "Thành công",
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
    
  },
  computed: {
      ...mapGetters({
        getBranch: GET_BRANCH_SELECTED,
      }),
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
  z-index: 200;
}
.popup-request {
  width: 600px;
  height: 310px;
  position: relative;
  top: 30%;
  margin: 0 auto;
  background-color: #fff;
  padding: 10px;
}
.popup-request-top {
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
  font-size: 23px;
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
.popup-request-center {
  width: 100%;
  height: 75%;
  padding: 0 5%;
  position: relative;
}
.popup-center {
  width: 100%;
  height: 75%;
  padding: 0 5%;
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
  margin: 15px 0;
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
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
}
.user-infor {
  width: 100%;
  height: 100px;
  border: 1px solid #bbbb;
  border-radius: 5px;
  display: flex;
  padding: 10px 10px;
}
.user-avatar {
  width: 15%;
  height: 100%;
}
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}
.user-label {
  width: 90%;
  height: 100%;
  display: block;
  padding: 10px;
}
.user-label-top {
  width: 100%;
  height: 50%;
  display: flex;
  font-weight: bold;
}
.user-label-bottom {
  width: 100%;
  height: 50%;
  display: flex;
}
.user {
  width: 20%;
  height: 100%;
  text-align: center;
  padding: 0 7px;
  border-right: 1px solid #bbbb;
}
.user.user-gender,
.user.user-name {
  width: 30%;
}

.user.user-role {
  border: none;
}
.popup-center-left-input {
  width: 100%;
  height: 25%;
  margin: 15px 0;
  display: flex;
  align-items: center;
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
</style>