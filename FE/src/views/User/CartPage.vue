<template>
  <div class="cartpage">
    <div class="cartpage-container">
      <div class="cartpage-top">
        <div class="cartpage-top-label">
          <label>Danh sách đăng ký khóa tập của bạn</label>
        </div>
        <div class="cartpage-top-tip">
          <label>Kiểm tra lại khóa tập của bạn trước khi thanh toán</label>
        </div>
      </div>
      <div class="cartpage-center">
        <div class="cartpage-center-left">
          <div class="left-top">
            <label>Khóa tập</label>
          </div>
          <div class="left-center" v-if="isLoading && isLoading === true">
            <v-skeleton-loader
              v-for="i in 4"
              :key="i"
              class="mx-auto my-3 border"
              max-width="600"
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
                        <MCombobox
                          v-if="drop"
                          :Drstyle="'height: 78%;'"
                          :inValueCombox="inValue_Unit"
                          :DropboxItem="index.ptList"
                          :Comboboxmodel="index.ptId"
                          :indexOf="index.Course.id"
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
                          :indexOf="index.Course.id"
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
                      @click="AskDelete(index.Order.id, index.Course.title, index.Course.id)"
                    >
                      <MTooltip :content="'Click để xóa'" />
                      <i class="ti ti-trash"></i>
                    </div>
                  </div>
                  <div class="cart-item-price">
                    <label>{{ FormatVND(index.Course.price) }}</label>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="CartItem.length <= 0">
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
          <div class="container-left-bottom" id="Payment" v-if="isPaymentPage">
            <div class="left-top payment">
              <label class="bottom-header-label">Thanh toán</label>
            </div>
            <div class="left-bottom-payment">
              <div class="payment-header">
                <div class="vnpay">
                  <img class="vnpay-icon" src="@\assets\icons\vnpaylogo.jpg" />
                </div>
              </div>
              <div class="payment-content">
                <div class="payment-content-header">
                  Chọn phương thức thanh toán
                </div>
                <div class="payment-content-method">
                  <div class="method-label" @click="isBank = !isBank">
                    <label>Thẻ nội địa và tài khoản ngân hàng</label>
                  </div>
                  <div class="method-icon"></div>
                </div>
                <div class="payment-bank" v-if="isBank">
                  <div
                    class="payment-bank-item"
                    v-for="index in bankList"
                    :key="index"
                    @click="Payment(index.code)"
                  >
                    <img class="bank-logo" :src="index.logo" />
                  </div>
                </div>
                <div class="payment-content-method">
                  <div class="method-label">
                    <label>Mã QR hỗ trợ VNPAY</label>
                  </div>
                  <div class="method-icon b"></div>
                </div>
                <div class="payment-content-method" @click="PaymentByCash">
                  <div class="method-label">
                    <label>Thanh toán bằng tiền mặt tại phòng tập</label>
                  </div>
                  <div class="method-icon c"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="cartpage-center-right">
          <div class="right-top">
            <label>Tổng tiền</label>
          </div>
          <div class="right-center">
            <div class="right-center-item">
              <div class="right-center-item-top">
                <div class="right-item-top-total">
                  <label class="total-left">Tổng số tiền cần trả:</label>
                  <label class="total-right">{{ FormatVND(totalPrice) }}</label>
                </div>
              </div>
              <div class="right-center-item-center">
                <div class="right-item-center-totalmoney">
                  <label class="total-left b">Tổng tiền:</label>
                  <label class="total-right b">{{
                    FormatVND(totalPrice)
                  }}</label>
                </div>
                <a
                  href="#Payment"
                  class="right-item-center-btn"
                  @click="SignCourse"
                >
                  <MButton
                    class="discount-btn"
                    :tab="6"
                    :ButtonCss="'btn-button-save d'"
                    :text="'THANH TOÁN'"
                  />
                </a>
              </div>
            </div>
          </div>
          <div class="container-left-top">
            <div class="left-top-header">
              <div class="header-avatar">
                <v-avatar size="40px" class="avatar-image">
                  <v-img
                    v-if="!userImage"
                    alt="Avatar"
                    src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                  ></v-img>
                  <v-img v-if="userImage" alt="Avatar" :src="userImage"></v-img>
                </v-avatar>
                <label class="avatar-name">{{ this.User.fullName }}</label>
              </div>
            </div>
            <div class="left-top-body">
              <div class="left-top-element">
                <div class="left-top-label">
                  <label>Email</label>
                </div>
                <div class="email-value">
                  <input type="text" :value="this.User.email" />
                </div>
              </div>

              <div class="left-top-element">
                <div class="left-top-label">
                  <label>Địa chỉ</label>
                </div>
                <div class="address-value">
                  <input type="text" :value="this.User.address" />
                </div>
              </div>
              <div class="left-top-element">
                <div class="left-top-label">
                  <label>Số điện thoại</label>
                </div>
                <div class="email-value">
                  <input type="text" :value="this.User.phoneNumber" />
                </div>
              </div>
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
  </div>
