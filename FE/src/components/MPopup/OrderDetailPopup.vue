<template>
  <div class="cover-popup">
    <div class="popup" :style="height">
      <div class="popup-top">
        <div class="popup-top-left">
          <div class="popup-top-left label">
            <label class="label-device">{{ popupLabel + "đăng ký" }}</label>
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
        <div class="popup-center-left-input">
          <label class="label-infor">Chọn người dùng</label>
        </div>
        <div class="popup-center-left-input">
          <div class="input-right">
            <MInputSearch
              @InputWhere="getSearchUser"
              :placeholder="'Tìm người dùng theo tên'"
              :iconsearch="'icon-search'"
            />
            <div ref="inforuser">
              <MPopupSearch
                :searchItems="UserTable"
                :isUser="true"
                v-if="dropUser"
                @choise-value="BindingUser"
              />
            </div>
          </div>
        </div>
        <div class="user-infor">
          <div class="user-avatar">
            <img
              v-if="!userImage || userImage == ''"
              class="avatar"
              src="@\assets\photos\avatar.png"
            />
            <img v-if="userImage" class="avatar" :src="userImage" />
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
              <div class="user user-address">
                <label>Địa chỉ:</label>
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
              <div class="user user-address">
                <label>{{ User.address }}</label>
              </div>
            </div>
          </div>
        </div>
        <div class="popup-center-left-input">
          <label class="label-infor">Chọn khóa học</label>
        </div>
        <div class="popup-center-left-input">
          <div class="input-right">
            <MInputSearch
              @InputWhere="getSearchCourse"
              :placeholder="'Tìm khóa học theo tên'"
              :iconsearch="'icon-search'"
            />
            <div class="infor" ref="infor">
              <MPopupSearch
                :searchItems="CourseTable"
                :isUser="false"
                v-if="dropCourse"
                @choise-value="BindingCourse"
              />
            </div>
            <div class="total-details">
              <label class="total-left b">Tổng tiền:</label>
              <label class="total-right b">{{ FormatVND(totalPrice) }}</label>
            </div>
          </div>
        </div>
        <div class="course-infor">
          <div class="left-center-item" v-for="item in Course" :key="item">
            <div class="cart-item-left">
              <div class="cart-item-left-img">
                <img
                  v-if="!item.course.imageUrl"
                  class="cart-img"
                  src="@\assets\photos\2.jpg"
                />
                <img
                  v-if="item.course.imageUrl"
                  class="cart-img"
                  :src="item.course.imageUrl"
                />
              </div>
            </div>
            <div class="cart-item-center">
              <div class="cart-item-center-top">
                <div class="cart-item-label">
                  <label>{{ item.course.title }}</label>
                </div>
                <div class="cart-item-prop">
                  <div class="cart-item-month">
                    <label>{{ item.course.rangeProcess }} tháng</label>
                  </div>
                </div>
              </div>
              <div class="cart-item-center-bottom">
                <div class="cart-item-center-combobox">
                  <div class="input-bottom">
                    <MCombobox
                      v-if="drop"
                      :Drstyle="'height: 78%;'"
                      :inValueCombox="inValue_Unit"
                      :DropboxItem="item.ptList"
                      :Comboboxmodel="ptId"
                      :indexOf="item.course.id"
                      :value="'id'"
                      :label="'username'"
                      :code="'gender'"
                      :CombolabelLeft="'Tên'"
                      :CombolabelRight="'Giới tính'"
                      :inputLabel="'Chọn PT'"
                      :isShow="isShowDropbox"
                      :maxlength="100"
                      :isInfor="true"
                      @combobox-value="getPT"
                      @handlSearchCombobox="searchCombobox"
                      :onSearch="isSearching"
                      ref="combobox"
                      :tab="1"
                    />
                  </div>
                </div>
                <div class="cart-item-center-input">
                  <div class="input-bottom">
                    <MDropItems
                      :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                      :Drstyle="'width: 200%; top: 42px;right: -40px;'"
                      :DropItemsModel="1"
                      :RecordDrop="SlotItems"
                      :indexOf="item.course.id"
                      :MDropSta="2"
                      @dropitem-value="getSlot"
                      :label="'name'"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div class="cart-item-right">
              <div class="cart-item-right-btn">
                <div
                  class="cart-item-delete"
                  @click="AskDelete(item.course.id, item.course.title)"
                >
                  <MTooltip :content="'Click để xóa'" />
                  <i class="ti ti-trash"></i>
                </div>
              </div>
              <div class="cart-item-price">
                <label>{{ FormatVND(item.course.price) }}</label>
              </div>
            </div>
          </div>
        </div>

        <div class="popup-center-left-input">
          <div class="input-add me-2">
            <MButton
              :text="popupLabel + 'đăng ký'"
              @click="SignCourse"
              class="button-add"
              :ButtonCss="'content-top-btn-content'"
            />
          </div>
          <div class="input-cancel">
            <MButton
              :ButtonCss="'btn-button-cancel'"
              :text="'Hủy'"
              @click="closePopup"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskDelete"
        :record="'khóa học'"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="deleteOrder"
        @close-notification-click="ClosePopupAsk"
        :getRecordCode="Ordername"
        :MPopupN="2"
      />
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
//import MInput from "@/components/MInput/MInput.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import MPopupSearch from "@/components/MPopupEdit/MPopupSearch.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "CoursePopup",
  components: {
    MButton,
    MInputSearch,
    MPopupNotification,
    MDropItems,
    MCombobox,
    MPopupSearch,
    MTooltip,
  },
  props: {
    courseSelected: Object,
    Mode: Number,
    popupLabel: String,
    selectedBranch: String,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
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

    AskDelete(id, name) {
      this.isShowAskDelete = true; //hiện popup hỏi người dùng
      this.Ordername = "<" + name + ">";
      this.Orderid = id;
    },

    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
    },

    /**
     * Hàm xóa đăng ký
     */
    deleteOrder() {
      try {
        if (this.Orderid) {
          this.itemOrder.forEach((i) => {
            if (i.courseId == this.Orderid) {
              this.itemOrder.splice(this.itemOrder.indexOf(i), 1);
            }
          });
          this.Course.forEach((i) => {
            if (i.id == this.Orderid) {
              this.totalPrice = this.totalPrice - i.price;
              this.Course.splice(this.Course.indexOf(i), 1);
            }
          });
          this.isShowAskDelete = false;
          this.ShowToast(1, "Xóa khóa học thành công");
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Xóa khóa học thất bại");
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

    getSearchCourse(value) {
      this.loadCourses(value);
    },

    /***
     * Hàm binding course 
     */
    async BindingCourse(course) {
      try {
        var isHave = false;
      this.Course.forEach((i) => {
        if (i.id == course.id) {
          isHave = true;
          this.ShowToast(3, "Khóa tập này đã được chọn");
        }
      });
      if (isHave == false) {
        let ptList = await this.getPtByCourseId(course.id);
        course.imageUrl = await fireStorage.displayImage(course.imageUrl);
        if (ptList && ptList.length > 0) {
          this.Course.push({ course: course, ptList: ptList });
          this.itemOrder.push({
            ptId: null,
            courseId: course.id,
            slot: 1,
          });
          this.totalPrice = this.totalPrice + course.price;
        }
      }
      this.drop = true;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Thêm khóa học thất bại");
      }
    },

    /**
     * Hàm load danh sách khóa học
     */
    async loadCourses(where) {
      try {
        const filter = `?branchId=${this.selectedBranch}&keyword=${where}`;
      fetch(`${apiConfig}/courses/` + filter, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.CourseTable = data.content;
          if (where == null) {
            this.dropCourse = false;
          } else {
            this.dropCourse = true;
          }
        })
        .catch((res) => {
          console.log(res);
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    getSearchUser(value) {
      this.loadUser(value);
    },

    /**
     * Hàm binding user
     */
    BindingUser(user) {
      this.User = user;
      (async () => {
        this.userImage = await fireStorage.displayImage(this.User.avatar);
      })();
      this.dropUser = false;
    },

    /**
     * Hàm load dữ liệu người dùng
     */
    async loadUser(where) {
      try {
        const filter = `?branchId=${this.selectedBranch}&keyword=${where}&roleId=5`;
      fetch(`${apiConfig}/manage/users/` + filter, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.UserTable = data.content;
          if (where == null) {
            this.dropUser = false;
          } else {
            this.dropUser = true;
          }
        })
        .catch((res) => {
          console.log(res);
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm lấy pt theo khóa học
     */
    async getPtByCourseId(id) {
      try {
        const response = await fetch(
          `${apiConfig}/pt-courses-user/mentor?courseId=${id}`,
          await fetchOptions("GET")
        );
        const data = await response.json().catch(() => response);
        return data.content;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    getSlot(slot, index) {
      try {
        if (index) {
          this.itemOrder.forEach((i) => {
            if (i.courseId == index) {
              i.slot = slot;
              this.ShowToast(1, "Cập nhật Slot thành công");
            }
          });
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Cập nhật Slot thất bại");
      }
    },

    getPT(value, index) {
      try {
        if (index) {
          this.itemOrder.forEach((i) => {
            if (i.courseId == index) {
              i.ptId = value;
              this.ShowToast(1, "Chọn PT thành công");
            }
          });
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Chọn PT thất bại");
      }
    },

    validatePT() {
      try {
        var result = false;
      this.itemOrder.forEach((i) => {
        if (i.ptId) {
          result = true;
        }
      });
      return result;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm thêm đăng ký
     */
    async SignCourse() {
      if (
        this.itemOrder.some((i) => i.ptId !== null) &&
        this.itemOrder.length > 0 &&
        this.User
      ) {
        try {
          fetch(
            `${apiConfig}/manage/orders/create-for-user`,
            await fetchOptions("POST", {
              buyer: this.User.username,
              items: this.itemOrder,
            })
          )
            .then((res) => res.json().catch(() => res))
            .then((res) => {
              if (!res.id) {
                this.ShowToast(3, res.message);
              } else {
                this.closePopup();
                this.ShowToast(1, "Đăng ký thành công");
                this.$emit("load-data");
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Đăng ký thất bại");
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        }
      } else {
        this.ShowToast(3, "Hãy chọn người dùng, khóa học, PT");
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
    FormatVND(price) {
      try {
        if (price) {
          price = price.toLocaleString("it-IT", {
            style: "currency",
            currency: "VND",
          });
          return price;
        } else {
          return "0 VNĐ";
        }
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
        if (this.dropCourse == true) {
          //kiểm tra xem con chuột có click vào dropitem
          const isClick = this.$refs.infor.contains(event.target);
          if (!isClick) {
            this.dropCourse = false;
          }
        }
        if (this.dropUser == true) {
          //kiểm tra xem con chuột có click vào dropitem
          const isClick = this.$refs.inforuser.contains(event.target);
          if (!isClick) {
            this.dropUser = false;
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_SELECTED,
    }),
  },
  data() {
    return {
      drop: false,
      dropPT: false,
      dropUser: false,
      dropCourse: false,
      CourseTable: [],
      UserTable: [],
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      openDropitem: false,
      WhereValue: null,
      User: null,
      userImage: null,
      Course: [],
      PTList: {},
      SlotItems: [
        { name: "Slot 1 Từ 6 giờ 30 - 9 giờ 30", id: 1 },
        { name: "Slot 2 Từ 9 giờ 30 - 12 giờ 30", id: 2 },
        { name: "Slot 3 Từ 12 giờ 30 - 15 giờ 30", id: 3 },
        { name: "Slot 4 Từ 15 giờ 30 - 18 giờ 30", id: 4 },
      ],
      itemOrder: [],
      isShowAskDelete: false,
      Orderid: null,
      Ordername: null,
      totalPrice: 0,
      isChoicePT: false,
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
  min-height: 600px;
  position: relative;
  top: 70px;
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
  height: 70%;
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
.user-infor {
  width: 100%;
  height: 100px;
  border: 1px solid #bbbb;
  border-radius: 5px;
  display: flex;
  padding: 10px 10px;
}
.user-avatar {
  width: 10%;
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
  text-align: left;
  padding: 0 10px;
  border-right: 1px solid #bbbb;
  overflow: hidden;
}
.user.user-address {
  width: 40%;
  border: none;
}
.course-infor {
  width: 100%;
  height: 350px;
  border: 1px solid #bbbb;
  border-radius: 5px;
  overflow-x: hidden;
  overflow-y: scroll;
}
.left-center-item {
  width: 100%;
  height: 150px;
  display: flex;
  padding: 20px 20px;
  border-bottom: 1px solid #bbbb;
  background-color: #fff;
}
.cart-item-left {
  width: 30%;
  height: 100%;
}
.cart-item-left-img {
  width: 150px;
  height: 110px;
  border: 1px solid #bbbb;
}
.cart-img {
  width: 100%;
  height: 100%;
}
.cart-item-center {
  width: 63%;
  height: 100%;
}
.cart-item-center-top {
  width: 100%;
  height: 40%;
}
.cart-item-center-bottom {
  width: 100%;
  height: 60%;
  display: flex;
}
.cart-item-center-combobox {
  width: 60%;
  height: 100%;
  display: flex;
  align-items: end;
  position: relative;
}
.cart-item-center-input {
  width: 30%;
  height: 100%;
  display: flex;
  align-items: end;
  position: relative;
}
.cart-item-label {
  font-size: 17px;
  font-weight: bold;
  display: flex;
}
.cart-item-month {
  display: flex;
}
.cart-item-right {
  width: 15%;
  height: 100%;
}
.cart-item-right-btn {
  width: 100%;
  height: 30%;
  display: flex;
  align-items: flex-end;
  margin-left: 50px;
}
.cart-item-delete {
  width: 26px;
  height: 30px;
  font-size: 25px;
  border-radius: 5px;
  background-color: #bbbb;
  display: flex;
  align-items: center;
  position: relative;
}
.cart-item-price {
  width: 100%;
  height: 70%;
  display: flex;
  align-items: flex-end;
}
.total-details {
  float: right;
  width: 100%;
  height: 50%;
  display: flex;
  align-items: center;
  font-size: 15px;
  font-weight: bold;
}
.total-left {
  width: 80px;
  display: flex;
  font-size: 15px;
  margin-right: 5px;
}
.infor {
  width: 310%;
}
.button-add {
  width: 150px;
}

@media (min-width: 1580px) {
  .popup {
    max-height: 750px;
    top: 20px;
  }
  .course-infor {
    height: 300px;
  }
}
</style>
