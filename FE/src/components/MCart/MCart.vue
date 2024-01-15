<template>
  <div class="cart" v-if="CartItem.length > 0">
    <div class="cart-top">
      <label class="cart-top-label">Đăng ký của bạn</label>
    </div>
    <div class="cart-mid">
      <div class="cart-item" v-for="index in CartItem" :key="index">
        <div class="cart-item-right">
          <div class="cart-item-label">
            <label>{{ index.Course.title }}</label>
          </div>
          <div class="cart-item-prop">
            <div class="cart-item-price">
              <label>{{ FormatVND(index.Course.price) }}</label>
            </div>
            <div class="cart-item-month">
              <label>{{ index.Course.rangeProcess }} tháng</label>
            </div> 
            <div class="cart-item-delete" @click="deleteOrder(index.Order.id)">
              <MTooltip :content="'Click để xóa'" />
              <i class="ti ti-trash"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="cart-bottom">
      <router-link class="cart-content-bottom-right" to="/cartpage">
        <div>
          <MButton
            :tab="6"
            :ButtonCss="'btn-button-save d'"
            :text="'Xem chi tiết'"
          />
        </div>
      </router-link>
    </div>
  </div>
</template>
<script>
import MButton from "@/components/MButton/MButton.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
export default {
  props: {},
  components: {
    MButton,
    MTooltip,
  },
  created() {
    this.loadCart();
  },
  methods: {

    async loadCart() {
      try {
        fetch(`${apiConfig}/prepare-orders`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.Order = data.content;
            this.getOrderDetail(data.content);
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
    async getOrderDetail(order) {
      try {
        order.forEach((i) => {
          this.ptCourse = i.ptCourse;
          this.CartItem.push({Course: this.ptCourse.course, Order: i});
          
        });
      } catch (error) {
       console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async deleteOrder(id){
      try {
        fetch(`${apiConfig}/prepare-orders/`+ id, await fetchOptions("DELETE"))
          .then((data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
            } else {
              this.ShowToast((this.ToastStatus = 1));
              this.CartItem = [];
              this.loadCart();
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
  },
  data() {
    return {
      CartItem: [],
      ptCourse: null,
      Order: null,
    };
  },
};
</script>
<style scoped>
.cart {
  width: 300px;
  min-height: 250px;
  position: absolute;
  border: 1px solid #bbbb;
  right: 5%;
  top: 8%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
  z-index: 1000;
}
.cart-top {
  width: 100%;
  height: 35px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #bbbb;
}
.cart-top-label {
  width: 100%;
  font-size: 18px;
}
.cart-mid {
  min-height: 150px;
  max-height: 200px;
  overflow-y: scroll;
  overflow-x: hidden;
}
.cart-item {
  width: 100%;
  height: 90px;
  display: flex;
  padding: 15px 10px;
}
.cart-item-left {
  width: 25%;
  height: 100%;
}
.cart-item-left-img {
  width: 60px;
  height: 60px;
}
.cart-img {
  width: 100%;
  height: 100%;
}
.cart-item-right {
  width: 95%;
  height: 100%;
  position: relative;
}
.cart-item-label {
  width:90%;
  height: 30px;
  font-size: 17px;
  font-weight: bold;
  overflow: hidden;
}
.cart-item-prop {
  width: 100%;
  display: flex;
}
.cart-item-price {
  width: 50%;
  height: 100%;
  color: rgb(9, 130, 243);
}
.cart-item-delete {
  font-size: 20px;
  position: absolute;
  right: -5%;
  top: 18%;
  cursor: pointer;
}
.cart-item-month {
  width: 50%;
  height: 100%;
  color: #777;
}
.cart-bottom {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.cart-content-bottom-right {
  width: 100%;
  height: 60%;
}
</style>