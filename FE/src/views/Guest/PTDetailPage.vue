<template>
  <div class="ptdetail-container">
    <div class="ptdetail-sidebar">
      <div class="sidebar-header">
        <label>HUẤN LUYỆN VIÊN KHÁC</label>
      </div>
      <div class="sidebar-content">
        <div class="list-pt" v-if="showLoading">
          <v-skeleton-loader
            min-width="320"
            type="image, article"
            v-for="i in 3"
            :key="i"
          ></v-skeleton-loader>
        </div>
        <div
          class="sidebar-item"
          v-for="pt in PTList"
          :key="pt.id"
          @click="getOne(pt.id)"
        >
          <MTooltip :content="'Click để xem'" />
          <div class="sidebar-avatar">
            <img class="avatar" :src="pt.avatar" v-if="pt.avatar" />
            <img
              class="avatar"
              src="@\assets\photos\pt1.jpg"
              v-if="!pt.avatar"
            />
          </div>
          <div class="sidebar-name">
            <label>{{ pt.fullName }}</label>
          </div>
        </div>
      </div>
    </div>
    <div class="ptdetail-content">
      <div class="ptdetail-content-top">
        <div class="ptdetail-content-left" v-if="showLoading">
          <v-skeleton-loader
            min-width="320"
            type="image, article"
          ></v-skeleton-loader>
        </div>
        <div class="ptdetail-content-left" v-else>
          <img
            class="avatar"
            :src="ptSelected.avatar"
            v-if="ptSelected.avatar"
          />
          <img
            class="avatar"
            src="@\assets\photos\pt1.jpg"
            v-if="!ptSelected.avatar"
          />
        </div>
        <div class="ptdetail-content-right">
          <div class="content-right-header">
            <label class="">{{ ptSelected.fullName }}</label>
          </div>
          <div class="content-right-gender">
            <label class=""
              >Giới tính: {{ formatGenderRecord(ptSelected.gender) }}</label
            >
          </div>
          <div class="content-right-gender">
            <label class=""
              >Tuổi: {{ formatAge(ptSelected.dateOfBirth) }}</label
            >
          </div>
          <div class="content-right-star" v-if="ptSelected.statsFeedback">
            <label class="">Số sao trung bình: </label>
            <v-rating
              v-model="ptSelected.statsFeedback.average"
              color="amber"
              density="compact"
              full-icon="ti ti-star-filled"
              empty-icon="ti ti-star"
              half-icon="ti ti-star-half-filled"
              half-increments
              readonly
              size="small"
            ></v-rating>
            <label class="">( {{ ptSelected.statsFeedback.total }}) </label>
          </div>
          <div class="content-right-description">
            <label class="">{{ ptSelected.description }}</label>
          </div>
        </div>
      </div>
      <div class="ptdetail-content-feedback">
        <v-slide-group v-model="model" selected-class="bg-success" show-arrows>
          <v-slide-group-item v-for="n in FeedbackList" :key="n">
            <CardFeedbackVue
              :feedback="n"
              :class="['ma-4', selectedClass]"
            ></CardFeedbackVue>
          </v-slide-group-item>
        </v-slide-group>
      </div>
    </div>
  </div>
</template>

