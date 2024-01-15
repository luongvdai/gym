<template>
  <div class="container" ref="scrollContainer">
    <v-card
      class="branch-card py-5 px-8 text-center mx-auto ma-4"
      max-width="500"
      min-height="350"
      width="100%"
    >
      <div class="info">
        <div class="text-start">
          <div>
            <p>
              Hãy chọn cho mình 1 <strong>chi nhánh</strong> để đăng ký khóa tập
            </p>
            <div>
              <div class="text-error"><strong>Lưu ý: </strong></div>
              Sau khi chọn khóa tập thì sẽ không thể thay đổi và sẽ phải mua
              đúng những khóa tập <strong>theo chi nhánh</strong> mà bạn đã đăng
              ký.
            </div>
          </div>
        </div>
      </div>
      <MCombobox
        :Drstyle="'height: 35px;'"
        :DropboxItem="arrBranch"
        @combobox-value="selectBranch"
        :value="'id'"
        :label="'name'"
        :Filter="false"
        :maxlength="5"
        ref="combobox"
      />
      <v-btn
        border
        class="mt-6 text-none bg-surface-variant"
        rounded
        variant="plain"
        text="Xác nhận"
        height="40"
        width="135"
        :disabled="!isVerify || selectedBranch == null"
        @click="UpdateBranch()"
      ></v-btn>
      <div class="mt-2">
        <input type="checkbox" class="mx-2" v-model="isVerify" />
        <label>Chấp nhận điều khoản</label>
      </div>
      <v-btn class="otp-close" variant="fill" icon @click="closePopup()">
        <i class="ti ti-x"></i>
      </v-btn>
    </v-card>
  </div>
</template>

<script>
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import { mapMutations, mapActions } from "vuex";
import {
  BRANCH_VERIFY_SHOW_MUTATION,
  LOGIN_ACTION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import toast from "@/resouce/toast";
export default {
  name: "ChooseBranch",
  components: {
    MCombobox,
  },
  data() {
    return {
      isVerify: false,
      arrBranch: [],
      selectedBranch: null,
    };
  },
  mounted() {
    this.disableMouseScroll();
  },
  created() {
    (async () => {
      await this.getBranch();
    })();
  },
  methods: {
    closePopup() {
      this.hasBrach("Close");
    },
    ...mapActions("auth", {
      login_action: LOGIN_ACTION,
    }),
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
    }),
    ...mapMutations("auth", {
      hasBrach: BRANCH_VERIFY_SHOW_MUTATION,
    }),
    disableMouseScroll() {
      const scrollContainer = this.$refs.scrollContainer;
      scrollContainer.addEventListener("wheel", (event) => {
        event.preventDefault();
      });
    },
    selectBranch(key) {
      this.selectedBranch = key;
    },
    async getBranch() {
      try {
        await fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.arrBranch = data.content;
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
    async UpdateBranch() {
      try {
        await fetch(
          `${apiConfig}/users/update-branch?branchId=${this.selectedBranch}`,
          await fetchOptions("POST")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            if (data.status && data.status == 200) {
              this.displayToast({
                show: true,
                icon: toast.Toastcssicon_sus,
                css: toast.Toastcss_sus,
                color: toast.ToastMess_color_sus,
                message: "Chọn chi nhánh thành công",
              });
              //call api refresh token
              await fetch(
                `${apiConfig}/api/auth/refresh-token`,
                fetchOptions("POST")
              )
                .then((response) => response.json().catch(() => response))
                .then((response) => {
                  this.login_action(response);
                })
                .catch((error) => {
                  console.log(error.message);
                  this.ShowToast(3, "Lỗi máy chủ");
                });
            } else {
              this.displayToast({
                show: true,
                icon: toast.Toastcssicon_faild,
                css: toast.Toastcss_faild,
                color: toast.ToastMess_color_faild,
                message: "Người dùng không hợp lệ",
              });
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
  },
};
</script>

<style scoped>
.container {
  position: absolute;
  background: rgba(0, 0, 0, 0.1);
  min-height: 100vh;
  z-index: 9999;
}
.branch-card {
  position: absolute;
  top: 30%;
  left: 40%;
}
.otp-close {
  position: absolute;
  top: 0%;
  right: 0%;
}
</style>
