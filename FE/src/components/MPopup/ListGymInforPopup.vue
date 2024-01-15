<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="right-header">
        <div class="right-header-label">
          <label>Quản lí lịch sử chỉ số cơ thể</label>
          <button class="btn-exit" @click="closePopup">
            <i class="ti ti-x"></i>
          </button>
        </div>
      </div>
      <div class="right-table">
        <table>
          <thead>
            <tr>
              <th>Ngày cập nhật</th>
              <th>Khóa học</th>
              <th>Học viên</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in InforTable" :key="item.id">
              <td>{{ formatUnixTime(item.updatedDate) }}</td>
              <td>{{ getCourseName(item.course) }}</td>

              <td>{{ getUserName(item.user) }}</td>
              <td class="last-column">
                <label @click="showPopupInfor(item)" for="">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="icon icon-tabler icon-tabler-edit"
                    width="30"
                    height="30"
                    viewBox="0 0 24 24"
                    stroke-width="2"
                    stroke="currentColor"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                    <path
                      d="M7 7h-1a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-1"
                    />
                    <path
                      d="M20.385 6.585a2.1 2.1 0 0 0 -2.97 -2.97l-8.415 8.385v3h3l8.385 -8.415z"
                    />
                    <path d="M16 5l3 3" /></svg
                ></label>
              </td>
              <td class="delete-column">
                <label @click="openAskDelete(item.id)" for="">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="icon icon-tabler icon-tabler-trash"
                    width="30"
                    height="30"
                    viewBox="0 0 24 24"
                    stroke-width="2"
                    stroke="currentColor"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                    <path d="M4 7l16 0" />
                    <path d="M10 11l0 6" />
                    <path d="M14 11l0 6" />
                    <path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" />
                    <path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" />
                  </svg>
                </label>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="total">
          <label
            >Tổng số người dùng
            <label class="total-label">{{ totalItems }}</label></label
          >
        </div>
      </div>
      <div class="right-bottom">
        <div class="button-add">
          <MButton
            :text="'Thêm mới'"
            class="button-add-btn"
            :class="'content-top-btn-content'"
            @click="showPopupInfor"
          ></MButton>
        </div>
        <div class="paging">
          <v-pagination
            :length="pageSize"
            v-model="pagein"
            :total-visible="7"
            prev-icon="ti ti-chevron-left"
            next-icon="ti ti-chevron-right"
          ></v-pagination>
        </div>
      </div>
    </div>
    <MLoading v-if="LoadingShow" />
    <InfoGymPopup
      v-if="isShowGymInfor"
      :recordSelected="this.recordSelected"
      @close-popup-click="closeGymInforPopup"
      @load-data="getUserInfor"
      :popupLabel="this.PopupLabel"
      :UserId="selectedUserId"
      :UserCourseId="selectedUserCourseId"
    />
    <MPopupNotification
      v-if="isShowAskDelete"
      :record="'bản ghi này'"
      @popup-ask-cance="ClosePopupAsk"
      @agree-delete-click="deleteGymInfor"
      @close-notification-click="ClosePopupAsk"
      :getRecordCode="getrecorddetetename"
      :MPopupN="2"
    />
  </div>
</template>

