<template>
  <div class="checkout-container">
    <div class="cartpage-container">
      <div class="cartpage-top">
        <div class="cartpage-top-label">
          <label>Quản lý khóa tập của bạn</label>
        </div>
        <div class="cartpage-top-tip">
          <label>Lịch sử đăng ký khóa tập của bạn</label>
        </div>
      </div>
      <div class="cartpage-center">
        <div class="cartpage-center-left">
          <div class="left-top">
            <v-tabs v-model="tab" bg-color="transparent" color="basil" grow>
              <v-tab
                v-for="item in items"
                :key="item"
                :value="item.value"
                @click="changeStatus(item.value)"
              >
                {{ item.name }}
              </v-tab>
            </v-tabs>
          </div>
          <div class="left-search">
            <v-text-field
              :loading="loading"
              density="compact"
              variant="solo"
              label="Tìm kiếm khóa học"
              append-inner-icon="mdi-magnify"
              single-line
              hide-details
              v-model="WhereValue"
              @click:append-inner="loadOrder"
            ></v-text-field>
          </div>
          <div class="left-center" v-if="isLoading && isLoading === true">
            <v-skeleton-loader
              v-for="i in 3"
              :key="i"
              class="mx-auto my-3 border"
              max-width="1000"
              type="article"
            ></v-skeleton-loader>
          </div>
          <div class="left-center" v-else>
            <div v-if="CartItem.length > 0">
            <div
              class="left-center-item"
              v-for="index in CartItem"
              :key="index"
            >
              <div class="cart-item-left">
                <div class="cart-item-left-img">
                  <img
                    v-if="index.Course.imageUrl"
                    class="cart-img"
                    :src="index.Course.imageUrl"
                  />
                  <img
                    v-if="!index.Course.imageUrl"
                    class="cart-img"
                    src="@\assets\photos\2.jpg"
                  />
                </div>
              </div>
              <div class="cart-item-center">
                <div class="cart-item-center-top">
                  <div class="cart-item-label">
                    <label>{{ index.Course.title }}</label>
                    <div class="top-status-right">
                      {{ FormatStatusOrder(index.Order.status) }}
                    </div>
                  </div>
                  <div class="cart-item-prop">
                    <div class="cart-item-month">
                      <label>{{ index.Course.rangeProcess }} tháng</label>
                    </div>
                  </div>
                </div>
                <div class="cart-item-center-bottom">
                  <div class="cart-item-center-combobox">
                    <div class="input-bottom">
                      <MInput
                        class="popup-input"
                        :tab="1"
                        :style="'min-height: 36px;'"
                        :inValue="inValue_Name"
                        :maxlength="100"
                        :inputLabel="'PT'"
                        :placeholder="'PT'"
                        v-model="index.PT.fullName"
                        @updateAlert="UpdateAlert"
                      />
                    </div>
                  </div>
                  <div class="cart-item-center-input">
                    <div class="input-bottom">
                      <MInput
                        class="popup-input"
                        :tab="1"
                        :style="'min-height: 36px;'"
                        :inValue="inValue_Name"
                        :maxlength="100"
                        :placeholder="'Slot'"
                        v-model="index.Order.slot"
                        @updateAlert="UpdateAlert"
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div class="cart-item-right">
                <div class="cart-item-right-status">
                  <div class="status-left">
                    <label>{{ FormatStatus(index.Order.status) }}</label>
                  </div>
                  <div class="status-right">
                    {{ FormatStatusOrder(index.Order.status) }}
                  </div>
                </div>
                <div class="cart-item-price">
                  <div class="price-left"></div>
                  <div class="price-right">
                    <label>{{ FormatVND(index.Course.price) }}</label>
                  </div>
                </div>
                <div class="cart-item-btn">
                  <div class="btn-feedback">
                    <MButton
                      v-if="index.Order.status == 'ALREADY_FEEDBACK'"
                      :tab="6"
                      :ButtonCss="'content-top-btn-content'"
                      :text="'Xem đánh giá'"
                      @click="openViewFeedback(index.Order.id)"
                    />
                  </div>
                  <div class="btn-chat">
                    <div
                      class="btn-chat_pt"
                      v-if="index.Order.status == 'IN_PROGRESS'"
                    >
                      <MButton
                        :tab="6"
                        :ButtonCss="'btn-button-cancel'"
                        :text="'Chat'"
                        @click="OpenChat(index.PT.id)"
                      />
                    </div>
                    <MButton
                      v-if="index.Order.status == 'COMPLETE_COURSE'"
                      :tab="6"
                      :ButtonCss="'btn-button-save c'"
                      :text="'Đánh giá PT'"
                      @click="openFeedback(index.Order.id)"
                    />
                    <MButton
                      v-if="index.Order.status == 'ALREADY_FEEDBACK'"
                      :tab="6"
                      :ButtonCss="'btn-button-cancel'"
                      :text="'Mua lại'"
                      @click="openCourse(index.Course.id)"
                    />
                    <MButton
                      v-if="index.Order.status == 'OUT_COURSE'"
                      :tab="6"
                      :ButtonCss="'btn-button-save c'"
                      :text="'Mua lại'"
                      @click="openCourse(index.Course.id)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="left-center" v-if="CartItem.length <= 0">
            <div class="left-center-item">
              <div class="cart-item-left">
                <div class="cart-item-left-img">
                  <img class="cart-img" src="@\assets\photos\2.jpg" />
                </div>
              </div>
              <div class="cart-item-center">
                <div class="cart-item-center-top">
                  <div class="cart-item-label">
                    <label>Bạn chưa có đăng ký nào cả</label>
                  </div>
                </div>
              </div>
            </div>
          </div>
          </div>
        </div>
        <v-pagination
          :length="TotalCount"
          v-model="pagein"
          :total-visible="7"
          prev-icon="ti ti-chevron-left"
          next-icon="ti ti-chevron-right"
        ></v-pagination>
      </div>
      <FeedbackPopup
        v-if="this.isShowFeedback"
        @close-popup-feedback="closeFeedback"
        :userCourseId="UserCourseId"
        @load-data="loadOrder"
      />
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
      <MPopupViewFeedback
        v-if="isViewFeedback"
        :userCourseId="userCourseId"
        @close-feedback-view="closeViewFeedback"
      />
    </div>
  </div>