</template>
<script>
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MButton from "@/components/MButton/MButton.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import toast from "@/resouce/toast";
import { mapGetters, mapMutations } from "vuex";
import { GET_ORDERSTATUS } from "@/stores/modules/storeconstants";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: {
    MButton,
    MCombobox,
    MTooltip,
    MDropItems,
    MPopupNotification,
  },
  computed: {
    ...mapGetters({
      getOrderStatus: GET_ORDERSTATUS,
    }),
  },
  created() {
    (async () => {
      await this.loadCart();
      await this.getInfor();
      await this.loadBank();
    })();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

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
        this.ptId = value;
        if (index) {
          this.itemOrder.forEach((i) => {
            if (i.courseId == index) {
              i.ptId = value;
              this.ShowToast(1, "Cập nhật PT thành công");
            }
          });
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Cập nhật PT thất bại");
      }
    },

    async SignCourse() {
      try {
        await fetch(
          `${apiConfig}/orders`,
          await fetchOptions("POST", {
            items: this.itemOrder,
          })
        )
          .then((res) => res.json().catch(() => res))
          .then((res) => {
            if (!res.id) {
              this.ShowToast(3, res.message);
            } else {
              this.orderId = res.id;
              this.isPaymentPage = true;
              this.ShowToast(1, "Đăng ký thành công");
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
    },

    async Payment(bankCode) {
      await this.showLoading(true);
      if (this.orderId) {
        try {
          await fetch(
            `${apiConfig}/orders/new-payment?bankCode=${bankCode}&orderId=${this.orderId}`,
            await fetchOptions("GET")
          )
            .then((data) => data.text())
            .then((data) => {
              if (!data) {
                this.showLoading(false);
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
        }
      } else {
        this.ShowToast(3, "Đăng ký không tồn tại");
      }
      await this.showLoading(false);
    },

    async CheckDone(id) {
      try {
        await fetch(`${apiConfig}/orders/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status == "IN_PROGRESS") {
              this.CartItem.forEach((i) => {
                this.deleteOrder(i.Order.id);
              });
              this.$router.push("/home");
            }
          })
          .catch((res) => {
            console.log(res.message);
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    PaymentByCash() {
      this.showLoading(true);
      try {
        this.CartItem.forEach((i) => {
          this.deleteOrder(i.Order.id);
        });
        this.ShowToast(1, "Đăng ký thành công");
        this.$router.push({
          path: "/ordersuccess",
          query: {
            transactionId: "none",
            orderSuccess: true,
            bankCode: "none",
            amount: "none",
          },
        });
      } catch (error) {
        console.log(error.message);
        this.ShowToast(3, "Thanh toán thất bại");
      }
      this.showLoading(false);
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

    async loadCart(isDelete) {
      if (isDelete == null) {
        this.isLoading = true;
      }
      this.PTList = null;
      try {
        await fetch(`${apiConfig}/prepare-orders`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.Order = data.content;
            if (this.getOrderStatus == false) {
              await this.getOrderDetail(data.content);
            } else {
              this.Order.forEach(async (i) => {
                await this.deleteOrder(i.id);
              });
            }
          })
          .catch((res) => {
            this.isLoading = false;
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        this.isLoading = false;
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.isLoading = false;
      }
    },
    async getOrderDetail(order) {
      try {
        for (const i of order) {
          this.ptCourse = i.ptCourse;
          this.ptId = this.ptCourse.ptId;
          this.courseId = this.ptCourse.courseId;

          let ptList = await this.getPtByCourseId(this.courseId);
          this.ptCourse.course.imageUrl = await fireStorage.displayImage(
            this.ptCourse.course.imageUrl
          );

          await this.CartItem.push({
            Course: this.ptCourse.course,
            Order: i,
            ptId: this.ptCourse.ptId,
            ptList: ptList,
          });
          this.itemOrder.push({
            ptId: this.ptCourse.ptId,
            courseId: this.ptCourse.courseId,
            slot: 1,
          });
          this.drop = true;
          // Note: Adjust this line based on your actual data structure
          this.totalPrice += this.ptCourse.course.price;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.showLoading(false);
      }
    },

    async getInfor() {
      try {
        await fetch(`${apiConfig}/users/my-profile`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.User = data;
            (async () => {
              this.userImage = await fireStorage.displayImage(this.User.avatar);
            })();
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

    AskDelete(id, name, courseId) {
      this.isShowAskDelete = true; //hiện popup hỏi người dùng
      this.Ordername = "<" + name + ">";
      this.Orderid = id;
      this.deleteCourseId = courseId;
    },

    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
    },

    async deleteOrder(idAll) {
      await this.showLoading(true);
      try {
        if (idAll == null) {
          idAll = this.Orderid;
        }
        await fetch(
          `${apiConfig}/prepare-orders/` + idAll,
          await fetchOptions("DELETE")
        )
          .then(async (data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
            } else {
              if (idAll == null) {
                this.ShowToast(1, "Xóa thành công");
              }
              this.itemOrder.forEach((i) => {
                if (i.courseId == this.deleteCourseId) {
                  this.itemOrder.splice(i, 1);
                }
              });
              this.CartItem = [];
              this.totalPrice = 0;
              this.isShowAskDelete = false; //đóng popup hỏi người dùng
              await this.loadCart(this.isShowAskDelete);
              this.isLoading = false;
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
      this.isLoading = false;
      await this.showLoading(false);
    },

    async loadBank() {
      await this.showLoading(true);
      try {
        fetch(
          "https://api.vietqr.io/v2/banks?utm_source=j2team&utm_medium=url_shortener&utm_campaign=bank-list-api",
          fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.bankList = response.data;
          })
          .catch((error) => {
            console.log(error.message);
            this.ShowToast(3, "Không thể hiển thị ngân hàng");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      await this.showLoading(false);
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
          message: message,
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
  props: {},

  data() {
    return {
      courseId: null,
      drop: false,
      CartItem: [],
      Course: null,
      Order: null,
      User: {},
      PTList: null,
      ptCourse: null,
      ptId: null,
      totalPrice: 0,
      itemOrder: [],
      orderId: null,
      bankList: [],
      isPaymentPage: false,
      isBank: false,
      isShowAskDelete: false,
      Orderid: null,
      Ordername: null,
      userImage: null,
      SlotItems: [
        { name: "Slot 1 Từ 6 giờ 30 - 9 giờ 30", id: 1 },
        { name: "Slot 2 Từ 9 giờ 30 - 12 giờ 30", id: 2 },
        { name: "Slot 3 Từ 12 giờ 30 - 15 giờ 30", id: 3 },
        { name: "Slot 4 Từ 15 giờ 30 - 18 giờ 30", id: 4 },
      ],
      orderSuccessPageInitialized: false,
      isloading: false,
      deleteCourseId: null,
    };
  },
};
</script>
<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.cartpage {
  width: 100%;
  min-height: 100vh;
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
  display: flex;
  padding: 5% 0;
}
.cartpage-center-left {
  width: 60%;
  height: 100%;
}
.left-top {
  width: 100%;
  height: 0px;
  font-size: 23px;
  font-weight: bold;
  display: flex;
}
.left-top.payment {
  height: 50px;
}
.right-top {
  width: 100%;
  height: 50px;
  font-size: 23px;
  font-weight: bold;
  display: flex;
}
.left-center {
  width: 100%;
  min-height: 250px;
  padding: 20px 0;
  background-color: #fff;
}
.left-center-item {
  margin-top: 30px;
  width: 100%;
  height: 150px;
  display: flex;
  padding: 20px 20px;
  border-bottom: 1px solid #bbbb;
  background-color: #fff;
  border-radius: 13px;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
}
.cart-item-left {
  width: 23%;
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
  padding-left: 25px;
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
  width: 37%;
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
  height: 58%;
  display: flex;
  align-items: flex-end;
}
.cartpage-center-right {
  width: 35%;
  height: 100%;
  margin-left: 60px;
}
.right-center {
  width: 100%;
  height: 185px;
  background-color: #fff;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
  border-radius: 13px;
  padding: 20px;
}
.right-center-item {
  width: 100%;
  height: 100%;
}
.right-center-item-top {
  height: 40%;
  width: 100%;
  border-bottom: 1px solid #bbbb;
}
.right-item-top-total,
.right-item-top-discount {
  width: 100%;
  height: 50%;
  display: flex;
}
.total-left,
.discount-left {
  width: 60%;
  display: flex;
  font-size: 15px;
}
.total-right,
.discount-right {
  width: 40%;
  text-align: flex-end;
  font-size: 15px;
}
.right-item-top-discount {
  color: #777;
}
.right-center-item-center {
  height: 67%;
  width: 100%;
}
.right-item-center-totalmoney {
  width: 100%;
  height: 50%;
  display: flex;
  align-items: center;
}
.total-left.b,
.total-right.b {
  font-size: 18px;
  font-weight: bold;
}
.right-item-center-btn {
  width: 85%;
  height: 45%;
  position: relative;
  display: flex;
  align-items: flex-end;
}
.right-bottom {
  width: 100%;
  height: 100px;
  background-color: #fff;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
  border-radius: 13px;
  padding: 20px 20px;
  margin-top: 30px;
}
.right-bottom-label {
  width: 100%;
  display: flex;
}
.right-bottom-discount {
  width: 100%;
  display: flex;
}
.right-bottom-input {
  width: 65%;
}
.right-bottom-btn {
  width: 30%;
  margin-left: 14px;
  position: relative;
  margin-top: 5px;
}
.discount-btn {
  right: 0;
}
.input-bottom {
  width: 90%;
  height: 73%;
}

.container-left-top {
  width: 100%;
  height: 250px;
  margin-bottom: 5vh;
  margin-top: 30px;
}
.left-top-header {
  width: 100%;
  height: 45px;
  display: flex;
  margin-bottom: 1vh;
}
.header-avatar {
  width: 70%;
  display: flex;
  align-items: center;
  font-size: 20px;
}
.avatar-image {
  margin-right: 10px;
}
.header-change-info {
  width: 30%;
  height: 100%;
  font-size: 16px;
  display: flex;
  justify-content: right;
  align-items: center;
}
.label-change-info {
  text-decoration-line: underline;
}
.left-top-body,
.left-center-body,
.left-bottom-body {
  width: 100%;
  height: 200px;
  display: flex;
  flex-wrap: wrap;
  border-radius: 15px;
  background: #fff;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
  padding: 20px;
}
.left-top-element {
  width: 50%;
  height: 50px;
  text-align: left;
  padding: 2% 3%;
}
.left-top-label {
  font-size: 18px;
  color: rgb(222, 2, 2);
}
.container-left-bottom {
  width: 100%;
  min-height: 400px;
  margin-bottom: 3vh;
}
.left-bottom-payment {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  border-radius: 15px;
  background: #fff;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 5px 15px;
  padding: 20px;
}
.payment-header {
  width: 100%;
  height: 15%;
}
.vnpay {
  width: 100%;
  height: 100%;
  display: flex;
}
.vnpay-icon {
  width: 130px;
  height: 45px;
}
.payment-content {
  width: 100%;
  height: 80%;
  padding: 24px;
  background-color: #f5f7f9;
  color: #21262c;
}
.payment-content-header {
  margin-bottom: 25px;
  font-size: 22px;
  line-height: 1.4;
  font-weight: 500;
}
.payment-content-method {
  width: 100%;
  height: 80px;
  padding: 12px 16px;
  display: flex;
  cursor: pointer;
  width: 100%;
  text-align: left;
  border: 0;
  outline: 0;
  background-color: #fff;
  margin-bottom: 20px;
}
.payment-content-method:hover {
  box-shadow: 0 4px 8px rgba(33, 38, 44, 0.16);
}
.method-label {
  width: 85%;
  height: 100%;
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 16px;
  line-height: 1.5;
  font-weight: 500;
  color: #21262c;
}
.method-icon {
  background-image: var(--icon);
  background-position: -1096px -751px;
  background-repeat: no-repeat;
  width: 60px;
  height: 60px;
}
.method-icon.b {
  background-position: -978px -751px;
}
.method-icon.c {
  background-position: -979px -833px;
}
.payment-bank {
  width: 100%;
  min-height: 200px;
  padding: 24px;
  display: flex;
  flex-wrap: wrap;
  background-color: #fff;
}
.payment-bank:hover {
  box-shadow: 0 4px 8px rgba(33, 38, 44, 0.16);
}
.payment-bank-item {
  width: 23%;
  padding-left: 4px;
  padding-right: 4px;
  margin-bottom: 8px;
  margin-left: 8px;
  border: 1px solid #bfccd9;
  border-radius: 3px;
  transition: 0.15s all linear;
}
.bank-logo {
  width: 100%;
  height: 100%;
}
.payment-bank-item:hover {
  border: 1px solid #3688da;
}
.payment-bank-item.vietcom {
  background-position: -130px -1682px;
}
.payment-bank-item.viettin {
  background-position: -20px -1682px;
}
.payment-bank-item.hdbank {
  background-position: -235px -1682px;
}
.payment-bank-item.mb {
  background-position: -342px -1682px;
}
.payment-bank-item.vpbank {
  background-position: -453px -1682px;
}
@media (max-width: 1200px) {
  .cartpage {
    padding: 10% 0;
  }
  .cartpage-container {
    padding: 3% 3%;
  }
  .cartpage-top-label {
    font-size: 23px;
    padding: 0 50px;
    height: 80px;
  }
  .cartpage-center {
    display: block;
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
    padding: 20px 10px;
  }
  .cart-item-left {
    display: none;
  }
  .cart-item-center {
    width: 75%;
    padding: 0;
  }
  .cart-item-center-input {
    width: 39%;
  }
  .cart-item-price {
    height: 58%;
  }
  .cart-item-center-top {
    padding: 0 7px;
  }
  .cart-item-label {
    height: 28px;
    overflow: hidden;
    text-align: start;
  }
  .left-top {
    font-size: 20px;
  }
  .cartpage-center-right {
    margin-left: 0;
  }
  .left-bottom-payment {
    padding: 0;
  }
  .payment-content {
    border-radius: 0 0 15px 15px;
    padding: 15px;
  }
  .payment-bank-item {
    width: 30%;
  }
  .payment-bank {
    padding: 24px 15px;
  }
}
</style>
