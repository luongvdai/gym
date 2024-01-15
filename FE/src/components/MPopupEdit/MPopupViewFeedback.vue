<template>
  <div class="cover-popup">
    <div class="feedback-container">
      <div class="feedback-header">
        <div class="feedback-header-right">
          <label>Đánh giá PT: {{PT.username}}</label>
        </div>
        <div class="feedback-header-left">
          <button type="button" class="btn-close" @click="closeViewFeedback">
            <i class="ti ti-x"></i>
          </button>
        </div>
      </div>
      <div class="feedback-content">
        <v-card-item>
          <div class="info">
            <img v-if="!urlImage" class="avatar" src="../../assets/photos/avatar.png" alt="" />
            <img v-else class="avatar" :src="urlImage" alt="" />
            <div class="text-start">
              <v-card-title class="name"> {{User.fullName}} </v-card-title>
              <v-card-subtitle class="coursename"
                ><v-rating
                  v-model="Feed.star"
                  color="amber"
                  density="compact"
                  full-icon="ti ti-star-filled"
                  empty-icon="ti ti-star"
                  half-icon="ti ti-star-half-filled"
                  half-increments
                  size="small"
                ></v-rating
              ></v-card-subtitle>
            </div>
          </div>
        </v-card-item>
        <div class="center-content">
          <v-textarea v-model="Feed.content" :rules="[rules.required, rules.maxLength]" hint="Nhập tối đa 20 ký tự"  rows="3"  :counter="20"  label="Feedback" variant="outlined"></v-textarea>
        </div>
      </div>
      <div class="feedback-bottom">
        <div class="popupedit-content-bottom-left">
          <div @click="deleteFeedback">
            <MButton :tab="7" :ButtonCss="'btn-button-cancel'" :text="'Xóa'" />
          </div>
        </div>
        <div class="popupedit-content-bottom-right">
          <div class="btn-product-popup-save1" @click="editFeedback">
            <MButton
              :tab="5"
              :ButtonCss="'content-top-btn-content'"
              :text="'Sửa'"
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
import fireStorage from "@/plugins/fireStorage";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "MPopupViewFeedback",
  components: {
    MButton,
  },
  props: {
    userCourseId: String,
  },
  data() {
    return {
        Feed: {},
        User: {},
        PT: {},
        urlImage: null,
        rules: {
        required: value => !!value || 'Bạn không được để trống',
        maxLength: value => (value && value.length <= 20) || 'Nhập tối đa 20 ký tự',
      },
    };
  },
  created() {
    if(this.userCourseId){
        this.loadFeedback(this.userCourseId);
    }
  },
  methods:{
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closeViewFeedback(){
        this.$emit("close-feedback-view");
    },
    async loadFeedback(id){
      try {
        fetch(`${apiConfig}/feedbacks/${id}`, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.Feed = data;
          this.User = data.sender;
          (async () => {
              this.urlImage = await fireStorage.displayImage(this.User.avatar);
            })();
          this.PT = data.userCourse.ptCourse.user;
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

    async editFeedback(){
        if(this.Feed.content.length <= 20){
          try {
          fetch(
            `${apiConfig}/feedbacks/${this.Feed.id}`,
            await fetchOptions("PUT", {
                content: this.Feed.content,
                star: this.Feed.star,
                userCourseId: this.userCourseId,
            })
          )
            .then((res) => res.json().catch(() => res))
            .then((res) => {
              if (!res.id) {
                this.ShowToast(3, res.message);
              } else {
                this.closeViewFeedback();
                this.ShowToast(1, "Sửa thành công");
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
        }
    },

    async deleteFeedback() {
      try {
          fetch(`${apiConfig}/feedbacks/` + this.Feed.id, await fetchOptions("DELETE"))
            .then((data) => {
              if (data.status != "200") {
                this.ShowToast(3, data.message);
              } else {
                this.ShowToast(1, "Xóa thành công");
                this.closeViewFeedback();
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
  beforeRouteLeave(to, from, next) {
    // Destroy trang cũ
    this.destroy();
    next();
  },
  destroy() {
    // Xóa tất cả dữ liệu
    this.Feed = {};
    this.User = {};
    this.PT = {};
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
.feedback-container {
  width: 450px;
  height: 315px;
  background-color: #fff;
  position: relative;
  top: 40%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
  padding: 0 20px;
}
.feedback-header {
  width: 100%;
  height: 20%;
  display: flex;
}
.feedback-header-right {
  width: 70%;
  height: 100%;
  display: flex;
  align-items: center;
}
.feedback-header-left {
  width: 30%;
  height: 100%;
  font-size: 25px;
  display: block;
  text-align: end;
  padding: 8px;
}
.feedback-header-left.btn-close {
  padding: 0.5rem 0.5rem;
  margin-top: -0.5rem;
  margin-right: -0.5rem;
  margin-bottom: -0.5rem;
}
.feedback-header-right label {
  font-size: 20px;
}
.feedback-content{
    width: 100%;
    height: 60%;
}.v-card-item{
    padding: 0;
}
.info {
  display: flex !important;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.avatar {
  width: 50px;
  height: 50px;
}
.text-start {
  width: 100%;
}
.name {
  font-size: 16px !important;
}
.coursename {
  font-size: 13px !important;
}
.content {
  display: flex;
}
.center-content{
  width: 100%;
  height: 60%;
  padding-top: 5%;
}
.feedback-bottom{
    width: 100%;
    height: 20%;
    position: relative;
}.popupedit-content-bottom-left {
    position: absolute;
    left: 0;
    width: 30%;
    height: 100%;
    display: flex;
    align-items: center;
}
.popupedit-content-bottom-right {
    position: absolute;
    right: -100px;
    width: 50%;
    height: 100%;
    display: flex;
    align-items: center;
}

</style>