<script>
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import InfoGymPopup from "@/components/MPopup/InfoGymPopup.vue";
import MButton from "@/components/MButton/MButton.vue";
import toast from "@/resouce/toast";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import MLoading from "@/components/MLoading/MLoading.vue";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "List GymInfor",
  components: {
    MButton,
    MLoading,
    InfoGymPopup,
    MPopupNotification,
  },
  props: {
    UserId: String,
    UserCourseId: String,
  },
  mounted() {
    if (this.UserId && this.UserCourseId) {
      this.selectedUserId = this.UserId;
      this.selectedUserCourseId = this.UserCourseId;
      this.getUserInfor();
    }
  },

  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    showPopupUpdateInfor(item) {
      this.recordSelected = item;
      this.isShowGymInfor = true;
    },
    showPopupAddInfor() {
      this.isShowGymInfor = true;
    },
    closePopup() {
      this.$emit("close-popup-click");
    },
    openAskDelete(id) {
      this.deleteRecordId = id;
      this.isShowAskDelete = true;
    },
    formatUnixTime(unixTimestamp) {
      try {
        //const utcTime = new Date(unixTimestamp); // Nhân với 1000 để chuyển sang milliseconds
        //const localTime = new Date(utcTime.getTime() + 7 * 60 * 60 * 1000); // Áp dụng múi giờ Việt Nam (UTC+7)
        let localTime = new Date(unixTimestamp);

        const day = localTime.getDate();
        const month = localTime.getMonth() + 1; // Tháng được đếm từ 0
        const year = localTime.getFullYear();
        const hours = localTime.getHours();
        const minutes = localTime.getMinutes();

        const formattedDateTime = `${hours}:${minutes} - ${day}/${month}/${year}`;

        return formattedDateTime; // Trả về ngày và giờ đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
      }
    },
    getUserName(user) {
      return user.fullName;
    },
    getCourseName(course) {
      return course.title;
    },
    async getUserInfor() {
      const id = this.selectedUserId;
      const filter = `&size=7&page=${this.pagein - 1}`;

      try {
        this.LoadingShow = true;
        fetch(
          `${apiConfig}/gym-information?userId=${id}&${filter}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.InforTable = data.content;
              this.pageSize = data.totalPages;
              this.totalItems = data.totalElements;

              this.userId = this.InforTable[0].userId;
              this.userCourseId = this.InforTable[0].userCourseId;
              this.popupOpen = true;
            }
            
          })
          .catch((res) => {
            console.log(res);
          });
      } catch (error) {
        console.log(error);
      }
      this.LoadingShow = false;
    },
    async deleteGymInfor() {
      const id = this.deleteRecordId;
      try {
        fetch(
          `${apiConfig}/gym-information/${id}`,
          await fetchOptions("DELETE")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
              this.isShowAskDelete = false;
            } else {
              this.isShowAskDelete = false;
              this.getUserInfor(this.selectedUserId);
              this.ShowToast((this.ToastStatus = 1));
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
    showPopupInfor(item) {
      this.recordSelected = { ...item };
      this.isShowGymInfor = true;
    },
    closeGymInforPopup() {
      this.isShowGymInfor = false;
    },
    ClosePopupAsk() {
      this.isShowAskDelete = false;
    },
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Xóa thành công",
        };

        //trường hợp toast cập nhật thành công
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
  watch: {
    pagein() {
      this.getUserInfor();
    },
  },
  data() {
    return {
      InforTable: {},
      selectedUserId: null,
      LoadingShow: false,
      pageSize: 1,
      pagein: 1,
      totalItems: "",
      recordSelected: {},
      isShowGymInfor: false,
      userCourseId: "",
      userId: "",
      deleteRecordId: "",
      isShowAskDelete: false,
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
  width: 900px;
  height: 600px;
  position: relative;
  top: 10%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
.right-header {
  width: 100%;
  height: 12%;
  padding: 1% 3%;
  display: flex;
}
.right-header-label {
  height: 100%;
  width: 100%;
  text-align: center;
  font-size: 24px;
  font-weight: bolder;
  color: green;
}
.right-table {
  padding: 5px 6%;
  height: 70%;
  width: 100%;
}
.right-table table {
  width: 100%;
  height: 90%;
}
.right-table table tr {
  height: 45px;
}
.right-table thead th {
  background-color: #3498db;
  color: white;
  font-weight: bold;
  text-align: center;
}
.right-table tbody td {
  border: 1px solid #ccc;
}
.right-table tbody tr{
  max-height: 30px;
}
.right-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}
.last-column,
.delete-column {
  width: 10%;
}
.last-column label {
  cursor: pointer;
  color: green;
}
.last-column label:hover,
.delete-column:hover {
  color: red;
}
.total {
  height: 10%;
  width: 100%;
  padding: 0 6%;
  text-align: right;
}
.right-bottom {
  height: 15%;
  width: 100%;
  display: flex;
  padding: 0px 30px;
}
.paging {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
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
.button-add {
  display: flex;
  width: 15%;
  align-items: center;
}
.paging {
  width: 85%;
}
</style>