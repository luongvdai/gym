<template>
  <div class="checkout-container">
    <div class="cartpage-container">
      <div class="cartpage-top">
        <div class="cartpage-top-label">
          <label>Lịch sử đăng ký khóa tập của bạn</label>
        </div>
        <div class="cartpage-top-tip">
          <label>Quản lý khóa tập bạn đã đăng ký, thanh toán, hủy</label>
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
                          v-if="index.Order.status != 'PENDING'"
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
                        <div
                          class="combobox"
                          v-if="index.Order.status == 'PENDING'"
                        >
                          <MCombobox
                            v-if="drop"
                            :Drstyle="'height: 78%;'"
                            :inValueCombox="inValue_Unit"
                            :DropboxItem="index.ptList"
                            :Comboboxmodel="ptId"
                            :indexOf="index"
                            :value="'id'"
                            :label="'username'"
                            :code="'gender'"
                            :CombolabelLeft="'Tên'"
                            :CombolabelRight="'Giới tính'"
                            :inputLabel="'Cập nhật PT'"
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
                    </div>
                    <div class="cart-item-center-input">
                      <div class="input-bottom">
                        <MInput
                          v-if="index.Order.status != 'PENDING'"
                          class="popup-input"
                          :tab="1"
                          :style="'min-height: 36px;'"
                          :inValue="inValue_Name"
                          :maxlength="100"
                          :placeholder="'Slot'"
                          v-model="index.OrderDetails.slot"
                          @updateAlert="UpdateAlert"
                        />
                        <MDropItems
                          v-if="index.Order.status == 'PENDING'"
                          :Inputstyle="'height: 90%;right: 0; background-color: #fff'"
                          :Drstyle="'width: 200%; top: 42px;right: -40px;'"
                          :DropItemsModel="index.OrderDetails.slot"
                          :RecordDrop="SlotItems"
                          :indexOf="index"
                          :MDropSta="2"
                          @dropitem-value="getSlot"
                          :label="'name'"
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
                    <div class="price-right">
                      <label>{{ FormatVND(index.Course.price) }}</label>
                    </div>
                  </div>
                  <div class="cart-item-btn">
                    <div class="btn-feedback">
                      <MButton
                        v-if="index.Order.status == 'PENDING'"
                        :tab="6"
                        :ButtonCss="'btn-button-save c'"
                        :text="'Thanh toán'"
                        @click="Payment(index.Order.id)"
                      />
                    </div>
                    <div class="btn-chat">
                      <MButton
                        v-if="index.Order.status == 'PENDING'"
                        :tab="6"
                        :ButtonCss="'btn-button-cancel'"
                        :text="'Hủy'"
                        @click="AskDelete(index.OrderDetails.id, index.Course.title)"
                      />
                      <MButton
                        v-if="index.Order.status == 'IN_PROGRESS'"
                        :tab="6"
                        :ButtonCss="'btn-button-save c'"
                        :text="'Liên hệ'"
                        @click="openFeedback"
                      />
                      <MButton
                        v-if="index.Order.status == 'COMPLETE'"
                        :tab="6"
                        :ButtonCss="'btn-button-save c'"
                        :text="'Mua lại'"
                        @click="BacktoCoursePage"
                      />
                      <MButton
                        v-if="index.Order.status == 'REJECT'"
                        :tab="6"
                        :ButtonCss="'btn-button-save c'"
                        :text="'Mua lại'"
                        @click="BacktoCoursePage"
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
    </div>
  </div>