</template>
<script>
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MPopupViewFeedback from "@/components/MPopupEdit/MPopupViewFeedback.vue";
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import FeedbackPopup from "@/components/MPopup/FeedbackPopup.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import {
  GET_BRANCH_HOME_SELECTED,
  SET_CHAT_ISOPENED,
} from "@/stores/modules/storeconstants";
import { mapGetters, mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: {
    MButton,
    MInput,
    FeedbackPopup,
    MPopupNotification,
    MPopupViewFeedback,
  },
  created() {
    console.log("here");
    (async () => {
      await this.loadOrder();
    })();
  },
  methods: {
    async OpenChat(index) {
      await this.showChat({
        show: false,
      });
      await this.showChat({
        show: true,
        receivedId: index,
      });
    },
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
      showChat: SET_CHAT_ISOPENED,
    }),

    changeStatus(value) {
      try {
        switch (value) {
          case 1:
            this.StatusValue = "";
            break;
          case 2:
            this.StatusValue = "IN_PROGRESS";
            break;
          case 3:
            this.StatusValue = "COMPLETE_COURSE";
            break;
          case 4:
            this.StatusValue = "ALREADY_FEEDBACK";
            break;
          case 5:
            this.StatusValue = "OUT_COURSE";
            break;
          default:
            break;
        }
        this.loadOrder();
      } catch (error) {
        console.log(error);
      }
    },

    openCourse(id){
      this.$router.push("/courses/" + id);
    },

    openViewFeedback(id) {
      this.userCourseId = id;
      this.isViewFeedback = true;
    },
    closeViewFeedback() {
      this.isViewFeedback = false;
    },

    async loadOrder() {
      this.isLoading = true;
      try {
        await this.showLoading(true);

        this.CartItem = [];
        var status = this.StatusValue; //lưu dữ liệu tìm kiếm
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }

        const filter = `?keyword=${where}&page=${
          this.pagein - 1
        }&size=5&status=${status}`;
        const response = await fetch(
          `${apiConfig}/user-courses` + filter,
          await fetchOptions("GET")
        );
        const data = await response.json().catch(() => response);

        this.Order = data.content;
        this.TotalCount = data.totalPages;

        await Promise.all(
          this.Order.map(async (i) => {
            this.course = i.ptCourse.course;
            this.ptId = i.ptCourse.ptId;
            this.courseId = i.ptCourse.courseId;

            i.ptCourse.course.imageUrl = await fireStorage.displayImage(
              i.ptCourse.course.imageUrl
            );

            this.CartItem.push({
              Order: i,
              Course: i.ptCourse.course,
              PTCourse: i.ptCourse,
              PT: i.ptCourse.user,
            });
          })
        );
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.isLoading = false;
        await this.showLoading(false);
      }
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

    async deleteOrder() {
      try {
        var id = this.Orderid;
        if (id) {
          fetch(`${apiConfig}/orders/` + id, await fetchOptions("DELETE"))
            .then((data) => {
              if (data.status != "200") {
                this.ShowToast(3, data.message);
              } else {
                this.ShowToast(1, "Xóa thành công");
                this.CartItem = [];
                this.isShowAskDelete = false;
                this.loadOrder();
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Xóa thất bại");
            });
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    FormatStatus(status) {
      try {
        if (status == "COMPLETE_COURSE") {
          return "Bạn cần đánh giá PT này";
        } else if (status == "ALREADY_FEEDBACK") {
          return "Bạn đã đánh giá PT này";
        } else {
          return "";
        }
      } catch (error) {
        console.log(error);
      }
    },

    FormatStatusOrder(status) {
      try {
        if (status == "IN_PROGRESS") {
          return "ĐANG TẬP";
        } else if (status == "COMPLETE_COURSE") {
          return "HOÀN THÀNH";
        } else if (status == "ALREADY_FEEDBACK") {
          return "ĐÃ ĐÁNH GIÁ";
        } else {
          return "ĐÃ HỦY";
        }
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

    openFeedback(id) {
      this.isShowFeedback = true;
      this.UserCourseId = id;
    },

    closeFeedback() {
      this.isShowFeedback = false;
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
          message: "Xóa thành công",
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
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
  },
  watch: {
    pagein() {
      this.loadOrder();
    },
  },
  data() {
    return {
      courseId: null,
      drop: false,
      CartItem: [],
      course: null,
      Order: null,
      User: {},
      ptCourse: null,
      ptId: null,
      statusLabel: "",
      StatusValue: "",
      WhereValue: null,
      TotalCount: 1,
      pagein: 1,
      isShowFeedback: false,
      tab: 1,
      items: [
        { value: 1, name: "Tất cả" },
        { value: 2, name: "Đang tập" },
        { value: 3, name: "Hoàn thành" },
        { value: 4, name: "Đã đánh giá" },
        { value: 5, name: "Đã hủy" },
      ],
      UserCourseId: null,
      isViewFeedback: false,
      userCourseId: null,
      isLoading: false,
    };
  },
};
</script>
<style scoped>
.checkout-container {
  width: 100%;
  min-height: 150vh;
  padding: 3% 12%;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
}
.cartpage-container {
  padding: 3% 5%;
  border-radius: 15px;
  background-color: #fff;
}
.cartpage-top {
  width: 100%;
  height: 20%;
}
.cartpage-top-label {
  font-size: 30px;
  font-weight: bold;
  height: 60px;
}
.cartpage-center {
  width: 100%;
  min-height: 60%;
  padding: 3% 6% 0% 6%;
}
.cartpage-center-left {
  width: 100%;
  height: 100%;
}
.left-top,
.left-search {
  width: 100%;
  height: 50px;
  font-size: 23px;
  font-weight: bold;
  display: flex;
}
.left-search {
  width: 100%;
  margin-top: 30px;
  padding: 0 2%;
}
.left-center {
  width: 100%;
  min-height: 250px;
  padding: 20px;
  background-color: #fff;
}
.left-center-item {
  margin-top: 30px;
  width: 100%;
  height: 165px;
  display: flex;
  padding: 20px 20px;
  border-bottom: 1px solid #bbbb;
  background-color: #fff;
  border-radius: 13px;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
}
.cart-item-left {
  width: 20%;
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
  width: 45%;
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
  width: 42%;
  height: 100%;
}
.cart-item-right-status {
  width: 100%;
  height: 30%;
  display: flex;
}
.status-left {
  width: 60%;
  text-align: end;
  padding: 5px 13px;
  border-right: 1px solid #bbbb;
  color: #26aa99;
}
.status-right {
  padding: 5px 0;
  width: 45%;
  color: red;
}
.top-status-right {
  display: none;
}
.cart-item-price {
  width: 100%;
  height: 45%;
  display: flex;
  align-items: center;
}
.price-left {
  width: 80%;
  text-align: end;
  padding: 5px 0;
  text-decoration-line: line-through;
  color: #000;
  opacity: 0.26;
}
.price-right {
  padding: 5px 0;
  font-size: 14px;
  width: 30%;
  color: red;
}
.cartpage-center-right {
  width: 35%;
  height: 100%;
  margin-left: 60px;
}
.input-bottom {
  width: 90%;
  height: 73%;
  cursor: no-drop;
}
.cart-item-btn {
  width: 100%;
  height: 50%;
  display: flex;
}
.btn-feedback,
.btn-chat {
  width: 50%;
  height: 100%;
  position: relative;
}
.popup-input {
  pointer-events: none;
}
.btn-chat_pt {
  position: relative;
}
.notification {
  position: absolute;
  top: 0;
  right: 20px;
  font-size: 1.5em;
  color: rgb(116, 116, 160);
}
@media (max-width: 1200px) {
  .checkout-container {
    padding: 3%;
  }
  .cartpage-container {
    padding: 3% 3%;
  }
  .cartpage-top-label {
    font-size: 22px;
    padding: 0;
    height: 80px;
  }
  .cartpage-center {
    display: block;
    padding: 3% 0% 0% 0%;
  }
  .cartpage-center-left,
  .cartpage-center-right {
    width: 100%;
    margin-top: 25px;
  }
  .left-center {
    padding: 20px 0;
    min-height: 200px;
  }
  .left-center-item {
    padding: 20px 5px;
    height: 100%;
    display: block;
  }
  .cart-item-left {
    display: none;
  }
  .cart-item-center {
    width: 100%;
    height: 65%;
    margin-bottom: 5px;
  }
  .cart-item-center-input {
    width: 40%;
  }
  .cart-item-price {
    height: 58%;
  }
  .cart-item-center-top {
    padding: 0 7px;
  }
  .left-top {
    font-size: 20px;
  }
  .cartpage-center-right {
    margin-left: 0;
  }
  .cart-item-right {
    width: 100%;
    height: 70px;
  }
  .cart-item-center-combobox {
    height: 56px;
  }
  .input-bottom {
    height: 100%;
  }
  .status-left {
    width: 60%;
    padding: 5px;
    text-align: justify;
  }
  .cart-item-month {
    align-items: center;
    height: 34px;
  }
  .cart-item-right-status {
    display: none;
  }
  .top-status-right {
    display: block;
    padding: 5px 0;
    width: 45%;
    color: red;
    width: 70%;
    text-align: end;
    font-size: 15px;
    font-weight: normal;
  }
  .price-right {
    width: 100%;
    text-align: end;
  }
  .btn-feedback,
  .btn-chat {
    width: 50%;
  }
}
</style>
