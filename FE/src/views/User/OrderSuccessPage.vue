<template>
  <div class="success-container">
    <div class="success-body">
      <div class="success-border">
      <div class="body-top">
        <div class="top-header" v-if="!isFail">
          <h1>Thank You!</h1>
        </div>
        <div class="top-header false" v-if="isFail">
          <h1>Thanh toán thất bại!</h1>
        </div>
        <div class="top-label">
          <label for="" v-if="!isFail">Đơn hàng của bạn đã được {{isPayment}} thành công.</label>
          <label for="" v-if="isFail">Đơn hàng của bạn đã bị hủy. Do thanh toán không thành công.</label>
        </div>
      </div>
      <div class="body-bottom">
        <div class="bottom-left" v-if="!isFail">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="bottom-left-icon"
            width="120"
            height="120"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path
              d="M12.01 2.011a3.2 3.2 0 0 1 2.113 .797l.154 .145l.698 .698a1.2 1.2 0 0 0 .71 .341l.135 .008h1a3.2 3.2 0 0 1 3.195 3.018l.005 .182v1c0 .27 .092 .533 .258 .743l.09 .1l.697 .698a3.2 3.2 0 0 1 .147 4.382l-.145 .154l-.698 .698a1.2 1.2 0 0 0 -.341 .71l-.008 .135v1a3.2 3.2 0 0 1 -3.018 3.195l-.182 .005h-1a1.2 1.2 0 0 0 -.743 .258l-.1 .09l-.698 .697a3.2 3.2 0 0 1 -4.382 .147l-.154 -.145l-.698 -.698a1.2 1.2 0 0 0 -.71 -.341l-.135 -.008h-1a3.2 3.2 0 0 1 -3.195 -3.018l-.005 -.182v-1a1.2 1.2 0 0 0 -.258 -.743l-.09 -.1l-.697 -.698a3.2 3.2 0 0 1 -.147 -4.382l.145 -.154l.698 -.698a1.2 1.2 0 0 0 .341 -.71l.008 -.135v-1l.005 -.182a3.2 3.2 0 0 1 3.013 -3.013l.182 -.005h1a1.2 1.2 0 0 0 .743 -.258l.1 -.09l.698 -.697a3.2 3.2 0 0 1 2.269 -.944zm3.697 7.282a1 1 0 0 0 -1.414 0l-3.293 3.292l-1.293 -1.292l-.094 -.083a1 1 0 0 0 -1.32 1.497l2 2l.094 .083a1 1 0 0 0 1.32 -.083l4 -4l.083 -.094a1 1 0 0 0 -.083 -1.32z"
              stroke-width="0"
              fill="currentColor"
            />
          </svg>
        </div>
        <div class="bottom-left false" v-if="isFail">
          <i class="ti ti-circle-x-filled"></i>
        </div>
        <div class="bottom-right" v-if="!isFail">
          <div class="bottom-right-label">
            <label for=""
              >Cảm ơn bạn đã đăng ký khóa học của Gym Management System. Khóa học đã được đăng ký, hệ thống sẽ xét duyệt đăng ký của bạn trong vòng vài ngày.
            </label>
            <label for=""
              >Bạn có thể truy cập trang Quản lý lịch sử đăng ký khóa học của tôi bất kỳ lúc nào để kiểm tra trạng thái đơn đăng ký khóa học của bạn. </label
            >
          </div>

          <MButton
            class="bottom-right-button"
            :class="'content-top-btn-content'"
            :text="'Quay lại danh sách khóa học'"
            @click="backToCourseList"
          />
        </div>
        <div class="bottom-right" v-if="isFail">
          <div class="bottom-right-label false">
            <label for=""
              >Thanh toán thất bại, bạn vui lòng kiểm tra lại tài khoản ngân hàng của mình.
            </label>
            <label for=""
              >Bạn có thể truy cập trang Danh sách của tôi để thanh toán lại</label
            >
          </div>

          <MButton
            class="bottom-right-button"
            :class="'content-top-btn-false'"
            :text="'Quay lại danh sách khóa học'"
            @click="backToCartList"
          />
        </div>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from 'vuex';
import {
  SET_ORDERSTATUS,
} from "@/stores/modules/storeconstants";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: {
    MButton,
  },
  props: ['transactionId', 'orderSuccess'],
  created() {
    console.log(this.transactionId);
    console.log(this.orderSuccess);

    if(this.orderSuccess == true){
      this.isPayment = "thanh toán";
      this.orderSuccessPageInitialized(true);
      this.loadPrepare();
    }else{
      this.isFail = true;
    }
  },
  methods: {
    ...mapMutations({displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
      orderSuccessPageInitialized: SET_ORDERSTATUS
    }),

    backToCourseList(){
      this.$router.push('/courses');
    },
    backToCartList(){
      this.$router.push('/cartpage');
    },

    async loadPrepare() {
      try {
        fetch(`${apiConfig}/prepare-orders`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.PreOrder = data.content;
            this.PreOrder.forEach(i => {
              this.deletePreOrder(i.id);
            });
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

    async deletePreOrder(id){
      try {
        fetch(
          `${apiConfig}/prepare-orders/` + id,
          await fetchOptions("DELETE")
        )
          .then((data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
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
  data() {
    return {
      isPayment: "đặt hàng",
      isFail: false,
      PreOrder: null,
    }
  }
};
</script>

<style scoped>
.success-container {
  background-color: #f2f4f5;
  height: 80vh;
  width: 100%;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
}
.success-body {
  width: 100%;
  height: 100%;
  margin-bottom: 13%;
  margin-top: 2%;
  padding: 3% 18%;
}
.body-top {
  width: 100%;
  height: 25%;
}
.top-header {
  width: 100%;
  height: 60%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.top-header h1 {
  font-weight: bold;
  font-size: 45px;
  color: #089a0e;
}

.top-header.false h1 {
  color: #e9485a;
}
.top-label {
  width: 100%;
  height: 40%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.top-label label {
  font-size: 20px;
}
.body-bottom {
  height: 35%;
  width: 100%;
  margin: 0 auto;
  display: flex;
  padding: 30px 20px;
}
.bottom-left {
height: 100%;
    width: 29%;
    font-size: 130px;
    display: flex;
    align-items: center;
    padding-left: 13%;
}
.bottom-left-icon {
  color: #089a0e;
}
.bottom-left.false {
  color: #e9485a;
}
.bottom-right {
  height: 100%;
  width: 60%;
  display: flex;
  flex-wrap: wrap;
}
.bottom-right-label {
  width: 100%;
  text-align: left;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}
.bottom-right-label.false{
  height: 30%;
}
.bottom-right-label label {
  width: 100%;
  margin-bottom: 20px;
}
.success-border{
  width: 100%;
  height: 100%;
   background-color: #fff;
   border-radius: 10px;
}
.bottom-right-button {
  height: 40%;
  display: flex;
  justify-content: left;
  align-self: flex-end;
}
@media (max-width: 1200px) {
  .body-bottom{
    width: 100%;
    padding: 30px 0;
    min-height: 38%;
  }
  .bottom-left{
    width: 30%;
    padding: 0;
  }
  .bottom-right{
    width: 65%;
  }
  .top-label label{
    font-size: 18px;
  }
  .bottom-right-label{
    text-align: justify;
  }
  .top-header.false h1{
    font-size: 30px;
  }.bottom-left.false{
    font-size: 110px;
  }.success-body{
    padding: 3% 13px;
  }.top-label{
    padding: 0 46px;
  }
}
</style>
