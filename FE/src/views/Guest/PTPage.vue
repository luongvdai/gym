<template>
  <div class="pt-container">
    <div class="ptlist">
      <div class="pt-header">
        <label>
          Danh Sách Huấn Luyện Viên Tại
          <router-link class="branchname text-decoration-none" to="/">{{
            getBranch.name
          }}</router-link>
        </label>
      </div>
      <div class="pt-header-bottom my-2">
        <span class="line"></span>
      </div>
      <div class="ptlist-content">
        <div class="d-flex flex-wrap" v-if="showLoading === true">
          <v-skeleton-loader
            min-height="300"
            width="300"
            type="card-avatar, article"
            v-for="i in 6"
            :key="i"
          ></v-skeleton-loader>
        </div>
        <div class="d-flex flex-wrap" v-else-if="showLoading === false">
          <div class="pt-avatar" v-for="pt in PTList" :key="pt.id">
            <router-link style="text-decoration: none" :to="`/ptpage/${pt.id}`">
              <MTooltip :content="'Click để xem'" />
              <img class="avatar" :src="pt.avatar" v-if="pt.avatar" />
              <img class="avatar" src="@\assets\photos\pt1.jpg" v-else />
              <div class="pt-name">
                <label>{{ pt.fullName }}</label>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </div>
    <div class="paging" v-if="!showLoading">
      <v-pagination
        :length="TotalCount"
        v-model="pagein"
        prev-icon="ti ti-chevron-left"
        next-icon="ti ti-chevron-right"
        :total-visible="7"
      ></v-pagination>
    </div>
    <div class="banner">
      <router-link :to="`/login`">
        <img class="banner-img" src="@\assets\photos\bannerPT.jpg" />
      </router-link>
    </div>
  </div>
</template>

<script>
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import fireStorage from "@/plugins/fireStorage";
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
  },
  props: {},
  created() {
    (async () => {
      await this.loadPT();
    })();
  },
  data() {
    return {
      PTList: [],
      showLoading: false,
      TotalCount: 1,
      pagein: 1,
    };
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    async loadPT() {
      this.showLoading = true;
      try {
        await fetch(
          `${apiConfig}/pt-courses/mentor?branchId=${
            this.getBranch.id
          }&size=6&page=${this.pagein - 1}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.PTList = data.content;
            this.TotalCount = data.totalPages;
            await this.PTList.map(async (pt) => {
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
      }finally{
        this.showLoading = false;
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
    async pagein() {
      await this.loadPT();
    },
    getBranch() {
      this.loadPT();
    },
  },
};
</script>

<style scoped>
.pt-container {
  display: block;
  min-height: 100vh;
  background-color: #222222;
  padding: 2% 10%;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}
.ptlist {
  width: 100%;
  min-height: 100vh;
}
.pt-header {
  width: 100%;
  height: fit-content;
  color: #fff;
  margin: 5px auto;
  font-size: 28px;
  font-weight: 500;
}

.branchname {
  font-size: 28px !important;
  font-weight: 500;
  text-decoration: none;
}
.pt-header-bottom {
  width: 100%;
  height: 5%;
  padding: 0 45%;
}
.line {
  width: 100%;
  height: 4px;
  background-color: #e9485a;
  display: block;
}
.ptlist-content {
  width: 100%;
  min-height: 100vh;
}
.pt-avatar {
  width: 300px;
  height: 400px;
  cursor: pointer;
  position: relative;
  background-color: #ccced1;
  border-radius: 5px;
}
.d-flex {
  gap: 100px 65px;
}
.avatar {
  width: 100%;
  height: 100%;
  transition: 0.5s ease;
  backface-visibility: hidden;
  opacity: 1;
  display: block;
  border-radius: 5px;
}
.pt-name {
  margin-top: 20px;
  font-size: 20px;
  color: #fff;
}

.pt-avatar:hover .avatar {
  opacity: 0.3;
}

.paging {
  width: 100%;
  height: 100px;
  padding: 20px 0;
  color: #fff;
  margin-top: 20px;
}
.banner {
  width: 100%;
  height: 150px;
}
.banner-img {
  width: 100%;
  height: 100%;
  cursor: pointer;
}
.text-decoration-none {
  font-size: larger;
}
.text-decoration-none:hover {
  color: #e9485a;
  text-decoration: underline !important;
}
@media (max-width: 1200px) {
  .pt-container {
    padding: 2% 0;
  }
  .pt-header {
    width: 99%;
    font-size: 26px;
  }
  .pt-header a {
    font-size: 26px !important;
  }
  .banner {
    height: 100%;
  }
}
@media (min-width: 1600px) and (max-width: 1700px) {
  .ptlist{
    padding: 2% 10%;
  }
}
</style>