</template>
<script>
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import FeedbackPopup from "@/components/MPopup/FeedbackPopup.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
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
    MDropItems,
    MCombobox,
  },
  created() {
    this.loadOrder();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    BacktoCoursePage() {
      this.$router.push("/courses");
    },

    async Payment(id) {
      this.isLoading = true;
      try {
        await fetch(
          `${apiConfig}/orders/new-payment?orderId=${id}&bankCode=NCB`,
          await fetchOptions("GET")
        )
          .then((data) => data.text())
          .then((data) => {
            if (!data) {
              this.ShowToast(3, data.message);
            } else {
              window.location.href = data;
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Thanh toán thất bại");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.isLoading = false;
      }
    },

    changeStatus(value) {
      try {
        switch (value) {
          case 1:
            this.StatusValue = "";
            break;
          case 2:
            this.StatusValue = "PENDING";
            break;
          case 3:
            this.StatusValue = "IN_PROGRESS";
            break;
          case 4:
            this.StatusValue = "COMPLETE";
            break;
          case 5:
            this.StatusValue = "FAILED";
            break;
          case 6:
            this.StatusValue = "REJECT";
            break;
          default:
            break;
        }
        this.pagein = 1;
        this.loadOrder();
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    getSlot(slot, index) {
      try {
        if (slot && index) {
          var slotMapping = {
            SLOT1: 1,
            SLOT2: 2,
            SLOT3: 3,
          };

          var defaultSlot = 4;

          var BindingSlot = slotMapping[slot] || defaultSlot;
          this.newSlot = BindingSlot;
          var courseId = index.Course.id;
          var orderDetailId = index.OrderDetails.id;
          this.newPTId = index.PT.id;
          this.ShowToast(1, "Chọn Slot thành công");
          this.EditOrder(courseId, orderDetailId);
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Chọn Slot thất bại");
      }
    },

    getPT(value, index) {
      try {
        if (value && index) {
          this.newPTId = value;
          this.ShowToast(1, "Chọn PT thành công");
          var courseId = index.Course.id;
          var orderDetailId = index.OrderDetails.id;
          var oldSlot = index.OrderDetails.slot;
          var slotMapping = {
            SLOT1: 1,
            SLOT2: 2,
            SLOT3: 3,
          };

          var defaultSlot = 4;

          var BindingSlot = slotMapping[oldSlot] || defaultSlot;
          this.newSlot = BindingSlot;
          this.EditOrder(courseId, orderDetailId);
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Chọn PT thất bại");
      }
    },

    async EditOrder(courseId, orderDetailId) {
      if (courseId && this.newPTId && this.newSlot) {
        try {
          fetch(
            `${apiConfig}/orders/update/` + orderDetailId,
            await fetchOptions("PUT", {
              courseId: courseId,
              ptId: this.newPTId,
              slot: this.newSlot,
            })
          )
            .then((res) => res.json().catch(() => res))
            .then((res) => {
              if (!res.status == 200) {
                this.ShowToast(3, res.message);
              } else {
                this.ShowToast(1, "Sửa thành công");
                this.loadOrder();
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Sửa thất bại");
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        }
      } else {
        this.ShowToast(3, "Hãy chọn người dùng, khóa học, PT");
      }
    },

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

    async loadOrder() {
      this.isLoading = true;
      try {
        this.CartItem = [];
        var status = this.StatusValue; //lưu dữ liệu tìm kiếm
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}&page=${
          this.pagein - 1
        }&size=1&status=${status}`;
        await fetch(`${apiConfig}/orders` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.content.length > 0) {
              this.Order = data.content;
              this.TotalCount = data.totalPages;
              if (this.Order.length > 0) {
                this.Order.forEach((i) => {
                  this.getOrderDetail(i.orderDetails, i);
                });
              }
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.isLoading = false;
      }
    },

    async getOrderDetail(orderDetails, order) {
      try {
        for (const i of orderDetails) {
          this.ptCourse = i.ptCourse;
          this.ptId = this.ptCourse.ptId;
          this.courseId = this.ptCourse.courseId;
          let ptList = [];
          if (order.status == "PENDING") {
            ptList = await this.getPtByCourseId(this.courseId);
          }
          if (i.slot == "SLOT1") {
            this.BindingSlot = 1;
          } else if (i.slot == "SLOT2") {
            this.BindingSlot = 2;
          } else if (i.slot == "SLOT3") {
            this.BindingSlot = 3;
          } else {
            this.BindingSlot = 4;
          }
          this.ptCourse.course.imageUrl = await fireStorage.displayImage(
            this.ptCourse.course.imageUrl
          );
          this.CartItem.push({
            Order: order,
            Course: this.ptCourse.course,
            OrderDetails: i,
            PT: this.ptCourse.user,
            ptList: ptList,
            slot: this.BindingSlot,
          });
          this.drop = true;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading(false);
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
          fetch(`${apiConfig}/orders/delete/` + id, await fetchOptions("DELETE"))
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

    FormatStatusOrder(status) {
      try {
        if (status == "PENDING") {
          return "CHƯA THANH TOÁN";
        } else if (status == "IN_PROGRESS") {
          return "ĐANG DUYỆT";
        } else if (status == "COMPLETE") {
          return "ĐÃ THANH TOÁN";
        } else if (status == "REJECT") {
          return "ĐÃ HỦY";
        } else if (status == "FAILED") {
          return "THẤT BẠI";
        } else {
          this.statusLabel = null;
          return "";
        }
      } catch (error) {
        console.log(error);
      }
    },

    FormatStatus(status) {
      try {
        if (status == "PENDING") {
          return "Bạn cần thanh toán";
        } else if (status == "IN_PROGRESS") {
          return "Hệ thống đang xét duyệt";
        } else if (status == "COMPLETE") {
          return "Đã đăng ký khóa học";
        } else if (status == "REJECT") {
          return "Đã hủy khóa học";
        } else if (status == "FAILED") {
          return "Thanh toán thất bại";
        } else {
          this.statusLabel = null;
          return "";
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

    openFeedback() {
      this.isShowFeedback = true;
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
      Course: null,
      Order: [],
      Orderdetail: null,
      User: {},
      ptCourse: null,
      ptId: null,
      orderId: null,
      StatusValue: "",
      WhereValue: null,
      TotalCount: 1,
      pagein: 1,
      isShowFeedback: false,
      isShowAskDelete: false,
      Ordername: null,
      Orderid: null,
      tab: 1,
      items: [
        { value: 1, name: "Tất cả" },
        { value: 2, name: "Chờ thanh toán" },
        { value: 3, name: "Đang duyệt" },
        { value: 4, name: "Đã duyệt" },
        { value: 5, name: "Thất bại" },
        { value: 6, name: "Đã hủy" },
      ],
      PTList: null,
      SlotItems: [
        { name: "Slot 1 Từ 6 giờ 30 - 9 giờ 30", id: "SLOT1" },
        { name: "Slot 2 Từ 9 giờ 30 - 12 giờ 30", id: "SLOT2" },
        { name: "Slot 3 Từ 12 giờ 30 - 15 giờ 30", id: "SLOT3" },
        { name: "Slot 4 Từ 15 giờ 30 - 18 giờ 30", id: "SLOT4" },
      ],
      BindingSlot: null,
      isLoading: false,
      newPTId: null,
      newSlot: null,
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
  padding: 3% 3% 0% 3%;
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
.top-status-right {
  display: none;
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
  width: 40%;
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
  width: 70%;
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
  width: 100%;
  text-align: end;
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
.combobox {
  width: 100%;
  height: 83%;
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
