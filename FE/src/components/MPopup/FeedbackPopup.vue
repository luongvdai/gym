<template>
  <div class="feedback-cover">
    <div class="feedback-popup">
      <div class="popup-top">
        <div class="top-header">
          <label class="label-header">Chia sẻ đánh giá của bạn</label>
        </div>
        <button class="close-icon" @click="closeFeedbackPopup">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="delete-icon"
            width="25"
            height="25"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path d="M18 6l-12 12" />
            <path d="M6 6l12 12" />
          </svg>
        </button>
      </div>
      <div class="popup-center">
        <div class="center-rate">
          <label class="rate-label"
            >Bạn đánh giá như thế nào về huấn luyện viên của chúng tôi?</label
          >
          <v-rating
            v-model="feedbackValue"
            hover
            color="amber"
            density="compact"
            full-icon="ti ti-star-filled"
            empty-icon="ti ti-star"
            half-icon="ti ti-star-half-filled"
            half-increments
            size="40"
            class="rate-star"
          ></v-rating>
        </div>
        <div class="center-content">
          <v-textarea
            v-model="feedbackContent"
            :rules="[rules.required, rules.maxLength]" 
            :counter="20"
            hint="Nhập tối đa 20 ký tự"
            label="Feedback"
            variant="outlined"
          ></v-textarea>
        </div>
      </div>
      <div class="popup-bottom">
        <MButton
          :text="'Gửi phản hồi'"
          class="button-sendfeedback"
          :class="'content-top-btn-content'"
          @click="FeedBack"
        />
      </div>
    </div>
  </div>
</template>

<script>
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import MButton from "@/components/MButton/MButton.vue";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: { MButton },
  props: {
    userCourseId: Object,
  },
  watch: {
    // feedbackContent(newVal) {
    //   if (newVal.length > 20) {
    //     // Trim the input to the maximum allowed length
    //     this.feedbackContent = newVal.slice(0, 20);
    //   }
    // },
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    closeFeedbackPopup() {
      this.$emit("close-popup-feedback");
    },

    async FeedBack() {
      if (this.feedbackContent.length <= 20 && this.feedbackContent) {
        try {
          fetch(
            `${apiConfig}/feedbacks`,
            await fetchOptions("POST", {
              star: this.feedbackValue,
              userCourseId: this.userCourseId,
              content: this.feedbackContent,
            })
          )
            .then((res) => res.json().catch(() => res))
            .then((res) => {
              if (!res.id) {
                this.ShowToast(3, res.message);
              } else {
                this.closeFeedbackPopup();
                this.$emit("load-data");
                this.ShowToast(1, "Đánh giá thành công");
              }
            })
            .catch((res) => {
              console.log(res.message);
              this.ShowToast(3, "Đánh giá thất bại");
            });
        } catch (error) {
          console.log(error);
          this.ShowToast(3, "Lỗi hệ thống");
        }
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
  },
  data() {
    return {
      feedbackValue: 1,
      feedbackContent: null,
      rules: {
        required: value => !!value || 'Bạn không được để trống',
        maxLength: value => (value && value.length <= 20) || 'Nhập tối đa 20 ký tự',
      },
    };
  },
};
</script>

<style scoped>
.feedback-cover {
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
.feedback-popup {
  width: 600px;
  height: 450px;
  position: relative;
  top: 15%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px 50px;
}
.popup-top {
  height: 15%;
  width: 100%;
  display: flex;
}
.top-header {
  height: 100%;
  width: 95%;
  text-align: left;
}
.label-header {
  font-weight: bold;
  font-size: 25px;
  color: #e91e63;
}
.close-icon {
  width: 5%;
  height: 50%;
  display: flex;
  justify-content: right;
}
.delete-icon {
  color: #6b6b6b;
}
.popup-center {
  width: 100%;
  height: 70%;
}
.center-rate {
  width: 100%;
  height: 25%;
  display: flex;
  flex-wrap: wrap;
}
.rate-label {
  width: 100%;
  text-align: left;
  font-weight: normal;
  font-size: 18px;
  height: 50%;
}
.rate-star {
  margin-left: 120px;
  height: 50%;
}
.center-content {
  width: 100%;
  height: 75%;
  padding-top: 5%;
}
.popup-bottom {
  width: 100%;
  height: 15%;
  display: flex;
  justify-content: center;
}
</style>