<script>
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
import CardFeedbackVue from "@/components/MCard/CardFeedback.vue";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "PTPage",
  components: {
    MTooltip,
    CardFeedbackVue,
  },
  props: {},

  created() {
    this.loadPT();
    this.getOne(this.$route.params.id);
  },
  data() {
    return {
      PTList: [],
      ptSelected: {},
      showLoading: false,
      FeedbackList: [],
    };
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    /**
     * hàm format giới tính
     */
    formatGenderRecord(gender) {
      if (gender == true) {
        return "Nam";
      } else return "Nữ";
    },

    formatAge(unixTimestamp) {
      try {
        let localTime = new Date(unixTimestamp * 1000);
        const year = localTime.getFullYear();
        var age = Math.abs(year - 1970);

        return age; // Trả về ngày và giờ đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
      }
    },

    async loadPT() {
      this.showLoading = true;
      try {
        fetch(
          `${apiConfig}/pt-courses-user/mentor?branchId=${this.getBranch.id}&size=3`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.PTList = data.content;
            this.PTList.map(async (pt) => {
              pt.avatar = await fireStorage.displayImage(pt.avatar);
            });
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      } finally {
        this.showLoading = false;
      }
    },

    async getOne(id) {
      this.ptSelected = {};
      this.showLoading = true;
      try {
        fetch(`${apiConfig}/users/mentor/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if(!data.id){
              this.ShowToast(3, data.message);
            }else{
              this.ptSelected = data;
            (async () => {
              this.ptSelected.avatar = await fireStorage.displayImage(
                this.ptSelected.avatar
              );
            })();
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
        this.loadFeeback(id);
        this.showLoading = false;
      }
    },

    async loadFeeback(id) {
      try {
        fetch(
          `${apiConfig}/feedbacks/by-pt?ptId=${id}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.FeedbackList = data.content;
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading = false;
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
    getBranch() {
      this.loadPT();
    },
  },
};
</script>

<style scoped>
.ptdetail-container {
  display: block;
  min-height: 100vh;
  background-color: #222222;
  padding: 5% 6%;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}
.ptdetail-sidebar {
  float: left;
  width: 345px;
  min-height: 100vh;
  padding: 15px;
  background-color: #4b4a4a;
  border-radius: 11px;
}
.ptdetail-content {
  float: right;
  width: calc(100% - 309px - 75px);
  min-height: 100vh;
  padding-left: 30px;
}

.ptdetail-content-top {
  width: 100%;
  height: 80%;
  margin-bottom: 40px;
  display: flex;
  padding: 3%;
  border-radius: 10px;
  background-color: #4b4a4a;
}
.sidebar-header {
  font-size: 25px;
  color: #fff;
  text-transform: uppercase;
  line-height: 30px;
  margin: 0 0 30px 0;
  position: relative;
  padding: 0 0 25px 0;
}
.sidebar-content {
  width: 100%;
  height: 80%;
}
.sidebar-item {
  overflow: hidden;
  clear: both;
  margin-bottom: 30px;
  display: flex;
  cursor: pointer;
  position: relative;
}
.sidebar-avatar {
  width: 50%;
  height: 80%;
  float: left;
  transition: 0.5s ease;
  backface-visibility: hidden;
  opacity: 1;
}
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 13px;
}
.sidebar-item:hover .avatar {
  opacity: 0.3;
}
.sidebar-name {
  float: right;
  width: calc(100% - 135px - 20px);
  color: #fff;
  font-size: 20px;
}
.ptdetail-content-left {
  width: 100%;
  height: 100%;
  float: left;
}
.ptdetail-content-right {
  padding-left: 15px;
  padding-right: 15px;
}
.content-right-header {
  font-size: 48px;
  color: #fff;
}
.content-right-description {
  color: #fff;
  padding: 20px 30px;
  height: 200px;
  width: 600px;
  text-align: justify;
  font-size: 17px;
}
.ptdetail-content-feedback {
  width: 100%;
  height: 20%;
  border-radius: 10px;
  background-color: #4b4a4a;
}
.content-right-gender {
  color: #fff;
  padding: 20px 30px;
  text-align: left;
  font-size: 17px;
}
.content-right-star {
  color: #fff;
  padding: 20px 30px;
  text-align: left;
  display: flex;
  align-items: center;
  font-size: 17px;
}
.content-right-star label {
  margin-right: 10px;
}
.mx-auto {
  margin-right: 20px !important;
}
@media (max-width: 1200px) {
  .ptdetail-container {
    display: grid;
    padding: 5% 9%;
  }
  .ptdetail-sidebar {
    order: 2;
    background-color: #222222;
  }
  .ptdetail-content {
    width: 100%;
    padding: 0;
  }
  .ptdetail-content-top {
    height: 76%;
    padding: 0;
    display: block;
    background-color: #222222;
  }
  .ptdetail-content-left {
    height: 56%;
  }
  .ptdetail-content-right {
    padding: 0;
    text-align: start;
  }
  .content-right-gender {
    padding: 20px 0;
  }
  .content-right-description {
    padding: 20px 0;
    width: 100%;
  }
  .ptdetail-content-feedback {
    background-color: #222222;
  }
  .content-right-star {
    padding: 20px 0;
  }
}
</style>
