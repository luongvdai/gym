<template>
  <div class="profile-container">
    <div class="profile-body">
      <div class="body-left">
        <div class="user-avatar">
          <img
            class="avatar"
            :src="urlImage"
            v-if="urlImage"
            @click="openImg(urlImage)"
          />
          <img
            src="@\assets\photos\2.jpg"
            v-if="!urlImage"
            class="avatar"
            @click="openImg(urlImage)"
          />
        </div>
        <div class="user-fullname">
          <label for="">{{ this.User.fullName }}</label>
        </div>
        <div class="user-email">
          <label for="">{{ getBranchName(this.User.branch) }}</label>
        </div>
        <div class="update-avatar">
          <input
            @change="handleFileSelect"
            type="file"
            ref="fileImage"
            class="d-none"
            accept="image/jpeg, image/png"
          />
          <MButton
            v-if="!isReadOnly"
            :text="'Thay đổi ảnh'"
            :style="'width:140px;'"
            :class="'content-top-btn-content'"
            @click="openFileInput"
            class="btn-change-avatar"
          />
        </div>
      </div>
      <div class="body-right">
        <div class="body-right-top">
          <div class="right-header">
            <div class="right-header-label">
              <label>Thông tin cá nhân</label>
            </div>
            <div class="right-header-btn">
              <MButton
                :text="'Đổi mật khẩu'"
                class="button-add"
                :class="'content-top-btn-content'"
                :style="'width:140px;'"
                @click="togglePopup"
              />
            </div>
          </div>
          <div class="right-form">
            <div class="body-element">
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Họ và tên</label>
                  <label class="input-label sao">*</label>
                </div>
                <div class="input-bottom">
                  <MInput
                    :tab="1"
                    :maxlength="36"
                    :placeholder="'Nhập tên của bạn '"
                    @updateAlert="UpdateAlert"
                    v-model="User.fullName"
                    :readOnly="isReadOnly"
                    :style="inputStyle"
                    @isSubmit="checkFullName"
                  />
                </div>
              </div>
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Email</label>
                  <label class="input-label sao">*</label>
                </div>
                <div class="input-bottom">
                  <MInput
                    :tab="1"
                    :maxlength="36"
                    :placeholder="'Nhập địa chỉ email '"
                    @updateAlert="UpdateAlert"
                    v-model="User.email"
                    :style="{
                      'min-height': '42px',
                      'background-color': '#f0f0f0',
                    }"
                    :readOnly="true"
                  />
                </div>
              </div>
            </div>
            <div class="body-element">
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Số điện thoại</label>
                  <label class="input-label sao">*</label>
                </div>
                <div class="input-bottom">
                  <MInput
                    :tab="1"
                    :maxlength="36"
                    :placeholder="'Nhập số điện thoại '"
                    @updateAlert="UpdateAlert"
                    v-model="User.phoneNumber"
                    :style="inputStyle"
                    :readOnly="isReadOnly"
                    :typeInput="'phonenumber'"
                    @isSubmit="checkPhone"
                  />
                </div>
              </div>
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Ngày sinh</label>
                  <label class="input-label sao">*</label>
                </div>
                <div class="input-bottom">
                  <MDatetime
                    @getDateTime="getDate"
                    :style="'max-height: 42px;'"
                    v-model="User.dateOfBirth"
                  />
                </div>
              </div>
            </div>
            <div class="body-element">
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Địa chỉ</label>
                </div>
                <div class="input-bottom">
                  <MInput
                    :tab="1"
                    :maxlength="36"
                    :placeholder="'Nhập địa chỉ '"
                    @updateAlert="UpdateAlert"
                    v-model="User.address"
                    :readOnly="isReadOnly"
                    :style="inputStyle"
                    @isSubmit="checkAddress"
                  />
                </div>
              </div>
              <div class="form-body">
                <div class="input-top">
                  <label class="input-label">Giới tính</label>
                  <label class="input-label sao">*</label>
                </div>
                <div class="input-bottom">
                  <m-input-radio
                    class="input-gender"
                    name="gender"
                    :label="'Nam'"
                    @click="getGender(true)"
                    :checked="User.gender == true"
                    :readOnly="isReadOnly"
                  >
                  </m-input-radio>
                  <m-input-radio
                    class="input-gender"
                    name="gender"
                    :label="'Nữ'"
                    @click="getGender(false)"
                    :readOnly="isReadOnly"
                    :checked="User.gender == false"
                  ></m-input-radio>
                </div>
              </div>
            </div>
            <div class="body-button">
              <div class="body-button-left">
                <MButton
                  v-if="!isReadOnly"
                  :text="'Hủy'"
                  class="button-add"
                  :class="'btn-button-cancel'"
                  :style="'width:120px;'"
                  @click="this.cancelUpdate"
                />
              </div>
              <div class="body-button-right">
                <MButton
                  @click="updateProfile"
                  :text="updateButtonText"
                  class="button-add"
                  :class="'content-top-btn-content'"
                  :style="'width:120px;'"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="body-right-bottom">
          <div class="right-header bottom">
            <div class="right-header-label bottom">
              <label>Chỉ số cơ thể</label>
            </div>

            <div class="right-header-filter">
              <MDropItems
              v-if="drop"
                :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                :Drstyle="'width: 100%; top: 42px;right: 1px;'"
                :MDropSta="2"
                :label="'title'"
                :RecordDrop="listCourse"
                @dropitem-value="getCourseId"
              />
            </div>
                <div class="right-header-btnFilter">
           
                <MButton
                  :text="'Lọc'"
                  class="button-add"
                  :class="'content-top-btn-content'"
                  :style="'width:100px;  margin-top: 5px;'"
                  @click="getListGymInfor"
                />
            </div>
          </div>
          <div class="right-table">
            <table>
              <thead>
                <tr>
                  <th>Ngày cập nhật</th>
                  <th>Người cập nhật</th>
                  <th class="course-name">Khóa học</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in InforTable" :key="item.id">
                  <td>{{ formatUnixTime(item.updatedDate) }}</td>
                  <td>{{ getMentorName(item.mentor) }}</td>
                  <td class="course-name">{{ getCourseName(item.course) }}</td>
                  <td class="last-column">
                    <label @click="showPopupInfor(item)" for="">
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        class="icon icon-tabler icon-tabler-eye"
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
                        <path d="M10 12a2 2 0 1 0 4 0a2 2 0 0 0 -4 0" />
                        <path
                          d="M21 12c-2.4 4 -5.4 6 -9 6c-3.6 0 -6.6 -2 -9 -6c2.4 -4 5.4 -6 9 -6c3.6 0 6.6 2 9 6"
                        /></svg
                    ></label>
                  </td>
                </tr>
              </tbody>
            </table>
          
          </div>
          <div class="right-bottom">
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
      </div>
    </div>
    <MPopupEdit
      v-if="isShow"
      :PopupEdit_label="'Đổi mật khẩu'"
      :height="'height: 380px;'"
      :inputShow="3"
      @close-product-popup="closePopup"
    />
    <MPopupViewImgVue
      v-if="isOpenImg"
      :urlImg="urlImage"
      @close-popupimg-click="closePopupImg"
    />
    <GymInforPopup
      v-if="isShowGymInfor"
      :recordSelected="this.recordSelected"
      @close-popup-click="closeGymInforPopup"
    />
    <MLoading v-if="LoadingShow" />
  </div>
</template>

<script>
import MInput from "@/components/MInput/MInput.vue";
import MButton from "@/components/MButton/MButton.vue";
import MDatetime from "@/components/MDatetime/MDatetime.vue";
import MInputRadio from "@/components/MInputRadio/MInputRadio.vue";
import MPopupEdit from "@/components/MPopupEdit/MPopupEdit.vue";
import GymInforPopup from "@/components/MPopup/GymInforPopup.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import toast from "@/resouce/toast";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import { mapMutations } from "vuex";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import fireStorage from "@/plugins/fireStorage";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "ProfilePage",
  components: {
    MInput,
    MButton,
    MDatetime,
    MInputRadio,
    MPopupEdit,
    MPopupViewImgVue,
    GymInforPopup,
    MLoading,
    MDropItems,
  },
  created() {
    this.fetchProfile();
    this.getListGymInfor();
    this.getListCourse();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
        };
      } else {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: "Thất bại",
        };
      }
      this.displayToast(this.ToastInfor);
    },

    async fetchProfile() {
      this.LoadingShow = true;

      try {
        fetch(`${apiConfig}/users/my-profile`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.User = data;
            (async () => {
              this.urlImage = await fireStorage.displayImage(this.User.avatar); 
            })();
            this.LoadingShow = false;
            this.isSubmitAddress = true;
            this.isSubmitFullName = true;
            this.isSubmitPhoneNumber = true;
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
    cancelUpdate() {
      this.isReadOnly = true;
      this.updateButtonText = "Chỉnh sửa";
      this.inputStyle = { "min-height": "42px", "background-color": "#f0f0f0" };
    },
    async updateProfile() {
      if (this.updateButtonText == "Chỉnh sửa") {
        this.updateButtonText = "Lưu";
        this.isReadOnly = false;
        this.inputStyle = "min-height: 42px;";
      } else if (this.updateButtonText == "Lưu") {
        if (this.checkSubmitAll()) {
          let imageUrl = await fireStorage.getURL(
            "user",
            Date.now(),
            this.$refs.fileImage.files[0]
          );
          try {
            fetch(
              `${apiConfig}/users/my-profile/`,
              await fetchOptions("POST", {
                fullName: this.User.fullName,
                email: this.User.email,
                phoneNumber: this.User.phoneNumber,
                address: this.User.address,
                gender: this.User.gender,
                dateOfBirth: this.User.dateOfBirth,
                avatar: imageUrl,
              })
            )
              .then((res) => res.json())
              .then((res) => {
                if (res.status) {
                  if (res.status != "200") {
                    this.ShowToast(3, res.message);
                  }
                } else {
                  this.ShowToast((this.ToastStatus = 1));
                  this.fetchProfile();
                  this.updateButtonText = "Chỉnh sửa";
                  this.isReadOnly = true;
                  this.inputStyle = {
                    "min-height": "42px",
                    "background-color": "#f0f0f0",
                  };
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
      }
    },
    async getListGymInfor() {
      this.LoadingShow = true;
      const filter = `?courseId=${this.selectedCourseId}&size=5&page=${this.pagein - 1}`;
      try {
        fetch(
          `${apiConfig}/gym-user-information${filter}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.InforTable = data.content;
            this.pageSize = data.totalPages;
            this.totalItems = data.totalElements;
            this.LoadingShow = false;
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
    async getListCourse() {
      try {
        fetch(
          `${apiConfig}/user-courses/get-course-by-user`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listCourse = data.content;
            this.drop = true;
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
    togglePopup() {
      this.isShow = true;
    },
    closePopup(value) {
      this.isShow = value;
    },
    getDate(date) {
      this.User.dateOfBirth = date;
    },

    getGender(gender) {
      this.User.gender = gender;
    },
    getCourseId(selectedit){
      this.selectedCourseId = selectedit;
      this.pagein = 1;
    },
    closePopupImg() {
      this.isOpenImg = false;
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
    },
    openFileInput() {
      this.$refs.fileImage.click();
    },
    handleFileSelect(event) {
      const file = event.target.files[0];
      if (file && this.isImageFile(file)) {
        this.urlImage = URL.createObjectURL(file);
        this.isImageUpdate = this.Mode === 2 ? true : false;
        console.log(this.urlImage);
      } else {
        confirm("Chọn file hình ảnh");
      }
    },
    isImageFile(file) {
      const acceptedImageTypes = ["image/jpeg", "image/png"];
      return file && acceptedImageTypes.includes(file.type);
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
    getBranchName(branch) {
      if (branch) {
        return branch.name;
      } else return "";
    },
    getMentorName(mentor) {
      return mentor.fullName;
    },
    getCourseName(course) {
      return course.title;
    },
    showPopupInfor(item) {
      this.recordSelected = { ...item };
      this.isShowGymInfor = true;
    },
    closeGymInforPopup() {
      this.isShowGymInfor = false;
    },
    checkFullName(isSubmit) {
      this.isSubmitFullName = isSubmit;
    },
    checkPhone(isSubmit) {
      this.isSubmitPhoneNumber = isSubmit;
    },
    checkAddress(isSubmit) {
      this.isSubmitAddress = isSubmit;
    },
    checkSubmitAll() {
      if (
        this.isSubmitAddress &&
        this.isSubmitFullName &&
        this.isSubmitPhoneNumber
      ) {
        return true;
      } else return false;
    },
  },
  watch: {
    pagein() {
      this.getListGymInfor();
    },
  },
  data() {
    return {
      User: {},
      isShow: false,
      isReadOnly: true,
      inputStyle: { "min-height": "42px", "background-color": "#f0f0f0" },
      updateButtonText: "Chỉnh sửa",
      isOpenImg: false,
      urlImage: null,
      isImageUpdate: false,
      pageSize: 1,
      totalItems: "",
      InforTable: {},
      recordSelected: {},
      isShowGymInfor: false,
      pagein: 1,
      LoadingShow: false,
      isSubmitFullName: false,
      isSubmitAddress: false,
      isSubmitPhoneNumber: false,
      listCourse: [],
      drop: false,
      selectedCourseId:"",
    };
  },
};
</script>

<style scoped>
.profile-container {
  height: auto;
  width: 100%;
  display: flex;
}
.profile-body {
  margin: 0;
  padding-top: 40px;
  color: #2e323c;
  background: #f5f6fa;
  position: relative;
  height: 100%;
  width: 100%;
  padding: 40px 12%;
  display: flex;
  flex-wrap: wrap;
}
.body-left {
  width: 25%;
  background-color: white;
  height: 45vh;
  margin-right: 3%;
  border-radius: 5px;
  padding: 10px 10px;
}
.user-avatar {
  height: 55%;
  width: 100%;
}
.user-avatar img {
  width: 60%;
  height: 100%;
  border-radius: 50%;
}
.user-fullname {
  height: 20%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  font-size: 18px;
  font-weight: bold;
}
.user-email {
  height: 10%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  font-size: 13px;
  color: rgb(110, 109, 109);
}
.update-avatar {
  height: 15%;
  width: 100%;
  display: flex;
  align-items: end;
  justify-content: center;
}
.body-right {
  width: 72%;
  height: 100%;
}
.body-right-top {
  background-color: white;
  border-radius: 5px;
  width: 100%;
  height: 55vh;
  padding: 10px 0%;
  margin-bottom: 5vh;
}

.right-header {
  width: 100%;
  height: 10%;
  padding: 0 3%;
  display: flex;
}
.right-header.bottom {
  height: 15%;
}
.right-header-label {
  height: 100%;
  width: 70%;
  text-align: left;
  font-size: 24px;
  font-weight: bolder;
  color: green;
}
.right-header-label.bottom {
  width: 50%;
}
.right-header-btn {
  height: 100%;
  width: 30%;
  display: flex;
  justify-content: right;
  align-items: end;
}
.right-form {
  height: 90%;
  width: 100%;
  padding-top: 2%;
}
.body-element {
  width: 100%;
  height: 28%;
  display: flex;
  flex-wrap: wrap;
}
.form-body {
  width: 50%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  text-align: left;
  padding: 0 3%;
}
.input-top {
  width: 100%;
  height: 25%;
}
.input-label {
  font-size: 16px;
  color: #000;
}
.input-label.sao {
  color: red;
}
.input-bottom {
  width: 100%;
  height: 75%;
  display: flex;
}
.input-gender {
  width: 30%;
  align-items: center;
  height: 70%;
}
.body-button {
  height: 16%;
  width: 100%;
  display: flex;
  padding: 0 3%;
  justify-content: right;
}
.body-button-left {
  margin-right: 30px;
}
.body-right-bottom {
  background-color: white;
  border-radius: 5px;
  width: 100%;
  height: 65vh;
  padding: 10px 0%;
}
.right-header-filter{
  width: 30%;
  margin-right: 10px;
}
.right-header-btnFilter{
  width: 15%;
  display: flex;
  align-items: flex-start;

}
.right-table {
  padding: 5px 10%;
  height: 70%;
  width: 100%;
}
.right-table table {
  width: 100%;
  height: 100%;
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

.right-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}
.last-column {
  width: 20%;
}
.last-column label {
  cursor: pointer;
  color: green;
}
.last-column label:hover {
  color: red;
}

.right-bottom {
  height: 15%;
  width: 100%;
  display: flex;
}
.paging {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}
@media (max-width: 1200px) {
  .profile-body {
    padding: 30px 5%;
    height: 170vh;
  }
  .body-left {
    height: 250px;
    width: 100%;
    margin: 0;
    border-bottom: 1px solid black;
    border-radius: 0;
    padding-top: 15px;
  }
  .user-avatar {
    height: 120px;
  }
  .user-avatar img {
    height: 120px;
    width: 120px;
  }
  .body-right {
    width: 100%;
  }
  .body-right-top {
    height: 75vh;
  }
  .body-element {
    height: 30%;
  }
  .form-body {
    width: 100%;
    height: 50%;
  }
  .right-header-label {
    font-size: 18px;
  }
  .right-header-btn {
    align-items: flex-start;
  }
  .body-button {
    height: 10%;
  }
  table {
    font-size: 13px;
  }
  .course-name {
    display: none;
  }
}
</style>